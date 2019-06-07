package com.tetrapak.customerhub.core.services;

import org.apache.http.HttpResponse;

import com.google.gson.JsonObject;

/**
 * Tetra Pak Financials Reults Service
 * @author ruhsharm
 */
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

    /**
     * 
     * Method to get the PDF of the invoice of the given document number
     * 
     * @param documentNumber
     * @param token
     * @return
     */
    HttpResponse getFinancialsInvoice(String documentNumber, String token);
}
