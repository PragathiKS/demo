package com.tetrapak.supplierportal.core.servlets;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHTOKEN;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.DOCUMENT_REFERENCE_ID;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ERROR_MESSAGE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_OK;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import com.tetrapak.supplierportal.core.bean.PaymentDetailResponse;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

/**
 * Payment Invoice Export To PDF Generator Servlet
 *
 * @author sunmanak(Sunil Kumar Yadav)
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Payment Invoice Export To PDF Generator Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/supportal/invoice/export",
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

		String authTokenStr;
		Cookie authToken = request.getCookie(AUTHTOKEN);
		if (Objects.nonNull(authToken)) {
			authTokenStr = xssAPI.encodeForHTML(authToken.getValue());
		} else {
			LOGGER.error("Auth token is invalid! {}");
			response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty(ERROR_MESSAGE, "Auth token is invalid!" + request.getRequestPathInfo());
			HttpUtil.writeJsonResponse(response, jsonResponse);
			return;
		}

		JsonObject jsonResponse = service.retrievePaymentDetails(authTokenStr, documentReferenceId);
		JsonElement statusResponse = jsonResponse.get(STATUS_CODE);

        boolean flag = false;
        PaymentDetailsModel paymentDetailsModel = request.getResource().adaptTo(PaymentDetailsModel.class);

        if (null == paymentDetailsModel) {
            LOGGER.error("FinancialStatementModel is null!");
        }else if (!RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse.toString());
        } else {
        	GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
        	JsonElement resultsResponse = jsonResponse.get(RESULT);
        	PaymentDetailResponse results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), PaymentDetailResponse.class);
        	flag = service.preparePdf(results, request, response, paymentDetailsModel);
        }
        
        if (!flag) {
            LOGGER.error("Financial results file download failed!");
            HttpUtil.sendErrorMessage(response);
        }
	}

	
}
