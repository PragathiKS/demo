package com.tetrapak.customerhub.core.services;

import java.util.List;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderSummary;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;

/**
 * Tetra Pak Order Detail Service
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {
	
	void generateOrderDetailsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                 String orderType, OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList, List<OrderSummary> orderSummaryList);
}
