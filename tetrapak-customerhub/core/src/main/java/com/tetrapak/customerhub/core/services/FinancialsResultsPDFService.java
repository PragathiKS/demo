package com.tetrapak.customerhub.core.services;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import com.tetrapak.customerhub.core.beans.financials.results.RequestParams;
import com.tetrapak.customerhub.core.beans.financials.results.Results;

/**
 * Tetra Pak Order Detail Service
 *
 * @author Ruhee Sharma
 */
@FunctionalInterface
public interface FinancialsResultsPDFService {

    /** Method to generate order details pdf
     * @param response response
     * @param orderType order type
     * @param orderDetailResponse order detail response
     */
    void generateFinancialsResultsPDF(SlingHttpServletResponse response,
                                  Results resultsResponse,RequestParams paramRequest);
}
