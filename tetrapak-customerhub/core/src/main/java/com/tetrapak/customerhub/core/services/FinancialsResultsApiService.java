package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

/**
 * Tetra Pak Financials Reults Service
 * @author Ruhee Sharma
 */
@FunctionalInterface
public interface FinancialsResultsApiService {

    /** Method to get Financials Results from TETRA PAK API
     * @param status status
     * @param documentType documenttype
     * @param invoiceDateFrom invoicedatefrom
     * @param customerkey customerkey
     * @param token token
     * @return json object
     */
    JsonObject getFinancialsResults(String status, String documentType, String invoiceDateFrom,String customerkey, String token);
}
