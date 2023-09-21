package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ACCEPT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_JSON;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_URL_ENCODED;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHORIZATION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.BEARER;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_TYPE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.INVOICE_MAPPING;
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
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tetrapak.supplierportal.core.bean.PaymentDetails;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.PaymentInvoiceDownloadService;
import com.tetrapak.supplierportal.core.services.UrlService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.HttpUtil;

/**
 * Impl class for Payment Invoice Download Service
 * 
 * @author Sunil Kumar Yadav
 */
@Component(immediate = true, service = PaymentInvoiceDownloadService.class)
public class PaymentInvoiceDownloadServiceImpl implements PaymentInvoiceDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentInvoiceDownloadServiceImpl.class);
	private static final String NEW_LINE = "\n";

	@Reference
	private APIGEEService apiGeeService;

	@Reference
	private UrlService urlService;

	private Font titleFont = new Font();
	private Font keyFont = new Font();
	private Font valueFont = new Font();
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

	
	@SuppressWarnings("unused")
	@Override
	public boolean preparePdf(PaymentDetails paymentDetails, SlingHttpServletRequest request,
			SlingHttpServletResponse response, PaymentDetailsModel paymentDetailsModel) {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(new RectangleReadOnly(620.0F, 842.0F));
		this.paymentDetailsModel = paymentDetailsModel;
		String language = GlobalUtil.getLanguage(request);
		setUpFonts();
		String fileName = paymentDetails.getCompanyCode() + "-" + LocalDate.now();
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".pdf");
		try {
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			String imageFile = "/content/dam/tetrapak/tetra-pak-two-liner.png";
			Image image = Image.getInstance(imageFile);
			image.setAbsolutePosition((PageSize.A4.getWidth() - image.getScaledWidth()) / 2,
					(PageSize.A4.getHeight() - image.getScaledHeight()) / 2);
		    document.add(image);

			PdfPTable table = new PdfPTable(2);
			float[] columnWidths = new float[] {35f, 65f};
			table.setWidths(columnWidths);
			table.setWidthPercentage(80);
			table.setHorizontalAlignment(50);
			
			populateInvoiceInformation(table, request, language);
			populateCompanyInformation(table, request, language);
			populateSupplierInformation(table, request, language);
			populateDetailsSection(table, request, language);

			document.close();
			return true;
		} catch (Throwable e) {
			LOGGER.error("IOException ", e);
			return false;
		}
	}
	
	private void setUpFonts() {
		titleFont.setStyle(Font.BOLD);
		titleFont.setSize(15);
		keyFont.setSize(12);
		valueFont.setStyle(Font.BOLD);
		valueFont.setSize(12);
	}

	private void populateInvoiceInformation(PdfPTable table,SlingHttpServletRequest request,String language) {
		String invoiceInfo = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getInvoiceInfo(), language);
		PdfPCell invoiceHead = new PdfPCell(new Phrase(invoiceInfo, titleFont));
		invoiceHead.setBorder(Rectangle.NO_BORDER);
		invoiceHead.setColspan(2);
		table.addCell(invoiceHead);
		
		PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String invoiceNo = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getInvoiceNo(), language);
		PdfPCell invoiceNoK = new PdfPCell(new Phrase(invoiceNo, keyFont));
		invoiceNoK.setBorder(Rectangle.NO_BORDER);
		table.addCell(invoiceNoK);
		PdfPCell invoiceNoV = new PdfPCell(new Phrase("1234567/1", valueFont));
		invoiceNoV.setBorder(Rectangle.NO_BORDER);
		table.addCell(invoiceNoV);
		
		String date = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getDate(), language);
		PdfPCell dateKey = new PdfPCell(new Phrase(date, keyFont));
		dateKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(dateKey);
		PdfPCell dateVal = new PdfPCell(new Phrase("2023.01.01", valueFont));
		dateVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(dateVal);
		
		String status = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getStatus(), language);
		PdfPCell statusKey = new PdfPCell(new Phrase(status, keyFont));
		statusKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(statusKey);
		PdfPCell statusVal = new PdfPCell(new Phrase("Posted", valueFont));
		statusVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(statusVal);
		
	}
	
   private void populateCompanyInformation(PdfPTable table, SlingHttpServletRequest request,String language) {
	   PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);		
	    emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String companyInfo = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getCompanyInfo(), language);
		PdfPCell companyHead = new PdfPCell(new Phrase(companyInfo, titleFont));
		companyHead.setBorder(Rectangle.NO_BORDER);
		companyHead.setColspan(2);
		table.addCell(companyHead);		
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String companyKey = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getCompany(), language);
		PdfPCell company = new PdfPCell(new Phrase(companyKey, keyFont));
		company.setBorder(Rectangle.NO_BORDER);
		table.addCell(company);
		PdfPCell companyVal = new PdfPCell(new Phrase("TatraPak", valueFont));
		companyVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(companyVal);
		
		String code = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getCode(), language);
		PdfPCell codeKey = new PdfPCell(new Phrase(code, keyFont));
		codeKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeKey);
		PdfPCell codeVal = new PdfPCell(new Phrase("0193", valueFont));
		codeVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeVal);
		
		String country = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getCountry(), language);
		PdfPCell countryKey = new PdfPCell(new Phrase(country, keyFont));
		countryKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(countryKey);
		PdfPCell countryVal = new PdfPCell(new Phrase("India", valueFont));
		countryVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(countryVal);
	}
   
   private void populateSupplierInformation(PdfPTable table, SlingHttpServletRequest request,String language) {
	   PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);		
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String supplierInformation = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getSupplierInfo(), language);		
		PdfPCell supplierInfo = new PdfPCell(new Phrase(supplierInformation, titleFont));
		supplierInfo.setBorder(Rectangle.NO_BORDER);
		supplierInfo.setColspan(2);
		table.addCell(supplierInfo);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String supplier = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getSupplier(), language);	
		PdfPCell supplierKey = new PdfPCell(new Phrase(supplier, keyFont));
		supplierKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(supplierKey);
		PdfPCell supplierVal = new PdfPCell(new Phrase("Alfa Laval Flow Equipment Kunshan", valueFont));
		supplierVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(supplierVal);
		
		String codei18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getCode(), language);
		PdfPCell code = new PdfPCell(new Phrase(codei18n, keyFont));
		code.setBorder(Rectangle.NO_BORDER);
		table.addCell(code);
		PdfPCell codeValue = new PdfPCell(new Phrase("5061011", valueFont));
		codeValue.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeValue);
	}
   
   private void populateDetailsSection(PdfPTable table, SlingHttpServletRequest request,String language) {
	    PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);		
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String detailsi18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getDetails(), language);
		PdfPCell details = new PdfPCell(new Phrase(detailsi18n, titleFont));
		details.setBorder(Rectangle.NO_BORDER);
		details.setColspan(2);
		table.addCell(details);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		
		String totalAmti18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getTotalAmount(), language);
		PdfPCell totAmtIncl = new PdfPCell(new Phrase(totalAmti18n, keyFont));
		totAmtIncl.setBorder(Rectangle.NO_BORDER);
		table.addCell(totAmtIncl);
		PdfPCell totAmtInclVal = new PdfPCell(new Phrase("105,077.47 CNY", valueFont));
		totAmtInclVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(totAmtInclVal);
		
		String taxi18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getTax(), language);
		PdfPCell tax = new PdfPCell(new Phrase(taxi18n, keyFont));
		tax.setBorder(Rectangle.NO_BORDER);
		table.addCell(tax);
		PdfPCell taxVal = new PdfPCell(new Phrase("12,088.56 CNY", valueFont));
		taxVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(taxVal);
		
		String netPayable = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getNetPayable(), language);
		PdfPCell netPay = new PdfPCell(new Phrase(netPayable, keyFont));
		netPay.setBorder(Rectangle.NO_BORDER);
		table.addCell(netPay);
		PdfPCell netPayVal = new PdfPCell(new Phrase("105,077.47 CNY", valueFont));
		netPayVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(netPayVal);
		
		String paymntTerm = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getPaymentTerm(), language);
		PdfPCell payTerm = new PdfPCell(new Phrase(paymntTerm, keyFont));
		payTerm.setBorder(Rectangle.NO_BORDER);
		table.addCell(payTerm);
		PdfPCell payTermVal = new PdfPCell(new Phrase("B110", valueFont));
		payTermVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(payTermVal);
		
		String dueDatei18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getDueDate(), language);
		PdfPCell dueDate = new PdfPCell(new Phrase(dueDatei18n, keyFont));
		dueDate.setBorder(Rectangle.NO_BORDER);
		table.addCell(dueDate);
		PdfPCell dueDateVal = new PdfPCell(new Phrase("2023.09.21", valueFont));
		dueDateVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(dueDateVal);
		
		String paymentMethod = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getPaymentMethod(), language);
		PdfPCell payMethod = new PdfPCell(new Phrase(paymentMethod, keyFont));
		payMethod.setBorder(Rectangle.NO_BORDER);
		table.addCell(payMethod);
		PdfPCell payMethodVal = new PdfPCell(new Phrase("'Payment method'", valueFont));
		payMethodVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(payMethodVal);
		
		String ponoi18n = GlobalUtil.getI18nValueForThisLanguage(
                request, StringUtils.EMPTY, paymentDetailsModel.getTax(), language);
		PdfPCell pono = new PdfPCell(new Phrase(ponoi18n, keyFont));
		pono.setBorder(Rectangle.NO_BORDER);
		table.addCell(pono);
		PdfPCell ponoVal = new PdfPCell(new Phrase("924461197,924461196,924461198,924461199", valueFont));
		ponoVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(ponoVal);	
  	}
	

}
