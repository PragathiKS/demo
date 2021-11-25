package com.tetrapak.customerhub.core.servlets;

import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import java.io.IOException;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class Equipment Servlet.
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + "addequipment", "sling.servlet.extensions=" + "json",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/addequipment"
}) public class AddEquipmentServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -7010933110610280308L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AddEquipmentServlet.class);

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");
        response.setStatus(HttpStatus.SC_OK);

        Map<String, String> resObj = new HashMap<>();
        resObj.put("message", "request received");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(resObj));
    }
}
