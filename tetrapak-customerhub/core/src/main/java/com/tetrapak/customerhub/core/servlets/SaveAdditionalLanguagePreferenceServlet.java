package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
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
 * This servlet is used for saving the additional language preference of the user.
 *
 * @author Aalekh Mathur
 */
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Save Additional Language Preference Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.resourceTypes=" + CustomerHubConstants.REBUILDING_KITS_DETAILS_RESOURCE_TYPE,
                "sling.servlet.selectors=" + CustomerHubConstants.LANGUGAGE_PREFERENCES, "sling.servlet.extensions=" 
                + CustomerHubConstants.JSON_SERVLET_EXTENSION
        })
public class SaveAdditionalLanguagePreferenceServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 7010372193905564525L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveAdditionalLanguagePreferenceServlet.class);

    @Reference
    private UserPreferenceService userPreferenceService;

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        LOGGER.debug("Initiating the save language preference process for the user");

        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null == session) {
            writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE, "session is null");
            return;
        }

        String userId = session.getUserID();
        String langCode = request.getParameter(CustomerHubConstants.LANG_CODE);
        if (langCode.matches(CustomerHubConstants.LANG_CODE_REGULAR_EXP)) {
            boolean langPrefStatus = userPreferenceService.setPreferences(userId, CustomerHubConstants.ADDITIONAL_LANGUAGE_PREFERENCES, langCode);
            if (langPrefStatus) {
                LOGGER.info("Saving the language preference as {}", langCode);
                writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_SUCCESS, "language preference is saved as " + langCode);
            } else {
                LOGGER.info("Saving the language preference failed");
                writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE, "failed to save language " + langCode);
            }
        } else {
            LOGGER.info("Saving the language preference failed");
            writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE, "invalid input");
        }
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