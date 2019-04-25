package com.tetrapak.customerhub.core.services;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import com.tetrapak.customerhub.core.beans.financials.results.RequestParams;
import com.tetrapak.customerhub.core.beans.financials.results.Results;

/**
 * Tetra Pak Financials Results Service
 *
 * @author Ruhee Sharma
 */
@FunctionalInterface
public interface FinancialsResultsPDFService {

    /** Method to generate financials results pdf
     * @param response SlingHttpServletResponse
     * @param resultsResponse Results 
     * @param paramRequest RequestParams
     * @return 
     */
    boolean generateFinancialsResultsPDF(SlingHttpServletResponse response,
                                  Results resultsResponse,RequestParams paramRequest);
}
