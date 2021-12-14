package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class Equipment Servlet.
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + "addequipment", "sling.servlet.extensions=" + "html",
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

    private Gson gson = new Gson();

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Add Equipment - Post");

        JsonObject jsonObject = null;
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
            jsonObject = addEquipmentService.addEquipment(resolveCustomerName(request), bean, token, prepareAttachments(request));
            if (jsonObject == null) {
                jsonObject = HttpUtil.setJsonResponse(jsonObject, "request error", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            jsonObject = new JsonObject();
            jsonObject = HttpUtil.setJsonResponse(jsonObject, "bad request", HttpStatus.SC_BAD_REQUEST);
        }
        HttpUtil.writeJsonResponse(response, jsonObject);
    }

    private String resolveCustomerName(SlingHttpServletRequest request) {
        Cookie aemCustomerName = request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME);
        if (aemCustomerName != null) {
            return aemCustomerName.getValue();
        }
        return null;
    }

    private List<File> prepareAttachments(final SlingHttpServletRequest request) throws IOException {
        List<File> files = new ArrayList<>();
        Map<String, RequestParameter[]> requestParameters = request.getRequestParameterMap();
        for (final Map.Entry<String, RequestParameter[]> entry : requestParameters.entrySet()) {
            RequestParameter[] pArr = entry.getValue();
            for (RequestParameter param : pArr) {
                InputStream stream = param.getInputStream();
                if (!param.isFormField()) {
                    File f = File.createTempFile("attachment", null);
                    FileUtils.copyInputStreamToFile(stream, f);
                    files.add(f);
                }
            }
        }
        return files;
    }

    private AddEquipmentFormBean createRequestAccessBean(SlingHttpServletRequest request) {
        String jsonObject = gson.toJson(request.getParameterMap());
        jsonObject = xssAPI.getValidJSON(jsonObject, StringUtils.EMPTY);
        return gson.fromJson(jsonObject, AddEquipmentFormBean.class);
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return request.getCookie(AUTH_TOKEN).getValue();
    }
}
