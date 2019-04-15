package com.tetrapak.customerhub.core.services;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.pdf.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.pdf.DeliveryList;
import com.tetrapak.customerhub.core.beans.pdf.OrderDetails;

/**
 * Tetra Pak Order Detail Service
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface OrderDetailsPDFService {
	
	void generateOrderDetailsPDF(SlingHttpServletRequest request, SlingHttpServletResponse response,
			OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList);
}
