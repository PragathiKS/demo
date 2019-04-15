package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryList;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetailResponse;
import com.tetrapak.customerhub.core.beans.oderdetails.OrderDetails;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.OrderDetailsExcelService;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.services.OrderDetailsService;
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
import java.util.List;

/**
 * PDF Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.extension=[" + CustomerHubConstants.PDF + "," + CustomerHubConstants.EXCEL + "]",
                "sling.servlet.paths=" + "/bin/customerhub/order-detail-pdf"
        })
public class OrderDetailsDownloadFileServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 2323660841296799482L;

    @Reference
    OrderDetailsService orderDetailsService;

    @Reference
    OrderDetailsPDFService generatePDF;

    @Reference
    OrderDetailsExcelService generateExcel;


    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsDownloadFileServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("HTTP GET request from OrderDetailsPDFServlet");

        final String orderNumber = request.getParameter("orderNumber");
        final String token = request.getParameter("token");

        final String[] selectors = request.getRequestPathInfo().getSelectors();
        final String orderType = selectors.length > 0 ? selectors[0] : StringUtils.EMPTY;

        final String extension = request.getRequestPathInfo().getExtension();

        JsonObject jsonResponse = orderDetailsService.getOrderDetails(orderNumber, token, orderType);
        JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);

        if (!status.toString().equalsIgnoreCase("200")) {
            LOGGER.error("Unable to retrieve response from API");
        } else {
            JsonElement result = jsonResponse.get(CustomerHubConstants.RESULT);
            Gson gson = new Gson();
            OrderDetailResponse orderDetailResponse = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(result),
                    OrderDetailResponse.class);
            OrderDetails orderDetails = orderDetailResponse.getOrderDetails();
            CustomerSupportCenter customerSupportCenter = orderDetailResponse.getCustomerSupportCenter();
            List<DeliveryList> deliveryList = orderDetailResponse.getDeliveryList();

            if ("pdf".equals(extension)) {
                generatePDF.generateOrderDetailsPDF(request, response, orderDetails, customerSupportCenter, deliveryList);
            } else if ("excel".equals(extension)) {
                generateExcel.generateOrderDetailsExcel(request, response, orderDetails, customerSupportCenter, deliveryList);
            } else {
                LOGGER.error("File type not specified for the download operation.");
            }

        }
    }
}
