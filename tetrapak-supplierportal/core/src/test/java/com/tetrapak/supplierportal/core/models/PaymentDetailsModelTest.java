package com.tetrapak.supplierportal.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.supplierportal.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.supplierportal.core.mock.MockInvoiceStatusServiceImpl;
import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PaymentDetailsModelTest {

	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "paymentdetails/paymentdetails.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en/paymentdetails";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/paymentdetails";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private PaymentDetailsModel model;
	
	/** The apigeeService. */
	private APIGEEService apigeeService;
	
	/** The invoiceStatusService. */
	private InvoiceStatusService invoiceStatusService;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
	    apigeeService = new MockAPIGEEServiceImpl();
	    invoiceStatusService = new MockInvoiceStatusServiceImpl();
        aemContext.registerService(APIGEEService.class, apigeeService);
        aemContext.registerService(InvoiceStatusService.class, invoiceStatusService);
		Resource resource = aemContext.currentResource(RESOURCE_PATH);
		model = resource.adaptTo(PaymentDetailsModel.class);
		model.setResource(resource);
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
	    model.setI18nKeys(RESOURCE_PATH);
	    model.setWithHoldingTaxes("sp.paymentDetails.withHoldingTaxes");
	    model.setSupplierInfo("sp.paymentDetails.supplierInformtaion");
	    model.setExportToPDF("sp.paymentDetails.exportToPDF");
	    model.setViewPaymentDataUrl(RESOURCE_PATH);
	}
	
	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null",model);
	}
	
	@Test
	public void test() {
		assertNotNull( model.getExportToPdfURL());
		assertNotNull( model.getResource());
		assertNotNull(model.getViewPaymentDataUrl());
		assertNotNull(model.getI18nKeys());
		assertTrue(model.isPublishEnvironment());
		assertEquals("sp.paymentDetails.bankDetails", model.getBankAccount());
		assertEquals("sp.paymentDetails.code", model.getCode());
		assertEquals("sp.paymentDetails.company", model.getCompany());
		assertEquals("sp.paymentDetails.companyCode", model.getCompanyCode());
		assertEquals("sp.paymentDetails.companyInfo", model.getCompanyInfo());		
		assertEquals("sp.paymentDetails.country", model.getCountry());		
		assertEquals("sp.paymentDetails.date", model.getDate());
		assertEquals("sp.paymentDetails.details", model.getDetails());
		assertEquals("sp.paymentDetails.dueDate", model.getDueDate());
		assertEquals("sp.paymentDetails.exportToPDF", model.getExportToPDF());
		assertEquals("sp.paymentDetails.emptyPaymentDetails", model.getEmptyPaymentDetails());
		assertEquals("sp.paymentDetails.fetchError", model.getFetchError());
		assertEquals("sp.paymentDetails.invoiceInfo", model.getInvoiceInfo());		
		assertEquals("sp.paymentDetails.invoiceNo", model.getInvoiceNo());
		assertEquals("sp.paymentDetails.multipono", model.getMultiPoNo());
		assertEquals("sp.paymentDetails.netpayable", model.getNetPayable());
		assertEquals("sp.paymentDetails.paidDate", model.getPaidDate());	
		assertEquals("sp.paymentDetails.paymentMethod", model.getPaymentMethod());	
		assertEquals("sp.paymentDetails.paymentTerm", model.getPaymentTerm());
		assertEquals("sp.paymentDetails.poNo", model.getPoNo());
		assertEquals("sp.paymentDetails.status", model.getStatus());
		assertEquals("sp.paymentDetails.supplier", model.getSupplier());
		assertEquals("sp.paymentDetails.supplierCode", model.getSupplierCode());
		assertEquals("sp.paymentDetails.supplierInformtaion", model.getSupplierInfo());
		assertEquals("sp.paymentDetails.tax", model.getTax());
		assertEquals("sp.paymentDetails.totalAmount", model.getTotalAmount());
		assertEquals("sp.paymentDetails.withHoldingTaxes", model.getWithHoldingTaxes());
		assertEquals(3,model.getPaymentsFromToDateGapInMonths());
	}
}
