package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailResponse;

import java.util.List;

/**
 * Service to create Excel file for order details.
 * @author Tushar
 *
 */
@FunctionalInterface
public interface OrderDetailsExcelService {
	/**
	 * Method to generate the excel file for the order details page.
	 * 
	 * @param response Response
	 * @param orderType Order Type
	 * @param orderDetailData
	 */
	void generateOrderDetailsExcel(SlingHttpServletResponse response,String orderType, OrderDetailResponse orderDetailData);

}
