package com.tetrapak.customerhub.core.services;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;

/**
 * Service to create Excel file for order details.
 * @author Tushar
 *
 */
@FunctionalInterface
public interface OrderDetailsExcelService {

    void generateOrderDetailsExcel(SlingHttpServletRequest request, SlingHttpServletResponse response,
			OrderDetails orderDetails, CustomerSupportCenter customerSupportCenter, List<DeliveryList> deliveryList);
}
