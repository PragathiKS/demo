package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ACCEPT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_JSON;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_PDF;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.APPLICATION_URL_ENCODED;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.ATTACHMENT_FILENAME;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.AUTHORIZATION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.BEARER;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_DISPOSITION;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.CONTENT_TYPE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.COUNT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.DATE_FORMAT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.DOCUMENT_REFERENCE_ID;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.FROM_DATE_TIME;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.INVOICE_MAPPING;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.NOTOSERIFCJKSC_BOLD;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.NOTOSERIFCJKSC_LIGHT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.NOTOSERIF_BOLD;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.NOTOSERIF_LIGHT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.PDF_EXT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_FAILURE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESPONSE_STATUS_SUCCESS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;
import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.TO_DATE_TIME;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tetrapak.supplierportal.core.bean.PaymentDetails;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;
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
	private static final String NEW_LINE = "\n";
	private static final String COMMA = ",";
	private static final String EMPTY_STRING = " ";

	@Reference
	private APIGEEService apiGeeService;

	@Reference
	private UrlService urlService;
	
	@Reference
	private InvoiceStatusService invoiceStatusService;

	private Font titleFont;
	private Font keyFont;
	private Font valueFont;
	private PaymentDetailsModel paymentDetailsModel;
	
	
	private HttpGet createRequest(final String apiURL, final String authTokenStr, final String docReferId)
			throws UnsupportedEncodingException, URISyntaxException {
		HttpGet getRequest = new HttpGet(apiURL);
		getRequest.addHeader(AUTHORIZATION, BEARER + authTokenStr);
		getRequest.addHeader(CONTENT_TYPE, APPLICATION_URL_ENCODED);
		getRequest.addHeader(ACCEPT, APPLICATION_JSON);
		int fromToDateGapInMonths = invoiceStatusService.getFromToDateGapInMonthsVal();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDateTime toDate = LocalDateTime.now();
		LocalDateTime fromDate = toDate.minusMonths(fromToDateGapInMonths);
		String toDateStr = toDate.format(dateTimeFormatter);
		String fromDateStr = fromDate.format(dateTimeFormatter);

		URI uri = new URIBuilder(getRequest.getURI()).addParameter(DOCUMENT_REFERENCE_ID, docReferId)
				.addParameter(COUNT, "1").addParameter(FROM_DATE_TIME, fromDateStr)
				.addParameter(TO_DATE_TIME, toDateStr).build();
		((HttpRequestBase) getRequest).setURI(uri);
		return getRequest;
	}

	@Override
	public JsonObject retrievePaymentDetails(String authTokenStr, String docReferId)
			throws IOException {
		LOGGER.debug("PaymentInvoiceDownloadServiceImpl#preparePdf-- Start");
		JsonObject jsonResponse = new JsonObject();
		final String apiURL = apiGeeService.getApigeeServiceUrl()
				+ GlobalUtil.getSelectedApiMapping(apiGeeService, INVOICE_MAPPING);
		HttpClient httpClient = HttpClientBuilder.create().build();
		int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		try {
			HttpGet getRequest = createRequest(apiURL, authTokenStr, docReferId);
			HttpResponse httpResponse = httpClient.execute(getRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			jsonResponse.addProperty(STATUS_CODE, statusCode);
			LOGGER.debug("Http Post request status code: {}", statusCode);
			jsonResponse = HttpUtil.setJsonResponse(jsonResponse, httpResponse);
			jsonResponse.addProperty(STATUS, RESPONSE_STATUS_SUCCESS);
			LOGGER.debug("Payment Details Call is Success");
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
		
		String fileName = paymentDetails.getDocumentReferenceID() + "-" + LocalDate.now();
		response.setContentType(APPLICATION_PDF);
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName + PDF_EXT);
		try {
			setUpFonts(language);
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			document.add(new Chunk(EMPTY_STRING));
			PDFUtil.drawImage(document, urlService.getImagesUrl() , 180, 69);
			
			PdfPTable table = new PdfPTable(2);
			float[] columnWidths = new float[] { 35f, 65f };
			table.setWidths(columnWidths);
			table.setWidthPercentage(80);
			table.setHorizontalAlignment(50);

			populateInvoiceInformation(paymentDetails, table, request, language);
			populateCompanyInformation(paymentDetails, table, request, language);
			populateSupplierInformation(paymentDetails, table, request, language);
			populateDetailsSection(paymentDetails, table, request, language);
			document.add(table);
			document.close();
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception while preparing PDF File ", e);
			return false;
		}
	}

	private void setUpFonts(String language) throws IOException, DocumentException {
		final String FONT_RESOURCES = urlService.getFontsUrl();
		try {
			switch (language) {
			case "zh":
				keyFont = FontUtil.getScFont(FONT_RESOURCES + NOTOSERIFCJKSC_LIGHT);
				titleFont = FontUtil.getScFontBold(FONT_RESOURCES + NOTOSERIFCJKSC_BOLD);
				valueFont = FontUtil.getScFontBold(FONT_RESOURCES + NOTOSERIFCJKSC_BOLD);
				break;
			default:
				keyFont = FontUtil.getEnFont(FONT_RESOURCES + NOTOSERIF_LIGHT);
				titleFont = FontUtil.getEnFontBold(FONT_RESOURCES + NOTOSERIF_BOLD);
				valueFont = FontUtil.getEnFontBold(FONT_RESOURCES + NOTOSERIF_BOLD);
				break;
			}
		} catch (IOException | DocumentException e) {
			LOGGER.error("Unable to set Up Font: ", e);
			throw e;
		}
		titleFont.setSize(15);
		keyFont.setSize(12);
		valueFont.setSize(12);
		keyFont.setColor(BaseColor.DARK_GRAY);
		titleFont.setColor(BaseColor.DARK_GRAY);
		valueFont.setColor(BaseColor.DARK_GRAY);
	}

	private void populateInvoiceInformation(PaymentDetails paymentDetails, PdfPTable table,
			SlingHttpServletRequest request, String language) {
		String invoiceInfo = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getInvoiceInfo(), language);
		PdfPCell invoiceHead = new PdfPCell(new Phrase(invoiceInfo, titleFont));
		invoiceHead.setBorder(Rectangle.NO_BORDER);
		invoiceHead.setColspan(2);
		table.addCell(invoiceHead);

		PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String invoiceNo = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getInvoiceNo(), language);
		PdfPCell invoiceNoK = new PdfPCell(new Phrase(invoiceNo, keyFont));
		invoiceNoK.setBorder(Rectangle.NO_BORDER);
		table.addCell(invoiceNoK);
		PdfPCell invoiceNoV = new PdfPCell(new Phrase(paymentDetails.getDocumentReferenceID(), valueFont));
		invoiceNoV.setBorder(Rectangle.NO_BORDER);
		table.addCell(invoiceNoV);

		String date = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY, paymentDetailsModel.getDate(),
				language);
		PdfPCell dateKey = new PdfPCell(new Phrase(date, keyFont));
		dateKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(dateKey);
		PdfPCell dateVal = new PdfPCell(new Phrase(paymentDetails.getDocumentDate(), valueFont));
		dateVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(dateVal);

		
		Map<String, List<String>> map = invoiceStatusService.invoiceStatusCodeMap();
		if(null != map && null != map.entrySet()) {
			map.entrySet().stream().forEach(entry -> {
				if(entry != null && entry.getValue() != null && entry.getValue().contains(paymentDetails.getInvoiceStatusCode())) {
					String status = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
							paymentDetailsModel.getStatus(), language);
					PdfPCell statusKey = new PdfPCell(new Phrase(status, keyFont));
					statusKey.setBorder(Rectangle.NO_BORDER);
					table.addCell(statusKey);
					PdfPCell statusVal = new PdfPCell(new Phrase(entry.getKey(), valueFont));
					statusVal.setBorder(Rectangle.NO_BORDER);
					table.addCell(statusVal);
				}
			});
		}
	}

	private void populateCompanyInformation(PaymentDetails paymentDetails, PdfPTable table,
			SlingHttpServletRequest request, String language) {
		PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String companyInfo = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getCompanyInfo(), language);
		PdfPCell companyHead = new PdfPCell(new Phrase(companyInfo, titleFont));
		companyHead.setBorder(Rectangle.NO_BORDER);
		companyHead.setColspan(2);
		table.addCell(companyHead);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String companyKey = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getCompany(), language);
		PdfPCell company = new PdfPCell(new Phrase(companyKey, keyFont));
		company.setBorder(Rectangle.NO_BORDER);
		table.addCell(company);
		PdfPCell companyVal = new PdfPCell(new Phrase(paymentDetails.getCompanyName(), valueFont));
		companyVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(companyVal);

		String code = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY, paymentDetailsModel.getCode(),
				language);
		PdfPCell codeKey = new PdfPCell(new Phrase(code, keyFont));
		codeKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeKey);
		PdfPCell codeVal = new PdfPCell(new Phrase(paymentDetails.getCompanyCode(), valueFont));
		codeVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeVal);

		String country = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getCountry(), language);
		PdfPCell countryKey = new PdfPCell(new Phrase(country, keyFont));
		countryKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(countryKey);
		PdfPCell countryVal = new PdfPCell(new Phrase(paymentDetails.getCompanyCountry(), valueFont));
		countryVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(countryVal);
	}

	private void populateSupplierInformation(PaymentDetails paymentDetails, PdfPTable table,
			SlingHttpServletRequest request, String language) {
		PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String supplierInformation = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getSupplierInfo(), language);
		PdfPCell supplierInfo = new PdfPCell(new Phrase(supplierInformation, titleFont));
		supplierInfo.setBorder(Rectangle.NO_BORDER);
		supplierInfo.setColspan(2);
		table.addCell(supplierInfo);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String supplier = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getSupplier(), language);
		PdfPCell supplierKey = new PdfPCell(new Phrase(supplier, keyFont));
		supplierKey.setBorder(Rectangle.NO_BORDER);
		table.addCell(supplierKey);
		PdfPCell supplierVal = new PdfPCell(new Phrase(paymentDetails.getSupplierName(), valueFont));
		supplierVal.setBorder(Rectangle.NO_BORDER);
		table.addCell(supplierVal);

		String codei18n = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getCode(), language);
		PdfPCell code = new PdfPCell(new Phrase(codei18n, keyFont));
		code.setBorder(Rectangle.NO_BORDER);
		table.addCell(code);
		PdfPCell codeValue = new PdfPCell(new Phrase(paymentDetails.getSupplier(), valueFont));
		codeValue.setBorder(Rectangle.NO_BORDER);
		table.addCell(codeValue);
	}

	private void populateDetailsSection(PaymentDetails paymentDetails, PdfPTable table, SlingHttpServletRequest request,
			String language) {
		PdfPCell emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		String detailsi18n = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
				paymentDetailsModel.getDetails(), language);
		PdfPCell details = new PdfPCell(new Phrase(detailsi18n, titleFont));
		details.setBorder(Rectangle.NO_BORDER);
		details.setColspan(2);
		table.addCell(details);
		emptyCell = new PdfPCell(new Phrase(NEW_LINE, titleFont));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setColspan(2);
		table.addCell(emptyCell);

		if (StringUtils.isNotBlank(paymentDetails.getAmountInTransactionCurrency())) {
			String totalAmti18n = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getTotalAmount(), language);
			PdfPCell totAmtIncl = new PdfPCell(new Phrase(totalAmti18n, keyFont));
			totAmtIncl.setBorder(Rectangle.NO_BORDER);
			table.addCell(totAmtIncl);
			PdfPCell totAmtInclVal = new PdfPCell(
					new Phrase(paymentDetails.getAmountInTransactionCurrency()+" "+paymentDetails.getTransactionCurrency(), valueFont));
			totAmtInclVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(totAmtInclVal);
		}
		
		if (StringUtils.isNotBlank(paymentDetails.getWithholdingTaxAmount())) {
			String withHoldingTaxes = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getWithHoldingTaxes(), language);
			PdfPCell withHoldingTaxesKey = new PdfPCell(new Phrase(withHoldingTaxes, keyFont));
			withHoldingTaxesKey.setBorder(Rectangle.NO_BORDER);
			table.addCell(withHoldingTaxesKey);
			PdfPCell withHoldingTaxesVal = new PdfPCell(
					new Phrase(paymentDetails.getWithholdingTaxAmount()+" "+paymentDetails.getTransactionCurrency(), valueFont));
			withHoldingTaxesVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(withHoldingTaxesVal);
		}

		if (StringUtils.isNotBlank(paymentDetails.getPaymentTerms())) {
			String paymntTerm = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getPaymentTerm(), language);
			PdfPCell payTerm = new PdfPCell(new Phrase(paymntTerm, keyFont));
			payTerm.setBorder(Rectangle.NO_BORDER);
			table.addCell(payTerm);
			PdfPCell payTermVal = new PdfPCell(new Phrase(paymentDetails.getPaymentTerms(), valueFont));
			payTermVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(payTermVal);
		}

		if (StringUtils.isNotBlank(paymentDetails.getClearingDate())) {
			String paidDate = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getPaidDate(), language);
			PdfPCell paidDateKey = new PdfPCell(new Phrase(paidDate, keyFont));
			paidDateKey.setBorder(Rectangle.NO_BORDER);
			table.addCell(paidDateKey);
			PdfPCell paidDateVal = new PdfPCell(new Phrase(paymentDetails.getClearingDate(), valueFont));
			paidDateVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(paidDateVal);
		}
		else if (StringUtils.isNotBlank(paymentDetails.getDueCalculationBaseDate())) {
			String dueDatei18n = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getDueDate(), language);
			PdfPCell dueDate = new PdfPCell(new Phrase(dueDatei18n, keyFont));
			dueDate.setBorder(Rectangle.NO_BORDER);
			table.addCell(dueDate);
			PdfPCell dueDateVal = new PdfPCell(new Phrase(paymentDetails.getDueCalculationBaseDate(), valueFont));
			dueDateVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(dueDateVal);
		}
		
		if (StringUtils.isNotBlank(paymentDetails.getBankAccount())) {
			String bankAccount = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getBankAccount(), language);
			PdfPCell bankAccountCell = new PdfPCell(new Phrase(bankAccount, keyFont));
			bankAccountCell.setBorder(Rectangle.NO_BORDER);
			table.addCell(bankAccountCell);
			PdfPCell bankAccountVal = new PdfPCell(new Phrase(paymentDetails.getBankAccount(), valueFont));
			bankAccountVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(bankAccountVal);
		}

		if (StringUtils.isNotBlank(paymentDetails.getPaymentMethod())) {
			String paymentMethod = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getPaymentMethod(), language);
			PdfPCell payMethod = new PdfPCell(new Phrase(paymentMethod, keyFont));
			payMethod.setBorder(Rectangle.NO_BORDER);
			table.addCell(payMethod);
			PdfPCell payMethodVal = new PdfPCell(new Phrase(paymentDetails.getPaymentMethod(), valueFont));
			payMethodVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(payMethodVal);
		}

		if (Objects.nonNull(paymentDetails.getPurchasingDocuments())
				&& paymentDetails.getPurchasingDocuments().length > 0) {
			String ponoi18n = GlobalUtil.getI18nValueForThisLanguage(request, StringUtils.EMPTY,
					paymentDetailsModel.getPoNo(), language);
			PdfPCell pono = new PdfPCell(new Phrase(ponoi18n, keyFont));
			pono.setBorder(Rectangle.NO_BORDER);
			table.addCell(pono);
			String ponos = Arrays.stream(paymentDetails.getPurchasingDocuments()).boxed().map(String::valueOf)
					.collect(Collectors.joining(COMMA));

			PdfPCell ponoVal = new PdfPCell(new Phrase(ponos, valueFont));
			ponoVal.setBorder(Rectangle.NO_BORDER);
			table.addCell(ponoVal);
		}
	}
}
