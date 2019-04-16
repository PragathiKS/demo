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

    void generateOrderDetailsPDF(SlingHttpServletResponse response,
                                 String orderType, OrderDetailsData orderDetailResponse);
}
