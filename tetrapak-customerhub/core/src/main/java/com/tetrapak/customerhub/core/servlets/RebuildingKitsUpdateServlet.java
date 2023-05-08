package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.beans.rebuildingkits.ImplementationStatusUpdateBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + RebuildingKitsUpdateServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + RebuildingKitsUpdateServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + RebuildingKitsUpdateServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class RebuildingKitsUpdateServlet extends SlingAllMethodsServlet {
    public static final String SLING_SERVLET_SELECTOR = "update";
    public static final String SLING_SERVLET_EXTENSION = "json";
    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/rebuildingkitdetails";

    private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsUpdateServlet.class);

    private static final String AUTH_TOKEN = "authToken";

    @Reference
    private transient XSSAPI xssAPI;

    @Reference
    private RebuildingKitsApiService rebuildingKitsApiService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: RK Implementation Status Update - Post");
        Session session = request.getResourceResolver().adaptTo(Session.class);
        JsonObject jsonObject = new JsonObject();
        if (null == session) {
            LOGGER.error("RK Implementation Status Update servlet exception: session is null");
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "session is null", HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonObject);
            return;
        }
        String requestString = xssAPI.getValidJSON(IOUtils.toString(request.getReader()), StringUtils.EMPTY);
        Gson gson = new Gson();
        ImplementationStatusUpdateBean bean = gson.fromJson(requestString, ImplementationStatusUpdateBean.class);
        final String token = getAuthTokenValue(request);
        if (bean != null && bean.isValid() && StringUtils.isNotEmpty(token)) {
            jsonObject = rebuildingKitsApiService.updateImplementationStatus(token,GlobalUtil.getCustomerEmailAddress(request),bean);
            if (jsonObject != null) {
                if(jsonObject.has(CustomerHubConstants.STATUS) && jsonObject.get(CustomerHubConstants.STATUS).getAsInt()!=HttpStatus.SC_CREATED){
                    jsonObject = HttpUtil.setJsonResponse(jsonObject, "request error", jsonObject.get(CustomerHubConstants.STATUS).getAsInt());
                    response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                    HttpUtil.writeJsonResponse(response, jsonObject);
                    return;
                }
                HttpUtil.writeJsonResponse(response, jsonObject);
            } else {
                jsonObject = new JsonObject();
                jsonObject = HttpUtil.setJsonResponse(jsonObject, "request error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                HttpUtil.writeJsonResponse(response, jsonObject);
            }
        } else {
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "bad request", HttpStatus.SC_BAD_REQUEST);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            HttpUtil.writeJsonResponse(response, jsonObject);
        }
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return request.getCookie(AUTH_TOKEN).getValue();
    }
}
