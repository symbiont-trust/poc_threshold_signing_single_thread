/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.simulator;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thresholdsign.helper.serializer.NodeParams;
import com.thresholdsign.helper.thresholdsigner.ThresholdSignerHelper;
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
public class SimulatorImpl implements Simulator {

    private final ThresholdSignerHelper thresholdSignerHelper;

    @Value( "${startup.T}" )
    private Integer T;

    @Value( "${startup.N}" )
    private Integer N;

    @Value( "${startup.stringToSign}" )
    private String toSign;

    // This holds Node state.  As we are not making network calls we still emulate state on 
    // the nodes
    private Map<Integer, NodeParams> nodeState_NodeParamsByNodeId = new HashMap<>();


    @Override
    public void start() throws Exception {

        // The code in this start is carried out by the Coordinator

        Set<Integer> nodes = thresholdSignerHelper.getNodes( N );
        ThresholdSigEd25519 tsig = new ThresholdSigEd25519( T, N );

        // This has the public key and private key shares in it
        ThresholdSigEd25519Params params = tsig.generate();

        // Here we send each threshold signer their private key share and the public key.  We also
        // send the text toSign.  Note that the stuff we send to the threshold signers is 
        // encapsulated in NodeParams.  Note that the threshold signer calculate:
        //
        //      Scalar ri = tsig.computeRi( nodeParams.getPrivateShare(), toSign );
        //
        // They save ri for later.  But they return EdwardsPoint riPoint.  riPoint is created from 
        // ri:
        //
        //     EdwardsPoint riPoint = ThresholdSigEd25519.mulBasepoint( ri );
        //
        List<EdwardsPoint> riList =
                thresholdSignerHelper.sendParamsToThresholdSigners(
                        params, toSign, nodes, T, N, nodeState_NodeParamsByNodeId );

        // The coordinator now calculates k which it will then send back to the threshold signers.
        EdwardsPoint R = tsig.computeR( riList );
        Scalar k = tsig.computeK( params.getPublicKey(), R, toSign );

        List<Scalar> partialSignatures =
                thresholdSignerHelper.sendKToThresholdSigners(
                        k, nodes, nodeState_NodeParamsByNodeId );

        byte[] finalSignature = tsig.computeSignature( R, partialSignatures );

        boolean check =
                ThresholdSigEd25519.verify(
                        params.getPublicKey(),
                        finalSignature,
                        toSign.getBytes( StandardCharsets.UTF_8 ) );

        log.info( "Final Signature is valid = " + check );

        thresholdSignerHelper.displaySignature( finalSignature );
    }
}
