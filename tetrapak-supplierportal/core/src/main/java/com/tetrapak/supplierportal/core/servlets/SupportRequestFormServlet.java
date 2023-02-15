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
import org.apache.sling.api.servlets.ServletResolverConstants;
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
import com.tetrapak.supplierportal.core.services.SupportRequestPurposesEmailsService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

@Component(service = Servlet.class, property = {
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
        ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=" + "html",
        ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "supplierportal/components/content/supportrequest"
})
public class SupportRequestFormServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -7410933110610280308L;
	private static final String AUTH_TOKEN = "authToken";

	private static final Logger LOGGER = LoggerFactory.getLogger(SupportRequestFormServlet.class);

    private static final String ON_BOARDING_PURPOSE = "On Boardiing Maintanance";
    private static final String CONTRACTING_PURPOSE = "Sourcing Contracting";
    private static final String CATALOGUES_PURPOSE = "Catalogues";
	private static final String FILES_KEY = "files";

	@Reference
	private transient XSSAPI xssAPI;

	public static final String CONTENT_TYPE = "contentType";
	public static final String FILE_NAME = "fileName";
	public static final String STREAM = "stream";

	@Reference
	private SupportRequestFormEmailService supportRequestFormEmailService;

	@Reference
	private SupportRequestPurposesEmailsService supportRequestPurposesEmailsService;

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
		String[] emails = supportRequestPurposesEmailsService.getOtherEmail();
		if (purposeOfContact.contains(ON_BOARDING_PURPOSE)) {
			emails = supportRequestPurposesEmailsService.getOnBoardingMaintananceEmail();
		} else if (purposeOfContact.contains(CONTRACTING_PURPOSE)) {
			emails = supportRequestPurposesEmailsService.getSourcingContractingEmail();
		} else if (purposeOfContact.contains(CATALOGUES_PURPOSE)) {
			emails = supportRequestPurposesEmailsService.getCataloguesEmail();
		}
		return emails;
	}

	private boolean validateRequest(SupportRequestFormBean bean) {
		return !(StringUtils.isEmpty(bean.getName()) || StringUtils.isEmpty(bean.getEmailAddress()) || StringUtils.isEmpty(bean.getPurposeOfContact()) || StringUtils.isEmpty(bean.getHowHelp())
				|| StringUtils.isEmpty(bean.getCompanyLegalName()) || StringUtils.isEmpty(bean.getCountry())
				|| StringUtils.isEmpty(bean.getCity()));
	}

	private SupportRequestFormBean createRequestAccessBean(SlingHttpServletRequest request) {
		String jsonString = gson.toJson(request.getParameterMap());
		jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
		jsonObject.remove(FILES_KEY);
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
