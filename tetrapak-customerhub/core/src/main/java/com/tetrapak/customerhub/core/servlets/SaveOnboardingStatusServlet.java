package com.tetrapak.customerhub.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * This servlet is used for saving the on-boarding status of the user. 
 * 
 * @author tustusha
 *
 */
@Component(	service = Servlet.class,
			property = {
						Constants.SERVICE_DESCRIPTION + "=Onboarding Status Servlet",
						SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
						SLING_SERVLET_RESOURCE_TYPES + "=customerhub/components/content/introscreen"
						})
public class SaveOnboardingStatusServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4140890702261737392L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SaveOnboardingStatusServlet.class);
	
	private static final String USER_ONBOARDED = "onBoardingStatus";

	@Reference
	private UserPreferenceService userPreferenceService;

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException, ServletException {
		LOGGER.debug("Initiating the on-boarding status for the user");
		Session session = request.getResourceResolver().adaptTo(Session.class);
		if (null == session) {
			writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_FAILURE, null);
			return;
		}
		String userId = session.getUserID();
		String userBoardingStatus = StringUtils.EMPTY;
		userBoardingStatus = userPreferenceService.getSavedPreferences(userId, USER_ONBOARDED);
		if (StringUtils.isNotBlank(userBoardingStatus)) {
			LOGGER.info("Saving the on-boarding status for the userID: {}", userId);
			writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_SUCCESS, userBoardingStatus);
		} else {
			LOGGER.debug("The user is already on-boarded");
			userPreferenceService.setPreferences(userId, USER_ONBOARDED, "true");
			writeJsonResponse(response, CustomerHubConstants.RESPONSE_STATUS_SUCCESS, userBoardingStatus);
		}
	}


	/**
	 * write JSON Response
	 * 
	 * @param resp   SlingHttpServletResponse
	 * @param status success or failure
	 * @param onBoardingStatus true/null
	 * @throws IOException
	 */
	private void writeJsonResponse(SlingHttpServletResponse resp, String status, String onBoardingStatus) throws IOException {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("status", status);
		if (StringUtils.isNotBlank(onBoardingStatus)) {
			jsonResponse.addProperty("isOnboarded", Boolean.TRUE);
		} else {
			jsonResponse.addProperty("isOnboarded", Boolean.FALSE);
		}
		HttpUtil.writeJsonResponse(resp, jsonResponse);
	}
}
