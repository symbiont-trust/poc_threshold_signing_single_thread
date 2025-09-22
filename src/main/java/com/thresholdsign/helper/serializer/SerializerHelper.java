/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.serializer;

import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.Scalar;

/**
 * @author JD - 22 Sep 2025
 */
public interface SerializerHelper {

    // NodeParams ==================================================================================

    String serializeNodeParams( NodeParams nodeParams );

    NodeParams deserializeToNodeParams( String serializedNodeParams );


    // EdwardsPoint riPoint ========================================================================

    String serializeRiPoint( EdwardsPoint riPoint );

    EdwardsPoint deserializeToRiPoint( String serializedRiPoint );


    // Scalar k ====================================================================================

    String serializeK( Scalar k );

    Scalar deserializeToK( String serializedK );


    // Scalar partialSignature =====================================================================
    
    String serializePartialSignature( Scalar partialSignature );

    Scalar deserializeToPartialSignaure( String serializedPartialSignature );
}
