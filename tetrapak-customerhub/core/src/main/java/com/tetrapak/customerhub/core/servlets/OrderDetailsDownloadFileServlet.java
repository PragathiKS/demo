package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.oderdetails.*;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;

/**
 * PDF Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.extension=[" + CustomerHubConstants.PDF + "," + CustomerHubConstants.EXCEL + "]",
		"sling.servlet.paths=" + "/bin/customerhub/order-detail" })
public class OrderDetailsDownloadFileServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2323660841296799482L;

	@Reference
	private OrderDetailsApiService orderDetailsApiService;

	@Reference
	private OrderDetailsPDFService generatePDF;

	@Reference
	private OrderDetailsExcelService generateExcel;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsDownloadFileServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.debug("HTTP GET request from OrderDetailsPDFServlet");

		final String orderNumber = request.getParameter("orderNumber");
		final String token = request.getParameter("token");

		final String[] selectors = request.getRequestPathInfo().getSelectors();
		final String orderType = selectors.length > 0 ? selectors[0] : StringUtils.EMPTY;

		final String extension = request.getRequestPathInfo().getExtension();

		JsonObject jsonResponse = orderDetailsApiService.getOrderDetails(orderNumber, token, orderType);
		JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);

		if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(status.toString())) {
			response.setStatus(Integer.parseInt(status.toString()));
			try {
				HttpUtil.writeJsonResponse(response, jsonResponse);
			} catch (IOException e) {
				LOGGER.error("IOException in OrderDetailsDownloadFileServlet {}", e.getMessage());
			}
			LOGGER.error("Unable to retrieve response from API");
		} else {
			JsonElement result = jsonResponse.get(CustomerHubConstants.RESULT);
			Gson gson = new Gson();
			OrderDetailsData orderDetailResponse = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(result),
					OrderDetailsData.class);
			OrderDetails orderDetails = orderDetailResponse.getOrderDetails();
			CustomerSupportCenter customerSupportCenter = orderDetailResponse.getCustomerSupportCenter();
			List<DeliveryList> deliveryList = orderDetailResponse.getDeliveryList();
			List<OrderSummary> orderSummaryList = orderDetailResponse.getOrderSummary();

			if (CustomerHubConstants.PDF.equals(extension)) {
				generatePDF.generateOrderDetailsPDF(request, response, orderType, orderDetails, customerSupportCenter,
						deliveryList, orderSummaryList);
			} else if (CustomerHubConstants.EXCEL.equals(extension)) {
				generateExcel.generateOrderDetailsExcel(response, orderType, orderDetailResponse);
			} else {
				LOGGER.error("File type not specified for the download operation.");
			}

		}
	}
}
