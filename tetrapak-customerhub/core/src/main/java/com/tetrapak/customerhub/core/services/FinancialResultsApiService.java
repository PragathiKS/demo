package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.financials.results.Params;
import org.apache.http.HttpResponse;

/**
 * Tetra Pak Financials Reults Service
 *
 * @author Nitin Kumar
 */
public interface FinancialResultsApiService {

    /**
     * Method to get Financials Results from TETRA PAK API
     *
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
    JsonObject getFinancialResults(Params paramsRequest, String token);

    /**
     * Method to get the PDF of the invoice of the given document number
     *
     * @param documentNumber document number
     * @param token          token
     * @return finance invoice
     */
    HttpResponse getFinancialInvoice(String documentNumber, String token);
}
