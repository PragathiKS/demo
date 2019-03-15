package com.tetrapak.customerhub.core.utils;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class GlobalUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);

    public static String getApiURL(APIGEEService apigeeService, String defaultJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : defaultJson;
    }

    public static String getPreferencesURL(APIGEEService apigeeService, String preferencesJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : preferencesJson;
    }

    public static ResourceResolver getResourceResolverFromSubService(
            final ResourceResolverFactory resourceFactory, final Map<String, Object> paramMap) {
        ResourceResolver resourceResolver = null;
        if (!paramMap.isEmpty()) {
            try {
                resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
                LOG.debug("resourceResolver for user {}", resourceResolver.getUserID());
            } catch (final LoginException e) {
                LOG.error("Unable to fetch resourceResolver for subservice {} exception {}",
                        paramMap.get(ResourceResolverFactory.SUBSERVICE), e);
            }
        }
        return resourceResolver;
    }

    public static void writeJsonResponse(SlingHttpServletResponse resp, JsonObject jsonResponse) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());
    }

}
