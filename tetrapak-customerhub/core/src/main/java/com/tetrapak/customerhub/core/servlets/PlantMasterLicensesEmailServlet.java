package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.commons.httpclient.HttpStatus;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/*
 * This servlet sends email on submission of request for licenses
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class PlantMasterLicensesEmailServlet extends SlingAllMethodsServlet {

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/plantmasterlicenses";
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";
    public static final String INVALID_JSON_REQUEST_MESSAGE = "Invalid JSON request";
    public static final String SESSION_NULL_MESSAGE = "Session is null";
    private static final long serialVersionUID = -5173298545182589288L;
    private static final String LICENSE_TYPE_REQUEST_PARAMETER = "licenseType";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesEmailServlet.class);

    @Reference
    private transient PlantMasterLicensesService plantMasterLicensesService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        JsonObject jsonResponse = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);

        try {
            if (null == session) {
                LOGGER.error("PlantMasterLicensesEmailServlet exception: " + SESSION_NULL_MESSAGE);
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, SESSION_NULL_MESSAGE, HttpStatus.SC_BAD_REQUEST);
                HttpUtil.writeJsonResponse(response, jsonResponse);
                return;
            }
            ResourceBundle resourceBundle = request.getResourceBundle(request.getLocale());
            String requestData = request.getReader().lines().collect(Collectors.joining());
            PlantMasterLicensesModel masterLicensesModel = request.adaptTo(PlantMasterLicensesModel.class);
            plantMasterLicensesService.sendEmail(resourceBundle, request.getHeader(LICENSE_TYPE_REQUEST_PARAMETER),
                    requestData, masterLicensesModel);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.RESPONSE_STATUS_SUCCESS,
                    HttpStatus.SC_ACCEPTED);
        } catch (IOException e) {
            LOGGER.error("Error : ", e);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, INVALID_JSON_REQUEST_MESSAGE,
                    HttpStatus.SC_BAD_REQUEST);
        }
        response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
        HttpUtil.writeJsonResponse(response, jsonResponse);
    }

}
