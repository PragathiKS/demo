package com.tetrapak.customerhub.core.servlets;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailsData;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.OrderDetailsModel;
import com.tetrapak.customerhub.core.services.OrderDetailsApiService;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * PDF and Excel Generator Servlet
 *
 * @author Nitin Kumar
 * @author Swati Lamba
 */
@Component(service = Servlet.class, property = {Constants.SERVICE_DESCRIPTION + "=PDF and Excel Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/orderdetails",
		"sling.servlet.selectors=" + "parts",
		"sling.servlet.selectors=" + "packmat",
		"sling.servlet.extensions=" + CustomerHubConstants.EXCEL,
        "sling.servlet.extensions=" + CustomerHubConstants.PDF })
public class OrderDetailsDownloadFileServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 2323660841296799482L;

	@Reference
	private OrderDetailsApiService orderDetailsApiService;

	@Reference
	private OrderDetailsPDFService generatePDF;

	@Reference
	private OrderDetailsExcelService generateExcel;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsDownloadFileServlet.class);

	@Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOGGER.debug("HTTP GET request from OrderDetailsPDFServlet");

		final String orderNumber = request.getParameter(CustomerHubConstants.ORDER_NUMBER);
		final String token = request.getParameter(CustomerHubConstants.TOKEN);

		final String[] selectors = request.getRequestPathInfo().getSelectors();
		final String orderType = selectors.length > 0 ? selectors[0] : StringUtils.EMPTY;

		final String extension = request.getRequestPathInfo().getExtension();

		JsonObject jsonResponse = orderDetailsApiService.getOrderDetails(orderNumber, token, orderType);
		JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);

		OrderDetailsModel orderDetailsModel = request.getResource().adaptTo(OrderDetailsModel.class);
		boolean flag = false;
		if (null == orderDetailsModel) {
			LOGGER.error("Order Details Model is null");
		} else if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(status.toString())) {
			LOGGER.error("Unable to retrieve response from API got status code:{}", status);
		} else {
			JsonElement result = jsonResponse.get(CustomerHubConstants.RESULT);
			Gson gson = new Gson();
			OrderDetailsData orderDetailResponse = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(result),
					OrderDetailsData.class);

			if (CustomerHubConstants.PDF.equals(extension)) {
				flag = generatePDF.generateOrderDetailsPDF(request, response, orderType, orderDetailResponse,
						orderDetailsModel);
			} else if (CustomerHubConstants.EXCEL.equals(extension)) {
				flag = generateExcel.generateOrderDetailsExcel(request, response, orderType, orderDetailResponse,
						orderDetailsModel);
			} else {
				LOGGER.error("File type not specified for the download operation.");
			}
		}

		if (!flag) {
			LOGGER.error("Order details file download failed!");
			HttpUtil.sendErrorMessage(response);
		}
	}

	
}
