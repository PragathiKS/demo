package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
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
     * @param status          status
     * @param documentType    document type
     * @param invoiceDateFrom invoice date from
     * @param invoiceDateTo   invoice date to
     * @param soaDate         soa date
     * @param customerNumber  customer Number
     * @param token           token
     * @return json object
     */
    JsonObject getFinancialResults(String status, String documentType, String invoiceDateFrom, String invoiceDateTo,
                                   String soaDate, String customerNumber, String token);

    /**
     * Method to get the PDF of the invoice of the given document number
     *
     * @param documentNumber document number
     * @param token          token
     * @return finance invoice
     */
    HttpResponse getFinancialInvoice(String documentNumber, String token);
}
