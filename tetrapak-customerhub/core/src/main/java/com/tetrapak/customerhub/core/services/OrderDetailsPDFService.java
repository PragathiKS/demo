package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import org.apache.sling.api.SlingHttpServletResponse;

import java.util.List;

/**
 * Tetra Pak Order Detail Service
 *
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {

    void generateOrderDetailsPDF(SlingHttpServletResponse response,
                                 String orderType, OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList, List<OrderSummary> orderSummaryList);
}
