package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Tetra Pak Order Detail Service
 *
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {

    /** Method to generate order details pdf
     * @param response response
     * @param orderType order type
     * @param orderDetailResponse order detail response
     */
    void generateOrderDetailsPDF(SlingHttpServletResponse response,
                                 String orderType, OrderDetailsData orderDetailResponse);
}
