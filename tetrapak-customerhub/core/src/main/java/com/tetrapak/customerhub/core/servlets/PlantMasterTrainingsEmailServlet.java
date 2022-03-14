package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.util.Objects;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.PlantMasterTrainingsFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PlantMasterTrainingsService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

@Component(
        service = Servlet.class,
        property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.selectors=" + PlantMasterTrainingsEmailServlet.SLING_SERVLET_SELECTOR,
                "sling.servlet.extensions=" + PlantMasterTrainingsEmailServlet.SLING_SERVLET_EXTENSION,
                "sling.servlet.resourceTypes=" + PlantMasterTrainingsEmailServlet.SLING_SERVLET_RESOURCE_TYPES })
public class PlantMasterTrainingsEmailServlet extends SlingAllMethodsServlet {

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/plantmastertrainings";
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";

    private static final long serialVersionUID = -1011318253584927348L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterTrainingsEmailServlet.class);

    @Reference
    private PlantMasterTrainingsService plantMasterTrainingsService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Inside PlantMasterTrainingsEmailServlet servlet : Post method");
        JsonObject jsonResponse = new JsonObject();
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (Objects.isNull(session)) {
            LOGGER.error("PlantMasterTrainingsEmailServlet exception: Session is null");
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.SESSION_NULL_MESSAGE,
                    HttpStatus.SC_BAD_REQUEST);
            HttpUtil.writeJsonResponse(response, jsonResponse);
            return;
        }
        try {
            PlantMasterTrainingsFormBean plantMasterTrainingsFormBean = plantMasterTrainingsService
                    .createPlantMasterTrainingsFormBean(request);
            if (Objects.nonNull(plantMasterTrainingsFormBean)) {
                plantMasterTrainingsService.sendEmail(plantMasterTrainingsFormBean, request);
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.SUCCESS_MESSAGE,
                        HttpStatus.SC_ACCEPTED);
            } else {
                jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.BAD_REQUEST_MESSAGE,
                        HttpStatus.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Error : ", e);
            jsonResponse = HttpUtil.setJsonResponse(jsonResponse, CustomerHubConstants.INVALID_JSON_REQUEST_MESSAGE,
                    HttpStatus.SC_BAD_REQUEST);
        }
        response.setStatus(jsonResponse.get(CustomerHubConstants.STATUS).getAsInt());
        HttpUtil.writeJsonResponse(response, jsonResponse);
    }
}
