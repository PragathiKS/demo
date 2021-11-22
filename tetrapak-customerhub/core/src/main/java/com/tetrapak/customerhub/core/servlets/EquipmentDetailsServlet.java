package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
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

/**
 * The Class Equipment Servlet.
 */
@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.selectors=" + "equipment", "sling.servlet.extensions=" + "json",
        "sling.servlet.resourceTypes=" + "customerhub/components/content/equipmentdetails"
})
public class EquipmentDetailsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 8364168871420162838L;

    private static final String AUTH_TOKEN = "authToken";

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDetailsServlet.class);

    @Reference
    private EquipmentDetailsService equipmentDetailsService;

    @Reference
    protected XSSAPI xssAPI;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");
        try {
            Gson gson = new Gson();
            EquipmentUpdateFormBean bean = gson.fromJson(request.getReader(), EquipmentUpdateFormBean.class);

            final String token = getAuthTokenValue(request);

            EquipmentResponse equipmentResponse = equipmentDetailsService.editEquipment(bean, token);
            if (equipmentResponse != null) {
                response.setStatus(equipmentResponse.getStatusCode());
                response.getWriter().write(equipmentResponse.getStatusMessage());
            } else {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("request error");
            }
        } catch (IOException e) {
            LOGGER.error("Error while par" + e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.getWriter().write("json error");
        }
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return xssAPI.encodeForHTML(request.getCookie(AUTH_TOKEN).getValue());
    }
}
