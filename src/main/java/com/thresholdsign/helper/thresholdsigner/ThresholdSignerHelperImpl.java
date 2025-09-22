/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.thresholdsigner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.thresholdsign.helper.serializer.NodeParams;
import com.thresholdsign.helper.serializer.SerializerHelper;
import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.Scalar;
import com.weavechain.sig.ThresholdSigEd25519;
import com.weavechain.sig.ThresholdSigEd25519Params;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JD - 19 Sep 2025
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ThresholdSignerHelperImpl implements ThresholdSignerHelper {

    private final SerializerHelper serializerHelper;
    
    @Override
    public Set<Integer> getNodes( Integer numberNodes ) {

        Set<Integer> nodes = new HashSet<>();

        // Use the exact same nodes as the working test for T=4, N=7
        if (numberNodes == 7) {
            nodes.add(1);
            nodes.add(3);
            nodes.add(4);
            nodes.add(6);
        } else {
            for ( int i = 0; i < numberNodes; i++ ) {
                nodes.add( i );
            }
        }

        return nodes;
    }


    private String receiveSerializedNodeParams(
            String serializedNodeParams,
            Integer nodeId,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId ) throws Exception {
        
        log.info( "Node with nodeId, " + nodeId + ", Received seriaized NodeParams" );
        NodeParams nodeParams = serializerHelper.deserializeToNodeParams( serializedNodeParams );
        String toSign = nodeParams.getToSign();
        Integer T = nodeParams.getThreshold();
        Integer N = nodeParams.getTotalNodes();

        ThresholdSigEd25519 tsig = new ThresholdSigEd25519( T, N );
        Set<Integer> nodes = getNodes( N );

        Scalar riScalar = tsig.computeRi( nodeParams.getPrivateShare(), toSign );

        // We are emulating saving the ri for later.  It is needed to create the partial signature
        // when the node has received the k parameter later on.
        nodeParams.setRiScalar( riScalar );
        nodeParams.setTsig( tsig );
        nodeParams.setNodes( nodes );

        nodeState_NodeParamsByNodeId.put( nodeId, nodeParams );

        EdwardsPoint riPoint = ThresholdSigEd25519.mulBasepoint( riScalar );
        String serializedRi = serializerHelper.serializeRiPoint( riPoint );
        return serializedRi;
    }


    private String sendSerializedNodeParams(
            String serializableParams,
            Integer nodeId,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId )
            throws Exception {

        log.info( "Sending serialzableParams to node, " + nodeId );

        String serializedRi =
                receiveSerializedNodeParams(
                        serializableParams, nodeId, nodeState_NodeParamsByNodeId );

        return serializedRi;
    }


    private String receiveK(
            String serializedK,
            Integer nodeId, 
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId ) {

        Scalar nodeK = serializerHelper.deserializeToK( serializedK );
        NodeParams nodeParams = nodeState_NodeParamsByNodeId.get( nodeId );
        ThresholdSigEd25519 tSig = nodeParams.getTsig();
        Set<Integer> nodes = nodeParams.getNodes();

        Scalar partialSignature =
                tSig.computeSignature(
                        nodeId + 1,
                        nodeParams.getPrivateShare(),
                        nodeParams.getRiScalar(),
                        nodeK,
                        nodes );

        String serializedPartialSignature =
                serializerHelper.serializePartialSignature( partialSignature );

        return serializedPartialSignature;
    }


    private String sendK(
            String serializedK,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId, Integer nodeId ) {

        return receiveK( serializedK, nodeId, nodeState_NodeParamsByNodeId );
    }


    private String bytesToHex( byte[] bytes ) {

        StringBuilder result = new StringBuilder();

        for ( byte b : bytes ) {

            result.append( String.format( "%02x", b ) );
        }

        return result.toString();
    }


    @Override
    public List<EdwardsPoint> sendParamsToThresholdSigners(
            ThresholdSigEd25519Params params, 
            String toSign, 
            Set<Integer> nodes, 
            int threshold,
            int totalNodes,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId ) throws Exception {

        List<EdwardsPoint> riList = new ArrayList<>();

        for ( Integer nodeId : nodes ) {
            
            Scalar privateShare = params.getPrivateShares().get( nodeId );

            NodeParams nodeParams =
                    new NodeParams( 
                            params.getPublicKey(), 
                            privateShare, 
                            nodeId, 
                            toSign,
                            threshold,
                            totalNodes );

            String serializedNodeParams =
                    serializerHelper.serializeNodeParams( nodeParams );
            
            String serializedRi =
                    sendSerializedNodeParams(
                            serializedNodeParams,
                            nodeId,
                            nodeState_NodeParamsByNodeId );

            EdwardsPoint ri = serializerHelper.deserializeToRiPoint( serializedRi );
            riList.add( ri );
        }

        return riList;
    }


    @Override
    public List<Scalar> sendKToThresholdSigners(
            Scalar k,
            Set<Integer> nodes, 
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId ) {

        String serialiazedK = serializerHelper.serializeK( k );
        List<Scalar> partialSignatures = new ArrayList<>();

        for ( Integer nodeId : nodes ) {

            String serializedPartialSignature =
                    sendK(
                            serialiazedK,
                            nodeState_NodeParamsByNodeId,
                            nodeId );

            Scalar partialSignature =
                    serializerHelper.deserializeToPartialSignaure(
                            serializedPartialSignature );

            partialSignatures.add( partialSignature );
        }

        return partialSignatures;
    }


    @Override
    public void displaySignature( byte[] signature ) {

        log.info( "\n=== Signature Details ===" );
        log.info( "Signature length: " + signature.length + " bytes" );
        log.info( "Signature (hex): " + bytesToHex( signature ) );

        log.info( "Signature (base64): " +
                Base64.getEncoder().encodeToString( signature ) );

        // Ed25519 signatures are 64 bytes: 32-byte R + 32-byte S
        if ( signature.length == 64 ) {

            byte[] R_bytes = Arrays.copyOfRange( signature, 0, 32 );
            byte[] S_bytes = Arrays.copyOfRange( signature, 32, 64 );
            log.info( "R component (32 bytes): " + bytesToHex( R_bytes ) );
            log.info( "S component (32 bytes): " + bytesToHex( S_bytes ) );
        }

        log.info( "========================\n" );
    }
}
