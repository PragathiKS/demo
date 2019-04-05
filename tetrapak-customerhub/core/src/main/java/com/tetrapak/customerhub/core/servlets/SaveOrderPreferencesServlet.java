package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * 
 * This servlet is used for saving the user preferences for a logged in user
 * 
 * @author swalamba
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Order Prefernces Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.resourceTypes=" + "customerhub/components/content/orderingcard",
		"sling.servlet.selector=" + "preference", "sling.servlet.extensions=" + "json" })
public class SaveOrderPreferencesServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Reference
	private UserPreferenceService userPreferenceService;

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		LOGGER.info("SaveOrderPreferencesServlet POST method started");
		Session session = request.getResourceResolver().adaptTo(Session.class);
		if (null == session) {
			writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
			return;
		}
		String userId = session.getUserID();
		String prefFromRequest = request.getParameter("fields");
		String[] preferList = {};
		if (null != prefFromRequest) {
			preferList = prefFromRequest.split(",");
		}
		String userPrefType = checkUserPreferenceType(request.getParameter("preferencetype"));
		Gson gson = new Gson();
		Type prefDataType = getPreferenceDataType(userPrefType);
		String json = gson.toJson(preferList, prefDataType);

		if (!userPreferenceService.setPreferences(userId, userPrefType, json)) {
			LOGGER.warn("Could not save UserPreferences To Azure Table for userID: {}", userId);
			writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE);
			return;
		}
		writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_SUCCESS);
	}

	/**
	 * 
	 * If no preference is given then by default it would be saved in order
	 * preferences
	 * 
	 * @param preferenceType
	 * @return
	 */
	private String checkUserPreferenceType(String preferenceType) {
		if (null != preferenceType && !preferenceType.isEmpty()) {
			return preferenceType;
		}
		return CustomerHubConstants.ORDER_PREFERENCES;
	}

	/**
	 * 
	 * Order preferences is a String Array For all other preferences, Map of
	 * key-value pair data is expected
	 * 
	 * @param userPrefType
	 * @return
	 */
	private Type getPreferenceDataType(String userPrefType) {
		Type preferenceDataType = null;
		if (CustomerHubConstants.ORDER_PREFERENCES.equals(userPrefType)) {
			preferenceDataType = new TypeToken<String[]>() {
			}.getType();
		} else {
			preferenceDataType = new TypeToken<Map<String, String>>() {
			}.getType();
		}
		return preferenceDataType;
	}

	/**
	 * write Json Response
	 * 
	 * @param resp   SlingHttpServletResponse
	 * @param status success or failure
	 * @throws IOException
	 */
	private void writeJsonResponse(SlingHttpServletResponse resp, String status) throws IOException {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("status", status);
		GlobalUtil.writeJsonResponse(resp, jsonResponse);
	}
}
