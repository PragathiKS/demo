package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
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

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws IOException {
        LOG.info("SaveOrderPreferencesServlet POST method started");

        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, TETRAPAK_USER);
        ResourceResolver resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory, paramMap);

        Session session = req.getResourceResolver().adaptTo(Session.class);
        String userId = session.getUserID();
        if (null != resourceResolver) {
            UserManager userManager = resourceResolver.adaptTo(UserManager.class);
            try {
                Authorizable user = userManager.getAuthorizable(userId);
                String prefFromRequest = req.getParameter("fields");
                String[] preferList = prefFromRequest.split(",");

                String path = user.getPath();
                Resource userResource = resourceResolver.getResource(path);
                ModifiableValueMap properties = userResource.adaptTo(ModifiableValueMap.class);
                properties.put(ORDER_PREFERENCES, preferList);
                resourceResolver.commit();

                resp.setContentType("application/json");

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("status", "success");
                resp.getWriter().write(jsonResponse.toString());

            } catch (RepositoryException e) {
                LOG.error("RepositoryException in SaveOrderPreferencesServlet", e);
            } finally {
                if (null != resourceResolver && resourceResolver.isLive()) {
                    resourceResolver.close();
                }
            }
        }


    }
}
