package com.tetrapak.supplierportal.core.models;

import static com.tetrapak.supplierportal.core.constants.I18Constants.ALL_PAYMENTS_HEADING;
import static com.tetrapak.supplierportal.core.constants.I18Constants.AMOUNT_INCLUDING_TAXES;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COUNTRY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DUE_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.FETCH_ERROR;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.MULTI_PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.NO_DATA_FOUND;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_FIRST;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_LAST;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_NEXT;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_PREV;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAID;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.RESET;
import static com.tetrapak.supplierportal.core.constants.I18Constants.RESULTS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.STATUS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.WITH_HOLDING_TAX;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.google.gson.Gson;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.services.APIGEEService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AllPaymentsModel {

	/**
	 * The request.
	 */
	@Self
	private Resource resource;

	@ValueMapValue
	private String invoiceNo;

	@ValueMapValue
	private String invoiceDate;

	@ValueMapValue
	private String status;

	@ValueMapValue
	private String company;

	@ValueMapValue
	private String companyCode;

	@ValueMapValue
	private String country;

	@ValueMapValue
	private String supplier;

	@ValueMapValue
	private String supplierCode;

	@ValueMapValue
	private String withHoldingTax;

	@ValueMapValue
	private String dueDate;

	@ValueMapValue
	private String poNo;

	@ValueMapValue
	private String amountIncludingTaxes;

	@ValueMapValue
	private String reset;

	@ValueMapValue
	private String results;

	@ValueMapValue
	private String pagination;

	private String downloadExcelServletUrl;

	private String allPaymentsApi;

	@ValueMapValue
	private String noDataFound;

	private String filterApi;

	@ValueMapValue
	private String paginationFirst;

	@ValueMapValue
	private String paginationLast;

	@ValueMapValue
	private String allPaymentsHeading;

	/** The sling settings service. */
	@OSGiService
	private SlingSettingsService slingSettingsService;

	/** The is publish environment. */
	private boolean isPublishEnvironment = Boolean.FALSE;

	/** The i18n keys. */
	private String i18nKeys;

	/** The service. */
	@OSGiService
	private APIGEEService service;
	
	@ValueMapValue
	private String fetcherror;
	
	@ValueMapValue
	private String multipono;
	
	@ValueMapValue
	private String paginationPrev;
	
	@ValueMapValue
	private String paginationNext;
	
	@ValueMapValue
	private String paid;
	

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getAllPaymentsHeading() {
		return allPaymentsHeading;
	}

	public void setAllPaymentsHeading(String allPaymentsHeading) {
		this.allPaymentsHeading = allPaymentsHeading;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getWithHoldingTax() {
		return withHoldingTax;
	}

	public void setWithHoldingTax(String withHoldingTax) {
		this.withHoldingTax = withHoldingTax;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getAmountIncludingTaxes() {
		return amountIncludingTaxes;
	}

	public void setAmountIncludingTaxes(String amountIncludingTaxes) {
		this.amountIncludingTaxes = amountIncludingTaxes;
	}

	public String getReset() {
		return reset;
	}

	public void setReset(String reset) {
		this.reset = reset;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getPagination() {
		return pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public String getPaginationLast() {
		return paginationLast;
	}

	public void setPaginationLast(String paginationLast) {
		this.paginationLast = paginationLast;
	}

	public String getNoDataFound() {
		return noDataFound;
	}

	public void setNoDataFound(String noDataFound) {
		this.noDataFound = noDataFound;
	}

	public String getPaginationFirst() {
		return paginationFirst;
	}

	public void setPaginationFirst(String paginationFirst) {
		this.paginationFirst = paginationFirst;
	}

	public SlingSettingsService getSlingSettingsService() {
		return slingSettingsService;
	}

	public void setSlingSettingsService(SlingSettingsService slingSettingsService) {
		this.slingSettingsService = slingSettingsService;
	}

	public boolean isPublishEnvironment() {
		return isPublishEnvironment;
	}

	public void setPublishEnvironment(boolean isPublishEnvironment) {
		this.isPublishEnvironment = isPublishEnvironment;
	}

	public String getDownloadExcelServletUrl() {
		return downloadExcelServletUrl;
	}

	public void setDownloadExcelServletUrl(String downloadExcelServletUrl) {
		this.downloadExcelServletUrl = downloadExcelServletUrl;
	}

	public String getAllPaymentsApi() {
		return allPaymentsApi;
	}

	public void setAllPaymentsApi(String allPaymentsApi) {
		this.allPaymentsApi = allPaymentsApi;
	}

	public String getFilterApi() {
		return filterApi;
	}

	public void setFilterApi(String filterApi) {
		this.filterApi = filterApi;
	}

	@PostConstruct
	public void init() {
		Map<String, Object> i18KeyMap = new HashMap<>();
		i18KeyMap.put(INVOICE_NO, getInvoiceNo());
		i18KeyMap.put(INVOICE_DATE, getInvoiceDate());
		i18KeyMap.put(STATUS, getStatus());
		i18KeyMap.put(COMPANY, getCompany());
		i18KeyMap.put(COMPANY_CODE, getCompanyCode());
		i18KeyMap.put(COUNTRY, getCountry());
		i18KeyMap.put(SUPPLIER, getSupplier());
		i18KeyMap.put(SUPPLIER_CODE, getSupplierCode());
		i18KeyMap.put(WITH_HOLDING_TAX, getWithHoldingTax());
		i18KeyMap.put(DUE_DATE, getDueDate());
		i18KeyMap.put(PO_NO, getPoNo());
		i18KeyMap.put(AMOUNT_INCLUDING_TAXES, getAmountIncludingTaxes());
		i18KeyMap.put(RESET, getReset());
		i18KeyMap.put(RESULTS, getResults());
		i18KeyMap.put(PAGINATION, getPagination());
		i18KeyMap.put(NO_DATA_FOUND, getNoDataFound());
		i18KeyMap.put(PAGINATION_FIRST, getPaginationFirst());
		i18KeyMap.put(PAGINATION_LAST, getPaginationLast());
		i18KeyMap.put(ALL_PAYMENTS_HEADING, getAllPaymentsHeading());
		i18KeyMap.put(FETCH_ERROR, getFetcherror());
		i18KeyMap.put(MULTI_PO_NO, getMultipono());
		i18KeyMap.put(PAGINATION_NEXT, getPaginationNext());
		i18KeyMap.put(PAGINATION_PREV, getPaginationPrev());
		i18KeyMap.put(PAID, getPaid());

		if (slingSettingsService.getRunModes().contains(SupplierPortalConstants.PUBLISH)) {
			isPublishEnvironment = Boolean.TRUE;
		}
		Gson gson = new Gson();
		i18nKeys = gson.toJson(i18KeyMap);
		downloadExcelServletUrl = resource.getPath() + SupplierPortalConstants.EXCEL_DOWNLOAD_EXTENSION;

		filterApi = service.getApigeeServiceUrl() 
				+ GlobalUtil.getSelectedApiMapping(service, SupplierPortalConstants.FILTERS_MAPPING);

		allPaymentsApi = service.getApigeeServiceUrl()
				+ GlobalUtil.getSelectedApiMapping(service, SupplierPortalConstants.INVOICE_MAPPING);
	}

	public String getI18nKeys() {
		return i18nKeys;
	}

	public void setI18nKeys(String i18nKeys) {
		this.i18nKeys = i18nKeys;
	}

	public String getFetcherror() {
		return fetcherror;
	}

	public void setFetcherror(String fetcherror) {
		this.fetcherror = fetcherror;
	}

	public String getMultipono() {
		return multipono;
	}

	public void setMultipono(String multipono) {
		this.multipono = multipono;
	}

	public String getPaginationPrev() {
		return paginationPrev;
	}

	public void setPaginationPrev(String paginationPrev) {
		this.paginationPrev = paginationPrev;
	}

	public String getPaginationNext() {
		return paginationNext;
	}

	public void setPaginationNext(String paginationNext) {
		this.paginationNext = paginationNext;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}	
}