package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**Servlet class for Saving user's preferences
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Order Prefernces Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + "customerhub/components/content/orderingcard",
                "sling.servlet.selector=" + "preference",
                "sling.servlet.extensions=" + "json"
        })
public class SaveOrderPreferencesServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String TETRAPAK_USER = "customerhubUser";

    private static final String ORDER_PREFERENCES = "orderPreferences";

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveOrderPreferencesServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws IOException {
        LOGGER.info("SaveOrderPreferencesServlet POST method started");

        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, TETRAPAK_USER);
        ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory, paramMap);

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
            return;
        }
        String userId = session.getUserID();
        if (null != resourceResolver) {
            UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            if (null == userManager) {
                writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
                return;
            }
            try {
                Authorizable user = userManager.getAuthorizable(userId);
                String prefFromRequest = request.getParameter("fields");
                String[] preferList = prefFromRequest.split(",");

                String path = user.getPath();
                Resource userResource = resourceResolver.getResource(path);
                if (null == userResource) {
                    writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
                    return;
                }
                ModifiableValueMap properties = userResource.adaptTo(ModifiableValueMap.class);
                if (null == properties) {
                    writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
                } else {
                    properties.put(ORDER_PREFERENCES, preferList);
                    resourceResolver.commit();
                    writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_SUCCESS);
                }
            } catch (RepositoryException e) {
                LOGGER.error("RepositoryException in SaveOrderPreferencesServlet", e);
            } finally {
                if (null != resourceResolver && resourceResolver.isLive()) {
                    resourceResolver.close();
                }
            }
        }
    }

    private void writeJsonResponse(SlingHttpServletResponse resp, String status) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        GlobalUtil.writeJsonResponse(resp, jsonResponse);
    }
}
