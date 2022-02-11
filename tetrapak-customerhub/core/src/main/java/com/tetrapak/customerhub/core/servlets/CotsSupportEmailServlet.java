package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.LanguageManager;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.CotsSupportService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + CotsSupportEmailServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + CotsSupportEmailServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + CotsSupportEmailServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class CotsSupportEmailServlet extends SlingAllMethodsServlet {
    
    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/cotssupport";
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";
    
    private static final long serialVersionUID = -1011318253584927348L;
    
    public static final String CONTENT_TYPE = "contentType";
    public static final String FILE_NAME = "fileName";
    public static final String STREAM = "stream";
    public static final String SUCCESS_MESSAGE = "Success";
    public static final String BAD_REQUEST_MESSAGE = "Bad Request";
    public static final String INVALID_JSON_REQUEST_MESSAGE = "Invalid JSON request";
    public static final String SESSION_NULL_MESSAGE = "Session is null";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportEmailServlet.class);
    
    @Reference
    private CotsSupportService cotsSupportService;
    
    @Reference
    private LanguageManager languageManager;
    
    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Inside CotsSupportEmailServlet servlet : Post method");
        JsonObject jsonResponse = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            LOGGER.error("CotsSupportEmailServlet exception: session is null");
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, SESSION_NULL_MESSAGE, HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonResponse);
            return;
        }
        try {
            CotsSupportFormBean cotsSupportFormBean = cotsSupportService.createCotsSupportFormBean(request);
            if (cotsSupportFormBean != null) {
                List<Map<String, String>> requestFilePayload = extractRequestFilePayload(request);
                cotsSupportService.sendEmail(requestFilePayload, cotsSupportFormBean, request);
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, SUCCESS_MESSAGE, HttpStatus.SC_ACCEPTED);
            } else {
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, BAD_REQUEST_MESSAGE, HttpStatus.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Error : ", e);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, INVALID_JSON_REQUEST_MESSAGE,
                    HttpStatus.SC_BAD_REQUEST);
        }
        response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
        HttpUtil.writeJsonResponse(response, jsonResponse);
    }
    
    /**
     * Create a map of file name to file input stream in String format corresponding to files sent in post request. This
     * map is further used in sending these files as attachment in email.
     * @param request
     * @return
     * @throws IOException
     */
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
                    attachment.put(CONTENT_TYPE, param.getContentType());
                    attachment.put(FILE_NAME, param.getFileName());
                    byte[] bytes = IOUtils.toByteArray(stream);
                    attachment.put(STREAM, Base64.getEncoder().encodeToString(bytes));
                    listOfAttachments.add(attachment);
                }
            }
        }
        return listOfAttachments;
    }
}
