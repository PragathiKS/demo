package com.tetralaval.servlet;

import com.google.gson.Gson;
import com.tetralaval.services.ContentChangeService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_SELECTORS;

/**
 * ContentChangeServlet
 */
@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Servlet using for dynamic content changes",
                SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
                SLING_SERVLET_PATHS + "=/bin/contentchange",
                SLING_SERVLET_SELECTORS + "=do"
        }
)
public class ContentChangeServlet extends SlingSafeMethodsServlet {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentChangeServlet.class);
    /** serialVersionUID constant */
    private static final long serialVersionUID = -3696200692793415341L;

    /** contentChangeService */
    @Reference
    private transient ContentChangeService contentChangeService;

    /**
     * Dynamic content changes
     * @param request
     * @param response
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            RequestParameterMap params = request.getRequestParameterMap();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter writer = response.getWriter();
            writer.println(new Gson().toJson(contentChangeService.getResults(request, params)));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Error while writing the response object.", e);
        }
    }
}
