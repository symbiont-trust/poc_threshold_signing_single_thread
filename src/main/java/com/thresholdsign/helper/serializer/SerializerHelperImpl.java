/*
    ================================================================================================
    This code is a product of symbiont-trust.

    This code is an open source software.

    ================================================================================================
    Author : JD
    ================================================================================================
*/
package com.thresholdsign.helper.serializer;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weavechain.curve25519.EdwardsPoint;
import com.weavechain.curve25519.Scalar;

import lombok.extern.slf4j.Slf4j;

/**
 * @author JD - 22 Sep 2025
 */
@Component
@Slf4j
public class SerializerHelperImpl implements SerializerHelper {

    private Gson gson;

    public SerializerHelperImpl() {

        gson = new GsonBuilder()
                .registerTypeAdapter( Scalar.class, new ScalarTypeAdapter() )
                .registerTypeAdapter( EdwardsPoint.class, new EdwardsPointTypeAdapter() )
                .create();

        log.info( "Initialized Gson" );
    }

    @Override
    public String serializeNodeParams( NodeParams nodeParams ) {

        return gson.toJson( nodeParams );
    }


    @Override
    public NodeParams deserializeToNodeParams( String serializedNodeParams ) {

        return gson.fromJson( serializedNodeParams, NodeParams.class );
    }


    @Override
    public String serializeRiPoint( EdwardsPoint riPoint ) {

        return gson.toJson( riPoint );
    }


    @Override
    public EdwardsPoint deserializeToRiPoint( String serializedRiPoint ) {

        return gson.fromJson( serializedRiPoint, EdwardsPoint.class );
    }


    @Override
    public String serializeK( Scalar k ) {

        return gson.toJson( k );
    }


    @Override
    public Scalar deserializeToK( String serializedK ) {

        return gson.fromJson( serializedK, Scalar.class );
    }


    @Override
    public String serializePartialSignature( Scalar partialSignature ) {

        return gson.toJson( partialSignature );
    }


    @Override
    public Scalar deserializeToPartialSignaure( String serializedPartialSignature ) {

        return gson.fromJson( serializedPartialSignature, Scalar.class );
    }
}
