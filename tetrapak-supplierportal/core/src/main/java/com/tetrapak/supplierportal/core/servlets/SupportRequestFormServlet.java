package com.tetrapak.supplierportal.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
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
import com.tetrapak.supplierportal.core.models.SupportRequestFormBean;
import com.tetrapak.supplierportal.core.services.SupportRequestFormEmailService;

@Component(service = Servlet.class, property = { "sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.selectors=" + "supportrequest", "sling.servlet.extensions=" + "html",
		"sling.servlet.resourceTypes=" + "supplierportal/components/content/supportrequest" })
public class SupportRequestFormServlet extends SlingAllMethodsServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupportRequestFormServlet.class);

	@Reference
	private transient XSSAPI xssAPI;

	@Reference
	private SupportRequestFormEmailService SupportRequestFormEmailService;

	private Gson gson = new Gson();

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		try {
			SupportRequestFormBean bean = createRequestAccessBean(request);
			if (bean != null && validateRequest(bean)) {
				String emailAddress = "surbhi.gupta@publicissapient.com";
				SupportRequestFormEmailService.sendEmailForNotification(bean, emailAddress);

			} else {

			}

		} catch (Exception e) {

		}

	}

	private boolean validateRequest(SupportRequestFormBean bean) {
		return !(StringUtils.isEmpty(bean.getPurposeOfContact()) || StringUtils.isEmpty(bean.getHowHelp())
				|| StringUtils.isEmpty(bean.getName()) || StringUtils.isEmpty(bean.getEmailAddress())
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
