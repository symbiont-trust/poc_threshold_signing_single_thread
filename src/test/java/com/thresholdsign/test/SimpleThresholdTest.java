package com.thresholdsign.test;

import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.Scalar;
import com.weavechain.sig.ThresholdSigEd25519;
import com.weavechain.sig.ThresholdSigEd25519Params;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleThresholdTest {

    @Test
    public void testSimpleThresholdSignature() throws Exception {
        String toSign = "test";
        ThresholdSigEd25519 tsig = new ThresholdSigEd25519(4, 7);

        // Generate parameters
        ThresholdSigEd25519Params params = tsig.generate();

        Set<Integer> nodes = new HashSet<>();
        nodes.add(1);
        nodes.add(3);
        nodes.add(4);
        nodes.add(6);

        // Round 1: Each node computes Ri
        List<EdwardsPoint> riList = new ArrayList<>();
        Map<Integer, Scalar> riStorage = new HashMap<>();

        for (int nodeId : nodes) {
            Scalar privateShare = params.getPrivateShares().get(nodeId);
            Scalar ri = tsig.computeRi(privateShare, toSign);
            EdwardsPoint riPoint = ThresholdSigEd25519.mulBasepoint(ri);

            riStorage.put(nodeId, ri);
            riList.add(riPoint);
        }

        // Coordinator computes R and k
        EdwardsPoint R = tsig.computeR(riList);
        Scalar k = tsig.computeK(params.getPublicKey(), R, toSign);

        // Round 2: Each node computes signature
        List<Scalar> partialSignatures = new ArrayList<>();
        for (int nodeId : nodes) {
            Scalar privateShare = params.getPrivateShares().get(nodeId);
            Scalar ri = riStorage.get(nodeId);
            Scalar signature = tsig.computeSignature(nodeId + 1, privateShare, ri, k, nodes);
            partialSignatures.add(signature);
        }

        // Compute final signature
        byte[] finalSignature = tsig.computeSignature(R, partialSignatures);

        // Verify
        boolean check = ThresholdSigEd25519.verify(params.getPublicKey(), finalSignature, toSign.getBytes(StandardCharsets.UTF_8));
        assertTrue(check);
        System.out.println("Simple test verification: " + (check ? "SUCCESS" : "FAILURE"));
    }
}