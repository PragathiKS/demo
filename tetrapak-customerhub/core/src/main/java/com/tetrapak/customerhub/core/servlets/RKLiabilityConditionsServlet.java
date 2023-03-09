package com.tetrapak.customerhub.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Get RK Liability Conditions PDF Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "customerhub/components/content/rkliabilityconditions",
        "sling.servlet.selectors=" + "getpdflinks", "sling.servlet.extensions=" + "json"
})
public class RKLiabilityConditionsServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        String preferredLanguage = request.getParameter("preferredLanguage");
    }
}
