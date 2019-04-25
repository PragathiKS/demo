package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

/**
 * Tetra Pak Order Detail Service
 * @author Ruhee Sharma
 */
@FunctionalInterface
public interface FinancialsResultsApiService {

    /** Method to get order details from TETRA PAK API
     * @param orderNumber order number
     * @param token token
     * @param orderType order type
     * @return json object
     */
    JsonObject getFinancialsResults(String status, String documentType, String invoiceDateFrom,String customerkey, String token);
}
