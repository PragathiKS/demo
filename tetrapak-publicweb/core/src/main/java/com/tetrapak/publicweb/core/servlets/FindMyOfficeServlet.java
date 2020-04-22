package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.services.impl.FindMyOfficeServiceImpl;

/**
 * The Class FindMyOfficeServlet.
 */
@Component(
        service = Servlet.class,
        property = { Constants.SERVICE_DESCRIPTION + "=Find My office data Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "publicweb/components/content/findMyOffice" })
public class FindMyOfficeServlet extends SlingSafeMethodsServlet {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FindMyOfficeServlet.class);

    private static final long serialVersionUID = -5454246014078059527L;

    @Reference
    private FindMyOfficeServiceImpl findMyOfficeServiceImpl;

    /**
     * Do get.
     *
     * @param request
     *            the request
     * @param resp
     *            the resp
     */
    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse resp) {
        final ResourceResolver resolver = request.getResourceResolver();
        JSONObject jsonObject = new JSONObject(findMyOfficeServiceImpl.getFindMyOfficeData(resolver));
        final String responseString = jsonObject.toString();
        try {
            resp.getWriter().write(responseString);
        } catch (IOException ioException) {
            LOGGER.error("ioException :{}", ioException);
        }
    }
}
