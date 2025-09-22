/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.serializer;

import java.lang.reflect.Type;
import java.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.weavechain.curve25519.Scalar;

/**
 * @author JD - 22 Sep 2025
 */
public class ScalarTypeAdapter implements JsonSerializer<Scalar>, JsonDeserializer<Scalar> {

    @Override
    public JsonElement serialize( Scalar src, Type typeOfSrc, JsonSerializationContext context ) {

        return new JsonPrimitive( Base64.getEncoder().encodeToString( src.toByteArray() ) );
    }


    @Override
    public Scalar deserialize( JsonElement json, Type typeOfT,
            JsonDeserializationContext context ) {

        byte[] bytes = Base64.getDecoder().decode( json.getAsString() );
        return Scalar.fromBits( bytes );
    }
}
