package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

import javax.servlet.Servlet;
import java.io.IOException;
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

    @Reference
    private transient XSSAPI xssAPI;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");

        String requestString = xssAPI.getValidJSON(IOUtils.toString(request.getReader()), StringUtils.EMPTY);
        Gson gson = new Gson();
        AddEquipmentFormBean bean = gson.fromJson(requestString, AddEquipmentFormBean.class);

        bean.getEquipmentComments();

        response.setStatus(HttpStatus.SC_OK);
        Map<String, String> resObj = new HashMap<>();
        resObj.put("message", "request received");
        response.getWriter().write(gson.toJson(resObj));
    }
}
