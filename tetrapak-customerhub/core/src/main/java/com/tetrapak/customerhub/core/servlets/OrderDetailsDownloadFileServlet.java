package com.tetrapak.customerhub.core.servlets;

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

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PDF and Excel Generator Servlet
 *
 * @author Nitin Kumar
 * @author Swati Lamba
 */
@Component(service = Servlet.class, property = {Constants.SERVICE_DESCRIPTION + "=PDF and Excel Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/orderdetails",
        "sling.servlet.extension=[" + CustomerHubConstants.PDF + "," + CustomerHubConstants.EXCEL + "]"})
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
            response.setStatus(Integer.parseInt(status.toString()));
            try {
                HttpUtil.writeJsonResponse(response, jsonResponse);
            } catch (IOException e) {
                LOGGER.error("IOException in OrderDetailsDownloadFileServlet {}", e);
            }
            LOGGER.error("Unable to retrieve response from API");
        } else {
            JsonElement result = jsonResponse.get(CustomerHubConstants.RESULT);
            Gson gson = new Gson();
            OrderDetailsData orderDetailResponse = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(result),
                    OrderDetailsData.class);

            if (CustomerHubConstants.PDF.equals(extension)) {
                flag = generatePDF.generateOrderDetailsPDF(request, response, orderType, orderDetailResponse, orderDetailsModel);

            } else if (CustomerHubConstants.EXCEL.equals(extension)) {
                flag = generateExcel.generateOrderDetailsExcel(request, response, orderType, orderDetailResponse,
                        orderDetailsModel);
            } else {
                LOGGER.error("File type not specified for the download operation.");
            }
        }
        if (!flag) {
            sendErrorMessage(response);
        }
    }

    private void sendErrorMessage(SlingHttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("errorMsg", "Some internal server error occurred while processing the request!");
            HttpUtil.writeJsonResponse(response, obj);
        } catch (IOException e) {
            LOGGER.error("IOException in OrderDetailsDownloadFileServlet {}", e);
        }
    }
}
