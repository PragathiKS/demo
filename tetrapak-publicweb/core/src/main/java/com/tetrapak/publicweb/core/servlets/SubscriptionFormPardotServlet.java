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

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * The Class SubscriptionFormPardotServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Subscription form Submit Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.selectors=" + "pardotsubscription", "sling.servlet.extensions=" + "json",
                "sling.servlet.resourceTypes=" + "publicweb/components/content/textImage",
                "sling.servlet.resourceTypes=" + "publicweb/components/content/textVideo",
                "sling.servlet.resourceTypes=" + "publicweb/components/content/banner" })
public class SubscriptionFormPardotServlet extends SlingAllMethodsServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4582610735374949058L;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionFormPardotServlet.class);

    /** The pardot service. */
    @Reference
    private transient PardotService pardotService;

    /**
     * Do post.
     *
     * @param request the request
     * @param resp the resp
     */
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        try {                       
            if (GlobalUtil.isChinaDataFlow(request)) {
                submitCustomFormData(request);
            } else {
                pardotService.submitPardotPostRespose(request.getParameterMap());
            }             
            // send response
            sendResponse(resp);
        } catch (final IOException ioException) {
            LOGGER.error("ioException :{}", ioException.getMessage(), ioException);
        }
    }

    /**
     * Submit custom form data.
     *
     * @param request the request
     */
    private void submitCustomFormData(final SlingHttpServletRequest request) {
        try {
            pardotService.submitcustomFormServicePostResponse(request.getParameterMap());
        } catch (Exception e) {
            LOGGER.error("Error occurred while submission of form data {}", e.getMessage());
        }
    }

    /**
     * Send response.
     *
     * @param resp the resp
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendResponse(final SlingHttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(PWConstants.STATUS_SUCCESS);
    }
}
