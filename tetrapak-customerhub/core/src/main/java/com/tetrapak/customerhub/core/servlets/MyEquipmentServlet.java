package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.MyEquipmentApiService;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * @author ojaswarn
 * The Class MyEquipmentServlet.
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Country List servlet to get list of countries",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/customerhub/myequipment" })
public class MyEquipmentServlet extends SlingSafeMethodsServlet {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MyEquipmentServlet.class);

	/** The country list service. */
	@Reference
	private MyEquipmentApiService countryListService;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1130383045571569764L;

	/** The Constant AUTH_TOKEN. */
	private static final String AUTH_TOKEN = "authToken";

	/**
	 * Do get.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		if (request.getParameterMap().containsKey("callfrom")) {
			String callFrom = request.getParameter("callfrom");

			LOGGER.debug("Inside doGet method of MyEquipmentServlet");
			final String token = request.getCookie(AUTH_TOKEN) == null ? StringUtils.EMPTY
					: request.getCookie(AUTH_TOKEN).getValue();
			LOGGER.debug("Got authToken from cookie : {}", token);

			JsonObject jsonResponse = new JsonObject();
			if (StringUtils.equalsIgnoreCase(callFrom, "countrylist")) {
				jsonResponse = countryListService.getCountryList(token);
			} else if (StringUtils.equalsIgnoreCase(callFrom, "equipmentlist")) {
				jsonResponse = countryListService.getListOfEquipments(token,
						request.getParameter("countrycode"), request.getParameter("count"));
			}

			JsonElement status = jsonResponse.get(CustomerHubConstants.STATUS);
			if (!CustomerHubConstants.RESPONSE_STATUS_OK.equalsIgnoreCase(status.toString())) {
				LOGGER.error("Unable to retrieve response from API, got status code: {}", status);
			} else if (!jsonResponse.isJsonNull()) {
				HttpUtil.writeJsonResponse(response, jsonResponse);
			}
		}
	}
}
