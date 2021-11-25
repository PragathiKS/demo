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

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Start: Equipment details - Post");

        Gson gson = new Gson();

        EquipmentUpdateFormBean bean = gson.fromJson(request.getReader(), EquipmentUpdateFormBean.class);
        final String token = getAuthTokenValue(request);

        if (bean != null && StringUtils.isNotEmpty(token)) {
            EquipmentResponse equipmentResponse = equipmentDetailsService.editEquipment(bean, token);
            if (equipmentResponse != null) {
                response.setStatus(equipmentResponse.getStatus());
                response.getWriter().write(equipmentResponse.getStatus());
            } else {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("request error");
            }
        } else {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            response.getWriter().write("bad request");
        }
    }

    private String getAuthTokenValue(SlingHttpServletRequest request) {
        if (null == request.getCookie(AUTH_TOKEN)) {
            return StringUtils.EMPTY;
        }
        return request.getCookie(AUTH_TOKEN).getValue();
    }
}
