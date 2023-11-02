package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.RESULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.bean.PaymentDetailResponse;
import com.tetrapak.supplierportal.core.models.PaymentDetailsModel;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;
import com.tetrapak.supplierportal.core.services.UrlService;
import com.tetrapak.supplierportal.core.utils.HttpUtil;
@RunWith(MockitoJUnitRunner.class)
public class PaymentInvoiceDownloadServiceImplTest {
	
	private static final String APIGEE_SERVICE_URL = "https://api-mig.tetrapak.com";
	private static final String APIGEE_MAPPINGS = "token-generator:bin/customerhub/token-generator";
	
	private static final String DOCUMENT_REFERENCE_ID = "79124499";

	private static final String AUTHTOKEN = "authToken";
	
	  /** The Constant STATUSCODE. */
	private static final String STATUS_CODE = "status_code";
	
	@Mock
    private SlingHttpServletRequest servletRequest;

    @Mock
    private SlingHttpServletResponse response;
    
    @Mock
	private APIGEEService apiGeeService;

    @Mock
	private UrlService urlService;
    
    @Mock
    private InvoiceStatusService invoiceStatusService;

    @InjectMocks
    private PaymentInvoiceDownloadServiceImpl impl;
    
    @Mock
	private SlingSettingsService slingSettingsService;
    
    @Mock
	private PaymentDetailsModel paymentDetailsModel;
    
    
    @Test
    public void retrievePaymentDetails() throws IOException {
    	when(apiGeeService.getApigeeServiceUrl()).thenReturn(APIGEE_SERVICE_URL);
    	when(apiGeeService.getApiMappings()).thenReturn(new String[] {APIGEE_MAPPINGS});
    	when(invoiceStatusService.getFromToDateGapInMonthsVal()).thenReturn(12);
    	JsonObject  jsonObj = impl.retrievePaymentDetails(AUTHTOKEN,DOCUMENT_REFERENCE_ID);
    	assertNotNull(jsonObj);
    	assertEquals(200, jsonObj.get(STATUS_CODE).getAsInt());
    }
    
    @Test
    public void retrievePaymentDetailsWhenUriIsNotValid() throws IOException {
    	when(apiGeeService.getApigeeServiceUrl()).thenReturn("--");
    	when(apiGeeService.getApiMappings()).thenReturn(new String[] {APIGEE_MAPPINGS});
    	JsonObject  jsonObj = impl.retrievePaymentDetails(AUTHTOKEN,DOCUMENT_REFERENCE_ID);
    	assertNotNull(jsonObj);
    	assertEquals(500, jsonObj.get(STATUS_CODE).getAsInt());
    }
    
    @Test
    public void preparePdf() throws IOException {
    	String content = IOUtils.toString(this.getClass().getResourceAsStream("/paymentdetails/view-payment-data.json"),
				"UTF-8");
		Gson gson = new Gson();
		JsonElement element = gson.fromJson(content, JsonElement.class);
		JsonObject jsonObj = element.getAsJsonObject();
		JsonElement resultsResponse = jsonObj.get(RESULT);
		PaymentDetailResponse results = gson.fromJson(HttpUtil.getStringFromJsonWithoutEscape(resultsResponse), PaymentDetailResponse.class);
		when(urlService.getFontsUrl()).thenReturn("/paymentdetails/");
		when(urlService.getImagesUrl()).thenReturn("/paymentdetails/");
		when(paymentDetailsModel.getBankAccount()).thenReturn("sp.paymentDetails.bankDetails");
		when(paymentDetailsModel.isPublishEnvironment()).thenReturn(true);
		when(paymentDetailsModel.getCode()).thenReturn("sp.paymentDetails.code");
		when(paymentDetailsModel.getCompany()).thenReturn("sp.paymentDetails.company");
		when(paymentDetailsModel.getCompanyCode()).thenReturn("sp.paymentDetails.companyCode");
		when(paymentDetailsModel.getCompanyInfo()).thenReturn("sp.paymentDetails.companyInfo");
		when(paymentDetailsModel.getCountry()).thenReturn("sp.paymentDetails.country");
		when(paymentDetailsModel.getDate()).thenReturn("sp.paymentDetails.date");
		when(paymentDetailsModel.getDetails()).thenReturn("sp.paymentDetails.details");
		when(paymentDetailsModel.getDueDate()).thenReturn("sp.paymentDetails.dueDate");
		when(paymentDetailsModel.getEmptyPaymentDetails()).thenReturn("sp.paymentDetails.emptyPaymentDetails");
		when(paymentDetailsModel.getFetchError()).thenReturn("sp.paymentDetails.fetchError");
		when(paymentDetailsModel.getInvoiceInfo()).thenReturn("sp.paymentDetails.invoiceInfo");
		when(paymentDetailsModel.getInvoiceNo()).thenReturn("sp.paymentDetails.invoiceNo");
		when(paymentDetailsModel.getMultiPoNo()).thenReturn("sp.paymentDetails.multiPoNo");
		when(paymentDetailsModel.getNetPayable()).thenReturn("sp.paymentDetails.netPayable");
		when(paymentDetailsModel.getPaidDate()).thenReturn("sp.paymentDetails.paidDate");
		when(paymentDetailsModel.getPaymentMethod()).thenReturn("sp.paymentDetails.paymentMethod");
		when(paymentDetailsModel.getPaymentTerm()).thenReturn("sp.paymentDetails.paymentTerm");
		when(paymentDetailsModel.getPoNo()).thenReturn("sp.paymentDetails.pono");
		when(paymentDetailsModel.getStatus()).thenReturn("sp.paymentDetails.status");
		when(paymentDetailsModel.getSupplier()).thenReturn("sp.paymentDetails.supplier");
		when(paymentDetailsModel.getSupplierCode()).thenReturn("sp.paymentDetails.supplierCode");
		when(paymentDetailsModel.getTax()).thenReturn("sp.paymentDetails.totalAmount");
		when(paymentDetailsModel.getTotalAmount()).thenReturn("sp.paymentDetails.bankDetails");
		when(paymentDetailsModel.getWithHoldingTaxes()).thenReturn("sp.paymentDetails.withHoldingTaxes");
		when(paymentDetailsModel.getSupplierInfo()).thenReturn("sp.paymentDetails.supplierInfo");
		when(paymentDetailsModel.getExportToPDF()).thenReturn("sp.paymentDetails.exportToPDF");
		boolean flag = impl.preparePdf(results.getData().get(0), servletRequest, response, paymentDetailsModel);
		assertFalse(flag);
    }
}
