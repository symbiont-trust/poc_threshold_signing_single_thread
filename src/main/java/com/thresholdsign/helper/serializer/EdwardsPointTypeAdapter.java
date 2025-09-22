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
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.weavechain.curve25519.CompressedEdwardsY;
import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.InvalidEncodingException;

/**
 * @author JD - 22 Sep 2025
 */
public class EdwardsPointTypeAdapter
        implements JsonSerializer<EdwardsPoint>, JsonDeserializer<EdwardsPoint> {

    @Override
    public JsonElement serialize( EdwardsPoint src, Type typeOfSrc,
            JsonSerializationContext context ) {

        return new JsonPrimitive( Base64.getEncoder().encodeToString( src.compress()
                .toByteArray() ) );
    }


    @Override
    public EdwardsPoint deserialize( JsonElement json, Type typeOfT,
            JsonDeserializationContext context ) {

        try {
            byte[] bytes = Base64.getDecoder().decode( json.getAsString() );
            return new CompressedEdwardsY( bytes ).decompress();
        }
        catch ( InvalidEncodingException e ) {
            throw new JsonParseException( "Failed to deserialize EdwardsPoint", e );
        }
    }
}
