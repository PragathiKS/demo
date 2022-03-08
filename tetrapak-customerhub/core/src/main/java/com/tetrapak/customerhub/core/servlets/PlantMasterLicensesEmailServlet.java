package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;


@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_SELECTOR,
        "sling.servlet.extensions=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_EXTENSION,
        "sling.servlet.resourceTypes=" + PlantMasterLicensesEmailServlet.SLING_SERVLET_RESOURCE_TYPES
})
public class PlantMasterLicensesEmailServlet extends SlingAllMethodsServlet {

    public static final String SLING_SERVLET_RESOURCE_TYPES = "customerhub/components/content/plantmasterlicenses";
    public static final String SLING_SERVLET_EXTENSION = "html";
    public static final String SLING_SERVLET_SELECTOR = "email";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesEmailServlet.class);

    @Reference
    PlantMasterLicensesService plantMasterLicensesService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response){

        PlantMasterLicensesModel model = request.adaptTo(PlantMasterLicensesModel.class);
        LOGGER.debug("iS MODEL NULL "+String.valueOf(model==null));
        LOGGER.debug("name : "+model.getEngineeringLicenseModel().getName());

        plantMasterLicensesService.sendEmail(request);

    }

}