package com.tetrapak.supplierportal.core.servlets;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHTOKEN;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.DOCUMENT_REFERENCE_ID;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ERROR_MESSAGE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

/**
 * Payment Invoice Export To PDF Generator Servlet
 *
 * @author sunmanak(Sunil Kumar Yadav)
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Payment Invoice Export To PDF Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/supportal/invoice/export",
		"sling.servlet.extensions=" + SupplierPortalConstants.PDF })
public class PaymentInvoiceExportServlet extends SlingSafeMethodsServlet {

	@Reference
	private PaymentInvoiceDownloadService service;

	/** The XSSAPI */
	@Reference
	private transient XSSAPI xssAPI;

	private static final long serialVersionUID = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInvoiceExportServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOGGER.debug("HTTP GET request from Payment Invoice Export Servlet");
		String documentReferenceId = request.getParameter(DOCUMENT_REFERENCE_ID);
		if (Objects.isNull(documentReferenceId)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.getWriter().write("Document Reference Id Is Missing.");
		} else {
			documentReferenceId = xssAPI.encodeForHTML(documentReferenceId);
		}

		String authTokenStr = getAuthToken(request, response);
		if(StringUtils.isBlank(authTokenStr)) {
			return ;
		}
		
		HttpResponse httpResponse = null;
		try {
			httpResponse = service.preparePdf(authTokenStr, documentReferenceId);

			if (null == httpResponse) {
				JsonObject jsonResponse = new JsonObject();
				jsonResponse.addProperty(STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				jsonResponse.addProperty(ERROR_MESSAGE,
						"Http response is null While Preparing Payment Invoice Details PDF");
				HttpUtil.writeJsonResponse(response, jsonResponse);
				return;
			}
			HeaderIterator headerItr = httpResponse.headerIterator();
			while (headerItr.hasNext()) {
				Header apiRespHeader = headerItr.nextHeader();
				response.setHeader(apiRespHeader.getName(), apiRespHeader.getValue());
			}
			httpResponse.getEntity().writeTo(response.getOutputStream());

		} catch (IOException | URISyntaxException ex) {
			LOGGER.error("Exception In PaymentInvoiceExportServlet {}", ex);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty(ERROR_MESSAGE, "Exception In PaymentInvoiceExportServlet " + ex.getMessage());
			HttpUtil.writeJsonResponse(response, jsonResponse);
		}
	}

	private String getAuthToken(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		Cookie authToken = request.getCookie(AUTHTOKEN);
		if (Objects.nonNull(authToken)) {
			return xssAPI.encodeForHTML(authToken.getValue());
		} else {
			LOGGER.error("Auth token is invalid! {}");
			response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty(ERROR_MESSAGE, "Auth token is invalid!" + request.getRequestPathInfo());
			HttpUtil.writeJsonResponse(response, jsonResponse);
			return null;
		}
	}
}
