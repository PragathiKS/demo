package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + RebuildingKitsUpdateServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + RebuildingKitsUpdateServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + RebuildingKitsUpdateServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class RebuildingKitsUpdateServlet extends SlingAllMethodsServlet {
    public static final String SLING_SERVLET_SELECTOR = "update";
    public static final String SLING_SERVLET_EXTENSION = CustomerHubConstants.JSON_SERVLET_EXTENSION;
    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/rebuildingkitdetails";

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {

    }
}
