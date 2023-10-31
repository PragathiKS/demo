package com.tetrapak.supplierportal.core.models;

import static com.tetrapak.supplierportal.core.constants.I18Constants.ALL_PAYMENTS_HEADING;
import static com.tetrapak.supplierportal.core.constants.I18Constants.AMOUNT_INCLUDING_TAXES;
import static com.tetrapak.supplierportal.core.constants.I18Constants.AMOUNT_INCLUDING_TAXES_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.APPLY_FILTER;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COLUMNS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_CODE_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.CONFIRMED;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COUNTRY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COUNTRY_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DUEDATE_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DUE_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.FETCH_ERROR;
import static com.tetrapak.supplierportal.core.constants.I18Constants.FILTER_SELECTED;
import static com.tetrapak.supplierportal.core.constants.I18Constants.FROM;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVALID_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVALID_DATE_RANGE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_DATE_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_NO_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.LAST_90_DAYS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.MULTI_PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.NO_DATA_FOUND;
import static com.tetrapak.supplierportal.core.constants.I18Constants.OTHER;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_FIRST;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_LAST;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_NEXT;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAGINATION_PREV;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAID;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAYMENT_DETAILS_URL;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PO_NO_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.RESET;
import static com.tetrapak.supplierportal.core.constants.I18Constants.RESULTS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SELECT_ALL;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SHOWHIDE_COLUMNS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.STATUS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.STATUS_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_CODE_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_TOOLTIP;
import static com.tetrapak.supplierportal.core.constants.I18Constants.TO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.WITH_HOLDING_TAX;
import static com.tetrapak.supplierportal.core.constants.I18Constants.WITH_HOLDING_TAX_TOOLTIP;

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
import com.tetrapak.supplierportal.core.services.InvoiceStatusService;
import com.tetrapak.supplierportal.core.utils.GlobalUtil;
import com.tetrapak.supplierportal.core.utils.LinkUtil;

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
	
	@OSGiService
	private InvoiceStatusService invoiceStatusService;
	
	private int paymentsFromToDateGapInMonths;

	@ValueMapValue
	private String paymentDetailsURL;
	
	@ValueMapValue
	private String showHideColumns;
	
	@ValueMapValue
	private String columns;
	
	@ValueMapValue
	private String selectAll;
	
	@ValueMapValue
	private String applyFilter;
	
	@ValueMapValue
	private String invoiceDateTooltip;
	
	@ValueMapValue
	private String dueDateTooltip;
	
	@ValueMapValue
	private String companyTooltip;
	
	@ValueMapValue
	private String companyCodeTooltip;
	
	@ValueMapValue
	private String countryTooltip;
	
	@ValueMapValue
	private String amountIncludingTaxesTooltip;
	
	@ValueMapValue
	private String withHoldingTaxTooltip;
	
	@ValueMapValue
	private String statusTooltip;
	
	@ValueMapValue
	private String invoiceNoTooltip;
	
	@ValueMapValue
	private String supplierTooltip;
	
	@ValueMapValue
	private String supplierCodeTooltip;
	
	@ValueMapValue
	private String poNoTooltip;
	
	@ValueMapValue
	private String last90Days;
	
	@ValueMapValue
	private String other;
	
	@ValueMapValue
	private String from;
	
	@ValueMapValue
	private String to;
	
	@ValueMapValue
	private String invalidDate;
	
	@ValueMapValue
	private String invalidDateRange;
	
	@ValueMapValue
	private String confirmed;
	
	@ValueMapValue
	private String filterSelected;
	

	public Resource getResource() {
		return resource;
	}

	public String getAllPaymentsHeading() {
		return allPaymentsHeading;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public String getStatus() {
		return status;
	}

	
	public String getCompany() {
		return company;
	}

	public String getCompanyCode() {
		return companyCode;
	}


	public String getCountry() {
		return country;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public String getWithHoldingTax() {
		return withHoldingTax;
	}

	public String getDueDate() {
		return dueDate;
	}

	public String getPoNo() {
		return poNo;
	}

	public String getAmountIncludingTaxes() {
		return amountIncludingTaxes;
	}

	public String getReset() {
		return reset;
	}

	public String getResults() {
		return results;
	}

	public String getPagination() {
		return pagination;
	}

	public String getPaginationLast() {
		return paginationLast;
	}

	public String getNoDataFound() {
		return noDataFound;
	}

	public String getPaginationFirst() {
		return paginationFirst;
	}

	public SlingSettingsService getSlingSettingsService() {
		return slingSettingsService;
	}

	public boolean isPublishEnvironment() {
		return isPublishEnvironment;
	}

	public String getDownloadExcelServletUrl() {
		return downloadExcelServletUrl;
	}

	public String getAllPaymentsApi() {
		return allPaymentsApi;
	}

	public String getFilterApi() {
		return filterApi;
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
		i18KeyMap.put(PAYMENT_DETAILS_URL, getPaymentDetailsURL());
		i18KeyMap.put(COLUMNS, getColumns());
		i18KeyMap.put(SHOWHIDE_COLUMNS, getShowHideColumns());
		i18KeyMap.put(SELECT_ALL, getSelectAll());
		i18KeyMap.put(APPLY_FILTER, getApplyFilter());
		i18KeyMap.put(INVOICE_DATE_TOOLTIP, getInvoiceDateTooltip());
		i18KeyMap.put(DUEDATE_TOOLTIP, getDueDateTooltip());
		i18KeyMap.put(COMPANY_TOOLTIP, getCompanyTooltip());
		i18KeyMap.put(COMPANY_CODE_TOOLTIP, getCompanyCodeTooltip());
		i18KeyMap.put(COUNTRY_TOOLTIP, getCountryTooltip());
		i18KeyMap.put(AMOUNT_INCLUDING_TAXES_TOOLTIP, getAmountIncludingTaxesTooltip());
		i18KeyMap.put(WITH_HOLDING_TAX_TOOLTIP, getWithHoldingTaxTooltip());
		i18KeyMap.put(STATUS_TOOLTIP, getStatusTooltip());
		i18KeyMap.put(INVOICE_NO_TOOLTIP, getInvoiceNoTooltip());
		i18KeyMap.put(SUPPLIER_TOOLTIP, getSupplierTooltip());
		i18KeyMap.put(SUPPLIER_CODE_TOOLTIP, getSupplierCodeTooltip());
		i18KeyMap.put(PO_NO_TOOLTIP, getPoNoTooltip());
		i18KeyMap.put(LAST_90_DAYS, getLast90Days());
		i18KeyMap.put(OTHER, getOther());
		i18KeyMap.put(FROM, getFrom());
		i18KeyMap.put(TO, getTo());
		i18KeyMap.put(INVALID_DATE, getInvalidDate());
		i18KeyMap.put(INVALID_DATE_RANGE, getInvalidDateRange());
		i18KeyMap.put(CONFIRMED, getConfirmed());
		i18KeyMap.put(FILTER_SELECTED, getFilterSelected());		

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
		
		paymentsFromToDateGapInMonths = invoiceStatusService.getFromToDateGapInMonthsVal();
	}

	public String getI18nKeys() {
		return i18nKeys;
	}

	public String getFetcherror() {
		return fetcherror;
	}

	public String getMultipono() {
		return multipono;
	}

	public String getPaginationPrev() {
		return paginationPrev;
	}

	public String getPaginationNext() {
		return paginationNext;
	}

	public String getPaid() {
		return paid;
	}

	public int getPaymentsFromToDateGapInMonths() {
		return paymentsFromToDateGapInMonths;
	}

	/**
	 * Get valid url to Payment Details URL
	 * @return mapped url.
	 */
	public String getPaymentDetailsURL() {
		return LinkUtil.getValidLink(resource, paymentDetailsURL);
	}

	public String getShowHideColumns() {
		return showHideColumns;
	}

	public String getColumns() {
		return columns;
	}

	public String getSelectAll() {
		return selectAll;
	}

	public String getApplyFilter() {
		return applyFilter;
	}

	public String getInvoiceDateTooltip() {
		return invoiceDateTooltip;
	}

	public String getDueDateTooltip() {
		return dueDateTooltip;
	}

	public String getCompanyTooltip() {
		return companyTooltip;
	}

	public String getCompanyCodeTooltip() {
		return companyCodeTooltip;
	}

	public String getCountryTooltip() {
		return countryTooltip;
	}

	public String getAmountIncludingTaxesTooltip() {
		return amountIncludingTaxesTooltip;
	}

	public String getStatusTooltip() {
		return statusTooltip;
	}

	public String getInvoiceNoTooltip() {
		return invoiceNoTooltip;
	}

	public String getSupplierTooltip() {
		return supplierTooltip;
	}

	public String getSupplierCodeTooltip() {
		return supplierCodeTooltip;
	}

	public String getPoNoTooltip() {
		return poNoTooltip;
	}

	public String getWithHoldingTaxTooltip() {
		return withHoldingTaxTooltip;
	}


	public InvoiceStatusService getInvoiceStatusService() {
		return invoiceStatusService;
	}

	public String getLast90Days() {
		return last90Days;
	}

	public String getOther() {
		return other;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getInvalidDate() {
		return invalidDate;
	}

	public String getInvalidDateRange() {
		return invalidDateRange;
	}


	public String getConfirmed() {
		return confirmed;
	}

	public String getFilterSelected() {
		return filterSelected;
	}

}