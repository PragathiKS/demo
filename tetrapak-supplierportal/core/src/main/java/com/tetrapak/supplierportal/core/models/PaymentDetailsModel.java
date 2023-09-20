package com.tetrapak.supplierportal.core.models;

import static com.tetrapak.supplierportal.core.constants.I18Constants.BANK_ACCOUNT;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COMPANY_INFO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.COUNTRY;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DETAILS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.DUE_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.EMPTY_PAYMENT_DETAILS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.EXPORT_TO_PDF;
import static com.tetrapak.supplierportal.core.constants.I18Constants.FETCH_ERROR;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_INFO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.INVOICE_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.MULTI_PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.NET_PAYABLE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAID_DATE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAYMENT_METHOD;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PAYMENT_TERM;
import static com.tetrapak.supplierportal.core.constants.I18Constants.PO_NO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.STATUS;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_CODE;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER_INFO;
import static com.tetrapak.supplierportal.core.constants.I18Constants.TAX;
import static com.tetrapak.supplierportal.core.constants.I18Constants.TOTAL_AMOUNT;
import static com.tetrapak.supplierportal.core.constants.I18Constants.WITH_HOLDING_TAX;
import static com.tetrapak.supplierportal.core.constants.I18Constants.SUPPLIER;
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
public class PaymentDetailsModel {
	
	/**
	 * The request.
	 */
	@Self
	private Resource resource;

	@ValueMapValue
	private String invoiceInfo;

	@ValueMapValue
	private String companyInfo;

	@ValueMapValue
	private String supplierInfo;

	@ValueMapValue
	private String details;

	@ValueMapValue
	private String invoiceNo;

	@ValueMapValue
	private String date;

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
	private String totalAmount;

	@ValueMapValue
	private String tax;

	@ValueMapValue
	private String withHoldingTaxes;

	@ValueMapValue
	private String netPayable;

	@ValueMapValue
	private String paymentTerm;

	@ValueMapValue
	private String dueDate;

	@ValueMapValue
	private String paidDate;

	@ValueMapValue
	private String bankAccount;

	@ValueMapValue
	private String paymentMethod;

	@ValueMapValue
	private String poNo;

	@ValueMapValue
	private String emptyPaymentDetails;
	
	
	private String exportToPdfURL;
	
	
	private String viewPaymentDataUrl;
	
	@ValueMapValue
	private String fetchError;
	
	@ValueMapValue
	private String multiPoNo;
	
	@ValueMapValue
	private String exportToPDF;
	

	/** The is publish environment. */
	private boolean isPublishEnvironment = Boolean.FALSE;

	/** The i18n keys. */
	private String i18nKeys;

	/** The service. */
	@OSGiService
	private APIGEEService service;
	
	/** The sling settings service. */
	@OSGiService
	private SlingSettingsService slingSettingsService;
	
	@PostConstruct
	public void init() {
		Map<String, Object> i18KeyMap = new HashMap<>();
		i18KeyMap.put(INVOICE_INFO, getInvoiceInfo());
		i18KeyMap.put(COMPANY_INFO, getCompanyInfo());
		i18KeyMap.put(SUPPLIER_INFO, getSupplierInfo());
		i18KeyMap.put(DETAILS, getDetails());
		i18KeyMap.put(INVOICE_NO, getInvoiceNo());
		i18KeyMap.put(DATE, getDate());
		i18KeyMap.put(STATUS, getStatus());
		i18KeyMap.put(COMPANY, getCompany());
		i18KeyMap.put(COMPANY_CODE, getCompanyCode());
		i18KeyMap.put(COUNTRY, getCountry());
		i18KeyMap.put(SUPPLIER, getSupplier());
		i18KeyMap.put(SUPPLIER_CODE, getSupplierCode());
		i18KeyMap.put(TOTAL_AMOUNT, getTotalAmount());
		i18KeyMap.put(TAX, getTax());
		i18KeyMap.put(WITH_HOLDING_TAX, getWithHoldingTaxes());
		i18KeyMap.put(NET_PAYABLE, getNetPayable());
		i18KeyMap.put(PAYMENT_TERM, getPaymentTerm());
		i18KeyMap.put(DUE_DATE, getDueDate());
		i18KeyMap.put(PAID_DATE, getPaidDate());
		i18KeyMap.put(BANK_ACCOUNT, getBankAccount());
		i18KeyMap.put(PAYMENT_METHOD, getPaymentMethod());
		i18KeyMap.put(PO_NO, getPoNo());
		i18KeyMap.put(EMPTY_PAYMENT_DETAILS, getEmptyPaymentDetails());
		i18KeyMap.put(FETCH_ERROR, getFetchError());
		i18KeyMap.put(MULTI_PO_NO, getMultiPoNo());
		i18KeyMap.put(EXPORT_TO_PDF, getExportToPDF());
		
		
		if (slingSettingsService.getRunModes().contains(SupplierPortalConstants.PUBLISH)) {
			isPublishEnvironment = Boolean.TRUE;
		}
		Gson gson = new Gson();
		i18nKeys = gson.toJson(i18KeyMap);
		exportToPdfURL = resource.getPath() + SupplierPortalConstants.EXPORT_TO_PDFURL;

		viewPaymentDataUrl = service.getApigeeServiceUrl()
				+ GlobalUtil.getSelectedApiMapping(service, SupplierPortalConstants.INVOICE_MAPPING);
	}

	public String getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(String invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}

	public String getSupplierInfo() {
		return supplierInfo;
	}

	public void setSupplierInfo(String supplierInfo) {
		this.supplierInfo = supplierInfo;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getWithHoldingTaxes() {
		return withHoldingTaxes;
	}

	public void setWithHoldingTaxes(String withHoldingTaxes) {
		this.withHoldingTaxes = withHoldingTaxes;
	}

	public String getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(String netPayable) {
		this.netPayable = netPayable;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getEmptyPaymentDetails() {
		return emptyPaymentDetails;
	}

	public void setEmptyPaymentDetails(String emptyPaymentDetails) {
		this.emptyPaymentDetails = emptyPaymentDetails;
	}

	public String getExportToPdfURL() {
		return exportToPdfURL;
	}

	public void setExportToPdfURL(String exportToPdfURL) {
		this.exportToPdfURL = exportToPdfURL;
	}

	public String getViewPaymentDataUrl() {
		return viewPaymentDataUrl;
	}

	public void setViewPaymentDataUrl(String viewPaymentDataUrl) {
		this.viewPaymentDataUrl = viewPaymentDataUrl;
	}

	public String getFetchError() {
		return fetchError;
	}

	public void setFetchError(String fetchError) {
		this.fetchError = fetchError;
	}

	public String getMultiPoNo() {
		return multiPoNo;
	}

	public void setMultiPoNo(String multiPoNo) {
		this.multiPoNo = multiPoNo;
	}

	public boolean isPublishEnvironment() {
		return isPublishEnvironment;
	}

	public void setPublishEnvironment(boolean isPublishEnvironment) {
		this.isPublishEnvironment = isPublishEnvironment;
	}

	public String getI18nKeys() {
		return i18nKeys;
	}

	public void setI18nKeys(String i18nKeys) {
		this.i18nKeys = i18nKeys;
	}

	public String getExportToPDF() {
		return exportToPDF;
	}

	public void setExportToPDF(String exportToPDF) {
		this.exportToPDF = exportToPDF;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
}
