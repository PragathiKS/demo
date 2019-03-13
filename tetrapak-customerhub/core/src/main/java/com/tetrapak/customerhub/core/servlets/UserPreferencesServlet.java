package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Save User Preferences Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.resourceTypes=" + "customerhub/components/content/orderingcard",
                "sling.servlet.selector=" + "userPreference",
                "sling.servlet.extensions=" + "json"
        })
public class UserPreferencesServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private static final String ORDER_PREFERENCES = "orderPreferences";

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) {
        ResourceResolver resourceResolver = req.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        String userId = session.getUserID();
        UserManager userManager = resourceResolver.adaptTo(UserManager.class);
        try {
            Authorizable user = userManager.getAuthorizable(userId);

            String path = user.getPath();
            Resource userResource = resourceResolver.getResource(path);
            ValueMap map = userResource.getValueMap();
            if (map.containsKey(ORDER_PREFERENCES)) {
                String savedPreferences = (String) map.get(ORDER_PREFERENCES);

                String[] preferList = savedPreferences.split(",");
            }
            resp.setContentType("application/json");

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            resp.getWriter().write(jsonResponse.toString());

        } catch (RepositoryException | IOException e) {
            LOG.error("Exception in UserPreferencesServlet", e);
        }

    }
}
