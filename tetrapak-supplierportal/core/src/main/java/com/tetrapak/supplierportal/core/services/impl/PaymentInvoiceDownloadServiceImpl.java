package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ACCEPT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_JSON;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_URL_ENCODED;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHORIZATION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.INVOICE_MAPPING;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.BEARER;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_TYPE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_FAILURE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_SUCCESS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.tetrapak.supplierportal.core.bean.PaymentDetailResponse;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;
import com.tetrapak.supplierportal.core.services.UrlService;
import com.tetrapak.supplierportal.core.utils.FontUtil;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.HttpUtil;
import com.tetrapak.supplierportal.core.utils.PDFUtil;

/**
 * Impl class for Payment Invoice Download Service
 * 
 * @author Sunil Kumar Yadav
 */
@Component(immediate = true, service = PaymentInvoiceDownloadService.class)
public class PaymentInvoiceDownloadServiceImpl implements PaymentInvoiceDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInvoiceDownloadServiceImpl.class);

	@Reference
	private APIGEEService apiGeeService;

	@Reference
	private UrlService urlService;

	private Font languageSpecificFont;
	private Font languageSpecificFontBold;
	private PaymentDetailsModel paymentDetailsModel;

	private HttpGet createRequest(final String apiURL, final String authTokenStr, final String documentReferenceId)
			throws UnsupportedEncodingException, URISyntaxException {
		HttpGet getRequest = new HttpGet(apiURL);
		getRequest.addHeader(AUTHORIZATION, BEARER + authTokenStr);
		getRequest.addHeader(CONTENT_TYPE, APPLICATION_URL_ENCODED);
		getRequest.addHeader(ACCEPT, APPLICATION_JSON);
		URI uri = new URIBuilder(getRequest.getURI()).addParameter("fromdatetime", "2023-07-01T00:00:00")
				.addParameter("todatetime", "2023-07-30T00:00:00")
				.addParameter("documentreferenceid", documentReferenceId).build();
		((HttpRequestBase) getRequest).setURI(uri);
		return getRequest;
	}

	@Override
	public JsonObject retrievePaymentDetails(String authTokenStr, String documentRef) throws IOException {
		LOGGER.debug("PaymentInvoiceDownloadServiceImpl#preparePdf-- Start");
		JsonObject jsonResponse = new JsonObject();
		final String apiURL = apiGeeService.getApigeeServiceUrl()
				+ GlobalUtil.getSelectedApiMapping(apiGeeService, INVOICE_MAPPING);
		HttpClient httpClient = HttpClientBuilder.create().build();
		int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		try {
			HttpGet getRequest = createRequest(apiURL, authTokenStr, documentRef);
			HttpResponse httpResponse = httpClient.execute(getRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			jsonResponse.addProperty(STATUS_CODE, statusCode);
			LOGGER.debug("Http Post request status code: {}", statusCode);
			jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);
			jsonResponse.addProperty(STATUS, RESPONSE_STATUS_SUCCESS);
			LOGGER.debug("APIGEE Call is Success");
		} catch (IOException | URISyntaxException e) {
			LOGGER.error("Unable to connect to the Payment Details Call - url {}", apiURL, e);
			jsonResponse.addProperty(STATUS_CODE, statusCode);
			jsonResponse.addProperty(STATUS, RESPONSE_STATUS_FAILURE);
			jsonResponse.addProperty(RESULT, e.getMessage());
		}
		return jsonResponse;
	}

	@Override
	public boolean preparePdf(PaymentDetailResponse paymentDetails, SlingHttpServletRequest request,
			SlingHttpServletResponse response, PaymentDetailsModel paymentDetailsModel){
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(new RectangleReadOnly(620.0F, 842.0F));
		this.paymentDetailsModel = paymentDetailsModel;
		String language = GlobalUtil.getLanguage(request);
		String fileName = paymentDetails.getData().get(0).getCompanyCode() + "-" + LocalDate.now();
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			Paragraph p = new Paragraph("HIiiiiiiiiiiiiii" );
	        document.add(p);
			//setLanguageSpecificFont(language);
	        
	        printHeadLines(request, document, language, paymentDetails);

			//PDFUtil.drawImage(document, urlService.getImagesUrl() + "tetra_pdf.png", 180, 69);
			//document.add(new Paragraph("\n", "NotoSerif-Light.ttf"));
			document.close();
			return true;
		} catch (Throwable e) {
			LOGGER.error("IOException ", e);
			return false;
		}
	}
	
	 private void printHeadLines(SlingHttpServletRequest request, com.itextpdf.text.Document document,String language,PaymentDetailResponse paymentDetails)
	            throws DocumentException {
	        languageSpecificFont.setSize(13);
	        languageSpecificFontBold.setSize(13);
	        Paragraph p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
	                request, StringUtils.EMPTY, paymentDetailsModel.getInvoiceNo(), language));
	        document.add(p);

	        p = new Paragraph("\n" + GlobalUtil.getI18nValueForThisLanguage(
	                request, StringUtils.EMPTY, paymentDetailsModel.getSupplierInfo(), language));
	        document.add(p);

	      
	        document.add(p);

	       
	        document.add(p);
	    }

	private void setLanguageSpecificFont(String language) throws IOException, DocumentException {
		final String FONT_RESOURCES = urlService.getFontsUrl();
		switch (language) {
		case "zh":
			languageSpecificFont = FontUtil.getScFont(FONT_RESOURCES + "NotoSerifCJKsc-Light.otf");
			languageSpecificFontBold = FontUtil.getScFontBold(FONT_RESOURCES + "NotoSerifCJKsc-Bold.otf");
			break;
		default:
			languageSpecificFont = FontUtil.getEnFont(FONT_RESOURCES + "NotoSerif-Light.ttf");
			languageSpecificFontBold = FontUtil.getEnFontBold(FONT_RESOURCES + "NotoSerif-Bold.ttf");
			break;
		}
		languageSpecificFont.setColor(BaseColor.DARK_GRAY);
		languageSpecificFontBold.setColor(BaseColor.DARK_GRAY);
	}

}
