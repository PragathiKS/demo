package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Tetra Pak Order Detail Service
 *
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {

    /**
     * Method to generate order details pdf
     *
     * @param request             request
     * @param response            response
     * @param orderType           order type
     * @param orderDetailResponse order detail response
     * @return true if successful
     */
    boolean generateOrderDetailsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                    String orderType, OrderDetailsData orderDetailResponse);
}
