/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.serializer;

import java.util.Set;

import com.weavechain.curve25519.Scalar;
import com.weavechain.sig.ThresholdSigEd25519;

/**
 * @author JD - 22 Sep 2025
 */
public class NodeParams {

    private final byte[] publicKey;
    private final Scalar privateShare;
    private final int nodeId;
    private final String toSign;
    private final int threshold; // T
    private final int totalNodes; // N

    // When the signer receives the privateShare it calculates these values:
    private Scalar riScalar;
    private ThresholdSigEd25519 tsig;
    private Set<Integer> nodes;

    public NodeParams(
            byte[] publicKey,
            Scalar privateShare,
            int nodeId,
            String toSign,
            int threshold,
            int totalNodes ) {

        this.publicKey = publicKey;
        this.privateShare = privateShare;
        this.nodeId = nodeId;
        this.toSign = toSign;
        this.threshold = threshold;
        this.totalNodes = totalNodes;
    }


    public Scalar getRiScalar() {

        return riScalar;
    }


    public void setRiScalar( Scalar riScalar ) {

        this.riScalar = riScalar;
    }


    public ThresholdSigEd25519 getTsig() {

        return tsig;
    }


    public void setTsig( ThresholdSigEd25519 tsig ) {

        this.tsig = tsig;
    }


    public Set<Integer> getNodes() {

        return nodes;
    }


    public void setNodes( Set<Integer> nodes ) {

        this.nodes = nodes;
    }


    public byte[] getPublicKey() {

        return publicKey;
    }


    public Scalar getPrivateShare() {

        return privateShare;
    }


    public int getNodeId() {

        return nodeId;
    }


    public String getToSign() {

        return toSign;
    }


    public int getThreshold() {

        return threshold;
    }


    public int getTotalNodes() {

        return totalNodes;
    }
}
