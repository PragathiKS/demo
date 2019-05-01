package com.tetrapak.customerhub.core.services;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;

/**
 * Tetra Pak Financials Results Service
 *
 * @author Ruhee Sharma
 */
@FunctionalInterface
public interface FinancialsResultsPDFService {

    /** Method to generate financials results pdf
     * @param request SlingHttpServletRequest
     * @param response SlingHttpServletResponse
     * @param resultsResponse Results 
     * @param paramRequest RequestParams
     * @param financialStatementModel FinancialStatementModel
     * @return true or false
     */
    boolean generateFinancialsResultsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                  Results resultsResponse,Params paramRequest, FinancialStatementModel financialStatementModel);
}
