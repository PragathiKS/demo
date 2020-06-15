package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tetrapak.publicweb.core.services.PardotService;

/**
 * The Class Business Inquiry Form Submit Request Servlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Subscription form Submit Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.selectors=" + "pardotsubscription", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/content/subscriptionform" })
public class SubscriptionFormPardotServlet extends SlingAllMethodsServlet {

    /**
     *
     */
    private static final long serialVersionUID = -4582610735374949058L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionFormPardotServlet.class);

    @Reference
    private PardotService pardotService;

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param resp
     *            the resp
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        try {
            pardotService.submitPardotPostRespose(request.getParameterMap(),
                    pardotService.getSubscriptionFormPardotURL());
            // send response
            sendResponse(resp);

        } catch (final IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }


    /**
     * Sends HTTPServlet response
     *
     * @param resp
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void sendResponse(final SlingHttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("Success");
    }


}
