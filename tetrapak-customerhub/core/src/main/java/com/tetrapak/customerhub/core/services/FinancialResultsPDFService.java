package com.tetrapak.customerhub.core.services;


import com.tetrapak.customerhub.core.beans.financials.results.Params;
import com.tetrapak.customerhub.core.beans.financials.results.Results;
import com.tetrapak.customerhub.core.models.FinancialStatementModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Tetra Pak Financial Results Service
 *
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface FinancialResultsPDFService {

    /**
     * Method to generate financial results pdf
     *
     * @param request                 SlingHttpServletRequest
     * @param response                SlingHttpServletResponse
     * @param resultsResponse         Results
     * @param paramRequest            RequestParams
     * @param financialStatementModel FinancialStatementModel
     * @return true or false
     */
    boolean generateFinancialResultsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                        Results resultsResponse, Params paramRequest, FinancialStatementModel financialStatementModel);
}
