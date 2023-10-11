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
		PaymentDetailsModel model = initPaymentModel();
		boolean flag = impl.preparePdf(results.getData().get(0), servletRequest, response, model);
		assertFalse(flag);
    }
    
    private PaymentDetailsModel initPaymentModel() {
    	PaymentDetailsModel model = new PaymentDetailsModel();
    	model.setPublishEnvironment(true);
		model.setBankAccount("sp.paymentDetails.bankDetails");
		model.setCode("sp.paymentDetails.code");
		model.setCompany("sp.paymentDetails.company");
		model.setCompanyCode("sp.paymentDetails.companyCode");
		model.setCompanyInfo("sp.paymentDetails.companyInfo");
		model.setCountry("sp.paymentDetails.country");
		model.setDate("sp.paymentDetails.date");
		model.setDetails("sp.paymentDetails.details");
		model.setDueDate("sp.paymentDetails.dueDate");
		model.setEmptyPaymentDetails("sp.paymentDetails.emptyPaymentDetails");
		model.setFetchError("sp.paymentDetails.fetchError");
		model.setInvoiceInfo("sp.paymentDetails.invoiceInfo");		
		model.setInvoiceNo("sp.paymentDetails.invoiceNo");
		model.setMultiPoNo("sp.paymentDetails.multipono");
		model.setNetPayable("sp.paymentDetails.netpayable");
		model.setPaidDate("sp.paymentDetails.paidDate");
		model.setPaymentMethod("sp.paymentDetails.paymentMethod");
		model.setPaymentTerm("sp.paymentDetails.paymentTerm");
		model.setPoNo("sp.paymentDetails.poNo");
		model.setStatus("sp.paymentDetails.status");		
		model.setSupplier("sp.paymentDetails.supplier");
		model.setSupplierCode("sp.paymentDetails.supplierCode");
	    model.setTax("sp.paymentDetails.tax");
	    model.setTotalAmount("sp.paymentDetails.totalAmount");
	    model.setWithHoldingTaxes("sp.paymentDetails.withHoldingTaxes");
	    model.setSupplierInfo("sp.paymentDetails.supplierInformtaion");
	    model.setExportToPDF("sp.paymentDetails.exportToPDF");
	    return model;
    }
    
    

}
