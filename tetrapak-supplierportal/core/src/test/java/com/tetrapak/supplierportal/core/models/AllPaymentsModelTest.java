package com.tetrapak.supplierportal.core.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;
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
		model.setPublishEnvironment(true);
		model.setDownloadExcelServletUrl(RESOURCE_PATH);
		model.setAllPaymentsApi(RESOURCE_PATH);
		model.setFilterApi(RESOURCE_PATH);
		model.setFetcherror(RESOURCE_PATH);
		model.setPaginationNext("sp.paymentDetails.pageNationNext");
		model.setPaginationPrev("sp.paymentDetails.PaginationPrev");
		model.setMultipono("sp.paymentDetails.Multipono");
		model.setAmountIncludingTaxes("sp.paymentDetails.amountIncludingTaxes");
		model.setAllPaymentsHeading("sp.paymentDetails.allPaymentsHeading");
		model.setCompany("sp.paymentDetails.company");
		model.setCompanyCode("sp.paymentDetails.companyCode");
		model.setCountry("sp.paymentDetails.country");		
		model.setDueDate("sp.paymentDetails.dueDate");		
		model.setInvoiceDate("sp.paymentDetails.invoiceDate");
		model.setInvoiceNo("sp.paymentDetails.invoiceNo");
		model.setNoDataFound("sp.paymentDetails.noDataFound");
		model.setPagination("sp.paymentDetails.pagination");
		model.setPaginationFirst("sp.paymentDetails.paginationFirst");
		model.setPaginationLast("sp.paymentDetails.paginationLast");
		model.setPoNo("sp.paymentDetails.poNo");
		model.setReset("sp.paymentDetails.reset");
		model.setResults("sp.paymentDetails.results");		
		model.setStatus("sp.paymentDetails.status");
		model.setSupplier("sp.paymentDetails.supplier");
		model.setSupplierCode("sp.paymentDetails.supplierCode");
	    model.setWithHoldingTax("sp.paymentDetails.withholdingtax");
	    model.setPaid("sp.paymentDetails.Paid");
	    model.setI18nKeys(RESOURCE_PATH);
	    model.setResource(null);
	    model.setSlingSettingsService(null);
	    model.setApplyFilter("sp.paymentDetails.applyFilter");
	    model.setColumns("sp.paymentDetails.columns");
	    model.setShowHideColumns("sp.paymentDetails.showHideColumns");
	    model.setSelectAll("sp.paymentDetails.selectAll");
	    model.setInvoiceNoTooltip("sp.paymentDetails.invoiceNoTooltip");
	    model.setInvoiceDateTooltip("sp.paymentDetails.invoiceDateTooltip");
	    model.setStatusTooltip("sp.paymentDetails.statusTooltip");
	    model.setCompanyTooltip("sp.paymentDetails.companyTooltip");
	    model.setCompanyCodeTooltip("sp.paymentDetails.companyCodeTooltip");
	    model.setCountryTooltip("sp.paymentDetails.countryTooltip");
	    model.setSupplierTooltip("sp.paymentDetails.supplierTooltip");
	    model.setSupplierCodeTooltip("sp.paymentDetails.supplierCodeTooltip");
	    model.setWithHoldingTaxTooltip("sp.paymentDetails.withHoldingTaxTooltip");
	    model.setDueDateTooltip("sp.paymentDetails.dueDateTooltip");
	    model.setPoNoTooltip("sp.paymentDetails.poNoTooltip");
	    model.setAmountIncludingTaxesTooltip("sp.paymentDetails.amountIncludingTaxesTooltip");
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
		assertNull(model.getSlingSettingsService());
		assertNotNull(model.getI18nKeys());
		assertNull(model.getResource());
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
		assertEquals("sp.paymentDetails.amountIncludingTaxesTooltip", model.getAmountIncludingTaxesTooltip());
	}
	
	
}
