package com.tetrapak.customerhub.core.servlets;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Order Prefernces Servlet",
                "sling.servlet.paths=" + "/bin/userServlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "/customerhub/components/content/orderingcard",
                "sling.servlet.extensions=" + "json"
        })
public class OrderPreferencesServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String ORDER_PREFERENCES = "orderPreferences";

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws IOException {

        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        String userId = session.getUserID();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        try {
            Authorizable user = userManager.getAuthorizable(userId);
            String prefFromRequest = req.getParameter("savedPreferences");
            String[] preferList = prefFromRequest.split(",");

            String path = user.getPath();
            Resource userResource = resourceResolver.getResource(path);
            ModifiableValueMap properties = userResource.adaptTo(ModifiableValueMap.class);
            properties.put(ORDER_PREFERENCES, preferList);
            resourceResolver.commit();


        } catch (RepositoryException e) {
        }

    }
}
