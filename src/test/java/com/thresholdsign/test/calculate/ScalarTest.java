/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.test.calculate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.weavechain.curve25519.Scalar;

public class ScalarTest {

    @Test
    public void testScalarSerialization() {

        Scalar originalScalar = Scalar.ONE;
        byte[] bytes = originalScalar.toByteArray();
        Scalar reconstructedScalar = Scalar.fromCanonicalBytes( bytes );
        Assertions.assertEquals( 1, originalScalar.ctEquals( reconstructedScalar ) );
    }
}