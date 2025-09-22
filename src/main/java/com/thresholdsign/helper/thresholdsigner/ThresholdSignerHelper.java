/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.thresholdsigner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thresholdsign.helper.serializer.NodeParams;
import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.Scalar;
import com.weavechain.sig.ThresholdSigEd25519Params;

/**
 * @author JD - 19 Sep 2025
 */
public interface ThresholdSignerHelper {

    Set<Integer> getNodes( Integer numberNodes );


    List<EdwardsPoint> sendParamsToThresholdSigners(
            ThresholdSigEd25519Params params, 
            String toSign, 
            Set<Integer> nodes, 
            int threshold,
            int totalNodes,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId ) throws Exception;


    List<Scalar> sendKToThresholdSigners(
            Scalar k,
            Set<Integer> nodes,
            Map<Integer, NodeParams> nodeState_NodeParamsByNodeId );


    void displaySignature( byte[] signature );
}
