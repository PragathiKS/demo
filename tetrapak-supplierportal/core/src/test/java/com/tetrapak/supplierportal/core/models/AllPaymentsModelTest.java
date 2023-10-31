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

public class AllPaymentsModelTest {

	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "allpayments/allpayments.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en/allpayments";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/allpayments";
	
	/** The aem context. */
	@Rule
    public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

	/** The model. */
	private AllPaymentsModel model;
	
	/** The apigeeService. */
	private APIGEEService apigeeService;
	
	/** The invoiceStatusService. */
	private InvoiceStatusService invoiceStatusService;;
	
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
		model = resource.adaptTo(AllPaymentsModel.class);
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
		assertNotNull( model.getAllPaymentsApi());
		assertNotNull(model.getDownloadExcelServletUrl());
		assertNotNull(model.getFilterApi());
		assertNotNull(model.getSlingSettingsService());
		assertNotNull(model.getI18nKeys());
		assertNotNull(model.getResource());
		assertEquals(3,model.getPaymentsFromToDateGapInMonths());
		assertTrue(model.isPublishEnvironment());
		assertEquals("sp.paymentDetails.amountIncludingTaxes", model.getAmountIncludingTaxes());
		assertEquals("sp.paymentDetails.allPaymentsHeading", model.getAllPaymentsHeading());
		assertEquals("sp.paymentDetails.company", model.getCompany());
		assertEquals("sp.paymentDetails.companyCode", model.getCompanyCode());
		assertEquals("sp.paymentDetails.country", model.getCountry());		
		assertEquals("sp.paymentDetails.dueDate", model.getDueDate());		
		assertEquals("sp.paymentDetails.invoiceDate", model.getInvoiceDate());
		assertEquals("sp.paymentDetails.invoiceNo", model.getInvoiceNo());
		assertEquals("sp.paymentDetails.noDataFound", model.getNoDataFound());
		assertEquals("sp.paymentDetails.pagination", model.getPagination());
		assertEquals("sp.paymentDetails.paginationFirst", model.getPaginationFirst());
		assertEquals("sp.paymentDetails.paginationLast", model.getPaginationLast());
		assertEquals("sp.paymentDetails.poNo", model.getPoNo());
		assertEquals("sp.paymentDetails.reset", model.getReset());
		assertEquals("sp.paymentDetails.results", model.getResults());		
		assertEquals("sp.paymentDetails.status", model.getStatus());
		assertEquals("sp.paymentDetails.supplier", model.getSupplier());
		assertEquals("sp.paymentDetails.supplierCode", model.getSupplierCode());
		assertEquals("sp.paymentDetails.withholdingtax", model.getWithHoldingTax());	
		assertEquals("sp.paymentDetails.showHideColumns", model.getShowHideColumns());	
		assertEquals("sp.paymentDetails.columns", model.getColumns());	
		assertEquals("sp.paymentDetails.selectAll", model.getSelectAll());	
		assertEquals("sp.paymentDetails.applyFilter", model.getApplyFilter());
		assertEquals("sp.paymentDetails.invoiceNoTooltip", model.getInvoiceNoTooltip());
		assertEquals("sp.paymentDetails.invoiceDateTooltip", model.getInvoiceDateTooltip());
		assertEquals("sp.paymentDetails.statusTooltip", model.getStatusTooltip());
		assertEquals("sp.paymentDetails.companyTooltip", model.getCompanyTooltip());
		assertEquals("sp.paymentDetails.companyCodeTooltip", model.getCompanyCodeTooltip());
		assertEquals("sp.paymentDetails.countryTooltip", model.getCountryTooltip());
		assertEquals("sp.paymentDetails.supplierTooltip", model.getSupplierTooltip());
		assertEquals("sp.paymentDetails.supplierCodeTooltip", model.getSupplierCodeTooltip());
		assertEquals("sp.paymentDetails.withHoldingTaxTooltip", model.getWithHoldingTaxTooltip());
		assertEquals("sp.paymentDetails.dueDateTooltip", model.getDueDateTooltip());
		assertEquals("sp.paymentDetails.poNoTooltip", model.getPoNoTooltip());
		assertEquals("sp.paymentDetails.last90Days", model.getLast90Days());
		assertEquals("sp.paymentDetails.other", model.getOther());
		assertEquals("sp.paymentDetails.from", model.getFrom());
		assertEquals("sp.paymentDetails.to", model.getTo());
		assertEquals("sp.paymentDetails.invalidDate", model.getInvalidDate());
		assertEquals("sp.paymentDetails.invalidDateRange", model.getInvalidDateRange());
		assertEquals("sp.paymentDetails.confirmed", model.getConfirmed());
		assertEquals("sp.paymentDetails.filterSelected", model.getFilterSelected());
		assertEquals("sp.paymentDetails.amountIncludingTaxesTooltip", model.getAmountIncludingTaxesTooltip());
	}
	
	
}
