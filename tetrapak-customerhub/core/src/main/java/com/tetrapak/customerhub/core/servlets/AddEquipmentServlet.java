package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
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
        "sling.servlet.selectors=" + "addequipment", "sling.servlet.extensions=" + "json",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/addequipment"
})
public class AddEquipmentServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -7010933110610280308L;
    private static final String AUTH_TOKEN = "authToken";

    private static final Logger LOGGER = LoggerFactory.getLogger(AddEquipmentServlet.class);

    @Reference
    private transient XSSAPI xssAPI;

    @Reference
    private AddEquipmentService addEquipmentService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Add Equipment - Post");

        JsonObject jsonObject = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            LOGGER.error("Equipment Details servlet exception: session is null");
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "session is null", HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonObject);
            return;
        }

        final String token = getAuthTokenValue(request);
        AddEquipmentFormBean bean = createRequestAccessBean(request);

        if (bean != null && org.apache.commons.lang.StringUtils.isNotEmpty(token)) {
            jsonObject = addEquipmentService.addEquipment(session.getUserID(), bean, token);
            if (jsonObject == null) {
                jsonObject = HttpUtil.setJsonResponse(jsonObject, "request error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "bad request", HttpStatus.SC_BAD_REQUEST);
        }
        HttpUtil.writeJsonResponse(response, jsonObject);
    }

    private AddEquipmentFormBean createRequestAccessBean(SlingHttpServletRequest request) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(request.getParameterMap());
        jsonObject = xssAPI.getValidJSON(jsonObject, StringUtils.EMPTY);
        return gson.fromJson(clearJsonFromArrayBrackets(jsonObject), AddEquipmentFormBean.class);
    }

    private String clearJsonFromArrayBrackets(String jsonObject) {
        return RegExUtils.removeAll(jsonObject, "[\\[\\]]");
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return request.getCookie(AUTH_TOKEN).getValue();
    }
}
