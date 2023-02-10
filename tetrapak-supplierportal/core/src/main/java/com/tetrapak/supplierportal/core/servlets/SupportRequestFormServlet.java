package com.tetrapak.supplierportal.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
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

	public static final String CONTENT_TYPE = "contentType";
	public static final String FILE_NAME = "fileName";
	public static final String STREAM = "stream";

	final String[] onBoardingMaintanance = {"ariba.suppliersupport@tetrapak.com"};
	final String[] sourcingContracting = {"ariba.suppliersupport@tetrapak.com"};
	final String[] catalogues = {"tetrapaksuppliers@ariba.com"};

	@Reference
	private SupportRequestFormEmailService supportRequestFormEmailService;

	private Gson gson = new Gson();

	@Override
	protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		LOGGER.info("into support request form servlet");
		JsonObject jsonObject = new JsonObject();
		try {
			SupportRequestFormBean bean = createRequestAccessBean(request);

			List<Map<String, String>> files = prepareAttachments(request);
			if (bean != null && validateRequest(bean)) {
				String[] emailAddress = getEmailAddress(bean.getPurposeOfContact());
				// send email
				jsonObject = supportRequestFormEmailService.sendEmailForNotification(bean, emailAddress, files);

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

	private String[] getEmailAddress(String purposeOfContact) {
		String[] emails = {"supplier.maintenance@tetrapak.com"};
		if (purposeOfContact.contains("On Boardiing Maintanance")) {
			emails = onBoardingMaintanance;
		} else if (purposeOfContact.contains("Sourcing Contracting")) {
			emails = sourcingContracting;
		} else if (purposeOfContact.contains("Catalogues")) {
			emails = catalogues;
		}
		return emails;
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

	private List<Map<String, String>> prepareAttachments(final SlingHttpServletRequest request) throws IOException {
		List<Map<String, String>> listOfAttachments = new ArrayList<>();
		Map<String, RequestParameter[]> requestParameters = request.getRequestParameterMap();
		for (final Map.Entry<String, RequestParameter[]> entry : requestParameters.entrySet()) {
			RequestParameter[] pArr = entry.getValue();
			for (RequestParameter param : pArr) {
				InputStream stream = param.getInputStream();
				if (!param.isFormField()) {
					Map<String, String> attachment = new HashMap<>();
					LOGGER.debug("File detected with name : {} and content type : {}", param.getFileName(),
							param.getContentType());
					attachment.put(CONTENT_TYPE, param.getContentType());
					attachment.put(FILE_NAME, param.getFileName());
					byte[] bytes = IOUtils.toByteArray(stream);
					attachment.put(STREAM, Base64.getEncoder().encodeToString(bytes));
					listOfAttachments.add(attachment);
				}
			}
		}
		return listOfAttachments;
	}
}
