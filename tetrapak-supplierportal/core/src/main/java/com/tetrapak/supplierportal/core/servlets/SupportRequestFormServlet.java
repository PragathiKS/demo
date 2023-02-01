package com.tetrapak.supplierportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.models.SupportRequestFormBean;
import com.tetrapak.supplierportal.core.services.SupportRequestFormEmailService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.extensions=" + "html", "sling.servlet.paths=" + "/bin/supplierportal/supportrequestform" })
public class SupportRequestFormServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -7410933110610280308L;
	private static final String AUTH_TOKEN = "authToken";

	private static final Logger LOGGER = LoggerFactory.getLogger(SupportRequestFormServlet.class);

	@Reference
	private transient XSSAPI xssAPI;

	@Reference
	private SupportRequestFormEmailService SupportRequestFormEmailService;

	private Gson gson = new Gson();

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		LOGGER.info("into support request form servlet");
		JsonObject jsonObject = new JsonObject();
		try {
			SupportRequestFormBean bean = createRequestAccessBean(request);
			if (bean != null && validateRequest(bean)) {
				String emailAddress = "surbhi.gupta1@publicissapient.com";
				jsonObject = SupportRequestFormEmailService.sendEmailForNotification(bean, emailAddress);

			} else {
				LOGGER.error("Support Request Details servlet exception: request is not valid");
				jsonObject = HttpUtil.setJsonResponse(jsonObject, "request is not valid", HttpStatus.SC_BAD_REQUEST);
				HttpUtil.writeJsonResponse(response, jsonObject);

			}

		} catch (Exception e) {
			jsonObject = HttpUtil.setJsonResponse(jsonObject, "invalid json request", HttpStatus.SC_BAD_REQUEST);
		}
		response.setStatus(jsonObject.get(SupplierPortalConstants.STATUS).getAsInt());
		HttpUtil.writeJsonResponse(response, jsonObject);

	}

	private boolean validateRequest(SupportRequestFormBean bean) {
		return !(StringUtils.isEmpty(bean.getPurposeOfContact()) || StringUtils.isEmpty(bean.getHowHelp())
				|| StringUtils.isEmpty(bean.getCompanyLegalName()) || StringUtils.isEmpty(bean.getCountry())
				|| StringUtils.isEmpty(bean.getCity()));
	}

	private SupportRequestFormBean createRequestAccessBean(SlingHttpServletRequest request) {
		String jsonString = gson.toJson(request.getParameterMap());
		jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
		jsonObject.remove("files");
		jsonObject = replaceArraysInJsonObject(jsonObject);
		return gson.fromJson(jsonObject, SupportRequestFormBean.class);
	}

	private JsonObject replaceArraysInJsonObject(JsonObject jsonObject) {
		for (String key : jsonObject.keySet()) {
			jsonObject.addProperty(key, jsonObject.get(key).getAsString());
		}
		return jsonObject;
	}

}
