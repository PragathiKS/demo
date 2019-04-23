package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

/**
 * Service to create Excel file for order details.
 *
 * @author Tushar
 */
@FunctionalInterface
public interface OrderDetailsExcelService {
	/**
	 * Method to generate the excel file for the order details page.
	 * @param request 
	 *
	 * @param response          Response
	 * @param orderType         Order Type
	 * @param orderDetailData   order details data
	 * @param orderDetailsModel order details model
	 */
	void generateOrderDetailsExcel(SlingHttpServletRequest request, SlingHttpServletResponse response, String orderType,
			OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel);

}
