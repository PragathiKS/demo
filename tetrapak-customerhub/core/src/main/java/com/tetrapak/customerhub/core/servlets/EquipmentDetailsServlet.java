package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

/**
 * The Class Equipment Servlet.
 */
@Component(service = Servlet.class, property = {
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + "equipment",
        "sling.servlet.extensions=" + "json",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/equipmentdetails"
})
public class EquipmentDetailsServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDetailsServlet.class);

    @Reference
    private EquipmentDetailsService equipmentDetailsService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");
        HttpStatus status = equipmentDetailsService.addEquipment(request);
        response.setStatus(HttpStatus.SC_OK);
        response.getWriter().write("200");
    }

}
