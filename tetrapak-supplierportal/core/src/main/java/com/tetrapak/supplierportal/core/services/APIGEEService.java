package com.tetrapak.supplierportal.core.services;

import java.io.IOException;

import com.google.gson.JsonObject;

/**
 * API GEE Service class
 */
public interface APIGEEService {

    String getApigeeServiceUrl();

    String[] getApiMappings();

    JsonObject retrieveAPIGEEToken(String accToken) throws  IOException;

}

