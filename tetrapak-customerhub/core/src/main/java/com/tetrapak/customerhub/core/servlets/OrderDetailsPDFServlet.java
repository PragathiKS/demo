package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.OrderDetailsPDFService;
import com.tetrapak.customerhub.core.services.OrderDetailsService;
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

/**
 * PDF Generator Servlet
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=PDF Generator Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/customerhub/order-detail-pdf"
        })
public class OrderDetailsPDFServlet extends SlingSafeMethodsServlet {

    @Reference
    OrderDetailsService orderDetailsService;

    @Reference
    OrderDetailsPDFService orderDetailsPDFService;

    private static final long serialVersionUID = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsPDFServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        LOGGER.debug("HTTP GET request from OrderDetailsPDFServlet");

        final String orderNumber = request.getParameter("orderNumber");
        final String token = request.getParameter("token");

        JsonObject jsonResponse = orderDetailsService.getOrderDetails(orderNumber, token);

        JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);
        if (!status.toString().equalsIgnoreCase("200")) {
            LOGGER.error("could not get successful response from API");
        } else {
            orderDetailsPDFService.generateOrderDetailsPDF(request, response, jsonResponse);
        }
    }
}
