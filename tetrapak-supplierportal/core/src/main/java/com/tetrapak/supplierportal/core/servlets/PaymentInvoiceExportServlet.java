package com.tetrapak.supplierportal.core.servlets;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHTOKEN;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.DOCUMENT_REFERENCE_ID;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.FROM_DATE_TIME;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_OK;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.TO_DATE_TIME;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Servlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.xss.XSSAPI;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
		Constants.SERVICE_DESCRIPTION + "=Export to PDF Generator Servlet",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST,
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.resourceTypes=" + "supplierportal/components/content/paymentdetails",
        "sling.servlet.selectors=" + SupplierPortalConstants.DOWNLOAD_LINK, 
        "sling.servlet.extensions=" + SupplierPortalConstants.PDF})
public class PaymentInvoiceExportServlet extends SlingAllMethodsServlet {

	@Reference
	private PaymentInvoiceDownloadService paymentInvoiceDownloadSevice;

	/** The XSSAPI */
	@Reference
	private transient XSSAPI xssAPI;

	private static final long serialVersionUID = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInvoiceExportServlet.class);
	
	private final Gson gson = new Gson();
	
	@Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        doPost(request, response);
    }


	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOGGER.debug("HTTP GET request from Payment Invoice Export Servlet");
		JsonObject jsonObj = new JsonObject();
     	String documentReferenceId = request.getParameter(DOCUMENT_REFERENCE_ID);
     	String fromDateTime =  request.getParameter(FROM_DATE_TIME);
     	String toDateTime =  request.getParameter(TO_DATE_TIME);
     	String status =  request.getParameter(STATUS);
		if (StringUtils.isBlank(documentReferenceId)|| 
				StringUtils.isBlank(fromDateTime) ||
				StringUtils.isBlank(toDateTime) ||
				StringUtils.isBlank(status)) {
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.getWriter().write("Invalid Input. DocumentReferenceId OR FromDate OR ToDate OR Status is Missing.");
			return;
		} else {
			documentReferenceId = xssAPI.encodeForHTML(documentReferenceId);
			fromDateTime = xssAPI.encodeForHTML(fromDateTime);
			toDateTime = xssAPI.encodeForHTML(toDateTime);
			status = xssAPI.encodeForHTML(status);
		}

		String authTokenStr;
		Cookie authToken = request.getCookie(AUTHTOKEN);
		if (Objects.nonNull(authToken)) {
			authTokenStr = xssAPI.encodeForHTML(authToken.getValue());
		} else {
			LOGGER.error("Auth token is invalid! {}");
			response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
			response.getWriter().write("Invalid Auth Token. Authentication Token Missing.");
			HttpUtil.setJsonResponse(jsonObj, "Auth token is invalid!" + request.getRequestPathInfo(), 1001);
			HttpUtil.sendErrorMessage(response);
			return;
		}

		JsonObject jsonResponse = paymentInvoiceDownloadSevice.retrievePaymentDetails(authTokenStr,documentReferenceId,
				fromDateTime, toDateTime, status);
		JsonElement statusResponse = jsonResponse.get(STATUS_CODE);

        boolean flag = false;
        PaymentDetailsModel paymentDetailsModel = request.getResource().adaptTo(PaymentDetailsModel.class);

        if (null == paymentDetailsModel) {
            LOGGER.error("PaymentDetailsModel is null!");
            HttpUtil.setJsonResponse(jsonObj, "PaymentDetailsModel is null!", 1002);
        }else if (!RESPONSE_STATUS_OK.equalsIgnoreCase(statusResponse.toString())) {
            LOGGER.error("Unable to retrieve response from API got status code:{}", statusResponse.toString());
            HttpUtil.setJsonResponse(jsonObj, "Unable to retrieve response from API got status code:"+ statusResponse.toString(), 1003);
        } else {
        	JsonElement resultsResponse = jsonResponse.get(RESULT);
        	PaymentDetailResponse results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), PaymentDetailResponse.class);
        	if(Objects.nonNull(results) && CollectionUtils.isNotEmpty(results.getData())) {
        		flag = paymentInvoiceDownloadSevice.preparePdf(results.getData().get(0), request, response, paymentDetailsModel);
        	}else {
        		LOGGER.error("Invoice Details are missing from Backend Services..!::{}",resultsResponse);
        		HttpUtil.setJsonResponse(jsonObj, "Invoice Details are missing from Backend Services..!::"+ resultsResponse, 1003);
        	}
        }        
        if (!flag) {
            LOGGER.error("PaymentDetails results Pdf file download failed!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            HttpUtil.sendErrorMessage(response);
        }
	}

	
}
