package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.CotsSupportModel;
import com.tetrapak.customerhub.core.services.CotsSupportService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.io.IOUtils;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + CotsSupportEmailServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + CotsSupportEmailServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + CotsSupportEmailServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class CotsSupportEmailServlet extends SlingAllMethodsServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1011318253584927348L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportEmailServlet.class);
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";
    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/cotssupport";
    
    @Reference
    CotsSupportService cotsSupportService;
    
    @Reference
    private transient XSSAPI xssAPI;
    
    private Gson gson = new Gson();
    
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Inside CotsSupportEmailServlet servlet : Post method");
        JsonObject jsonResponse = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            LOGGER.error("Equipment Details servlet exception: session is null");
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "session is null", HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonResponse);
            return;
        }
        try {
            CotsSupportFormBean bean = createCotsSupportFormBean(request);
            if (bean != null) {
                // Map<String, DataSource> requestFilePayload = extractRequestFilePayload(request);
                List<Map<String, String>> requestFilePayload = extractRequestFilePayload(request);
                CotsSupportModel model = request.getResourceResolver()
                        .getResource(request.getParameter("componentPath")).adaptTo(CotsSupportModel.class);
                cotsSupportService.sendEmail(requestFilePayload, model, bean);
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "Success", HttpStatus.SC_ACCEPTED);
            } else {
                LOGGER.debug("Empty bean");
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "bad request", HttpStatus.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("error", e);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, "invalid json request", HttpStatus.SC_BAD_REQUEST);
        }
        response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
        HttpUtil.writeJsonResponse(response, jsonResponse);
        LOGGER.debug("Inside do get method of CotsSupportEmailServlet");
        
    }
    
    private CotsSupportFormBean createCotsSupportFormBean(SlingHttpServletRequest request) {
        LOGGER.debug("Inside createCotsSupportFormBean method");
        String jsonString = gson.toJson(request.getParameterMap());
        jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        if (jsonObject.has("files")) {
            jsonObject.remove("files");
        }
        jsonObject = replaceArraysInJsonObject(jsonObject);
        LOGGER.debug(jsonObject.toString());
        return gson.fromJson(jsonObject, CotsSupportFormBean.class);
    }
    
    private List<Map<String, String>> extractRequestFilePayload(final SlingHttpServletRequest request)
            throws IOException {
        List<Map<String, String>> listOfAttachments = new ArrayList<>();
        Map<String, RequestParameter[]> requestParameters = request.getRequestParameterMap();
        for (final Map.Entry<String, RequestParameter[]> entry : requestParameters.entrySet()) {
            RequestParameter[] pArr = entry.getValue();
            for (RequestParameter param : pArr) {
                InputStream stream = param.getInputStream();
                if (!param.isFormField()) {
                    Map<String, String> attachment = new HashMap<>();
                    LOGGER.debug("file detected");
                    LOGGER.debug(param.getFileName() + "=========" + param.getContentType());
                    final String name = param.getFileName();
                    final String mimeType = param.getContentType();
                    attachment.put("contentType", param.getContentType());
                    attachment.put("fileName", param.getFileName());
                    byte[] bytes = IOUtils.toByteArray(stream);
                    attachment.put("stream", Base64.getEncoder().encodeToString(bytes));
                    listOfAttachments.add(attachment);
                } else {
                    LOGGER.debug(param.getName() + "=========??" + request.getParameter(param.getName()));
                }
            }
        }
        return listOfAttachments;
    }
    
    private JsonObject replaceArraysInJsonObject(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            jsonObject.addProperty(key, jsonObject.get(key).getAsString());
        }
        return jsonObject;
    }
    
}
