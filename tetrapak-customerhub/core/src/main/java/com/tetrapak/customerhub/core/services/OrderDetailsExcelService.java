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
	 * 
	 * @param request
	 * @param response
	 * @param orderType
	 * @param orderDetailData
	 * @param orderDetailsModel
	 * @return true if successful generation otherwise false
	 */
	boolean generateOrderDetailsExcel(SlingHttpServletRequest request, SlingHttpServletResponse response,
			String orderType, OrderDetailsData orderDetailData, OrderDetailsModel orderDetailsModel);

}
