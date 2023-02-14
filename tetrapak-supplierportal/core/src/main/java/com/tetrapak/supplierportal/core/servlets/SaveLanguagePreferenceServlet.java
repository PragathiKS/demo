package com.tetrapak.supplierportal.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.HttpUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;

/**
 * This servlet is used for saving the language preference of the user.
 *
 * @author Nitin Kumar
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Save Language Preference Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/supplierportal/saveLanguagePreference"
        })
public class SaveLanguagePreferenceServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveLanguagePreferenceServlet.class);

    @Reference
    private UserPreferenceService userPreferenceService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Initiating the save language preference process for the user");

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            writeJsonResponse(response, SupplierPortalConstants.RESPONSE_STATUS_FAILURE, "session is null");
            return;
        }

        String userId = session.getUserID();
        String langCode = request.getParameter(GlobalUtil.LANG_CODE);
        langCode = getValidLangCode(langCode);
        if (null != langCode) {
            boolean langPrefStatus = userPreferenceService.setPreferences(userId, SupplierPortalConstants.LANGUGAGE_PREFERENCES, langCode);
            if (langPrefStatus) {
                LOGGER.info("Saving the language preference as {} for the user : {}", langCode, userId);
                writeJsonResponse(response, SupplierPortalConstants.RESPONSE_STATUS_SUCCESS, "language preference is saved as " + langCode);
            } else {
                LOGGER.info("Saving the language preference failed for the user : {}", userId);
                writeJsonResponse(response, SupplierPortalConstants.RESPONSE_STATUS_FAILURE, "failed to save language " + langCode);
            }
        } else {
            LOGGER.info("Saving the language preference failed for the user : {}", userId);
            writeJsonResponse(response, SupplierPortalConstants.RESPONSE_STATUS_FAILURE, "invalid input");
        }
    }

    private String getValidLangCode(String langCode) {
        if (langCode.matches("^[a-z]{2}([_])?([A-Za-z]{2})?$")) {
            return langCode;
        }
        return null;
    }

    /**
     * write JSON Response
     *
     * @param resp    SlingHttpServletResponse
     * @param status  success or failure
     * @param message message
     * @throws IOException IO Exception
     */
    private void writeJsonResponse(SlingHttpServletResponse resp, String status, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);
        jsonResponse.addProperty("message", message);
        HttpUtil.writeJsonResponse(resp, jsonResponse);
    }
}
