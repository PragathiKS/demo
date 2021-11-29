package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
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

/**
 * The Class Equipment Servlet.
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + "equipment", "sling.servlet.extensions=" + "json",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/equipmentdetails"
})
public class EquipmentDetailsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 8364168871420162838L;

    private static final String AUTH_TOKEN = "authToken";

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDetailsServlet.class);

    @Reference
    private transient XSSAPI xssAPI;

    @Reference
    private EquipmentDetailsService equipmentDetailsService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");
        Session session = request.getResourceResolver().adaptTo(Session.class);
        JsonObject jsonObject = new JsonObject();
        if (null == session) {
            LOGGER.error("Equipment Details servlet exception: session is null");
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "session is null", HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonObject);
            return;
        }

        String requestString = xssAPI.getValidJSON(IOUtils.toString(request.getReader()), StringUtils.EMPTY);

        Gson gson = new Gson();
        EquipmentUpdateFormBean bean = gson.fromJson(requestString, EquipmentUpdateFormBean.class);
        final String token = getAuthTokenValue(request);

        if (bean != null && bean.isValid() && StringUtils.isNotEmpty(token)) {
            jsonObject = equipmentDetailsService.editEquipment(session.getUserID(), bean, token);
            if (jsonObject != null) {
                HttpUtil.writeJsonResponse(response, jsonObject);
            } else {
                jsonObject = HttpUtil.setJsonResponse(jsonObject, "request error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
                HttpUtil.writeJsonResponse(response, jsonObject);
            }
        } else {
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "bad request", HttpStatus.SC_BAD_REQUEST);
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
