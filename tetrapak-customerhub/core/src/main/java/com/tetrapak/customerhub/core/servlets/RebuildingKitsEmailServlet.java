package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.RebuildingKitDetailsModel;
import com.tetrapak.customerhub.core.services.RebuildingKitsEmailService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/*
 * This servlet sends email on submission of request for CTI creation
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + RebuildingKitsEmailServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + RebuildingKitsEmailServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + RebuildingKitsEmailServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class RebuildingKitsEmailServlet extends SlingAllMethodsServlet {

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/rebuildingkitdetails";
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";
    public static final String INVALID_JSON_REQUEST_MESSAGE = "Invalid JSON request";
    public static final String SESSION_NULL_MESSAGE = "Session is null";
    private static final long serialVersionUID = -5172343245182589288L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsEmailServlet.class);

    @Reference
    private transient RebuildingKitsEmailService rebuildingKitsEmailService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        JsonObject jsonResponse = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);

        try {
            if (null == session) {
                LOGGER.error("RebuildingKitsEmailServlet exception: " + SESSION_NULL_MESSAGE);
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, SESSION_NULL_MESSAGE, HttpURLConnection.HTTP_BAD_REQUEST);
                response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
                HttpUtil.writeJsonResponse(response, jsonResponse);
                return;
            }
            ResourceBundle resourceBundle = request.getResourceBundle(request.getLocale());
            String requestData = request.getReader().lines().collect(Collectors.joining());
            RebuildingKitDetailsModel rebuildingKitDetailsModel = request.adaptTo(RebuildingKitDetailsModel.class);
            rebuildingKitsEmailService.sendEmail(resourceBundle,requestData, rebuildingKitDetailsModel);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.RESPONSE_STATUS_SUCCESS,
                    HttpURLConnection.HTTP_ACCEPTED);
        } catch (IOException e) {
            LOGGER.error("Error : ", e);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, INVALID_JSON_REQUEST_MESSAGE,
                    HttpURLConnection.HTTP_BAD_REQUEST);
        }
        response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
        HttpUtil.writeJsonResponse(response, jsonResponse);
    }

}
