
package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Bean class for Record response json
 */
public class Record {
    
    @SerializedName("documentNumber")
    @Expose
    private String documentNumber;
    
    @SerializedName("documentType")
    @Expose
    private String documentType;
    
    @SerializedName("invoiceStatus")
    @Expose
    private String invoiceStatus;
    
    @SerializedName("invoiceReference")
    @Expose
    private String invoiceReference;
    
    @SerializedName("poNumber")
    @Expose
    private String poNumber;
    
    @SerializedName("docDate")
    @Expose
    private String docDate;
    
    @SerializedName("dueDate")
    @Expose
    private String dueDate;
    
    @SerializedName("clearedDate")
    @Expose
    private String clearedDate;
    
    @SerializedName("currency")
    @Expose
    private String currency;
    
    @SerializedName("dueDays")
    @Expose
    private String dueDays;
    
    @SerializedName("orgAmount")
    @Expose
    private String orgAmount;
    
    @SerializedName("remAmount")
    @Expose
    private String remAmount;
    
    @SerializedName("salesOffice")
    @Expose
    private String salesOffice;
    
    @SerializedName("companyCode")
    @Expose
    private String companyCode;
    
    @SerializedName("salesLocalData")
    @Expose
    private String salesLocalData;
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    public String getInvoiceStatus() {
        return invoiceStatus;
    }
    
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
    
    public String getInvoiceReference() {
        return invoiceReference;
    }
    
    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }
    
    public String getPoNumber() {
        return poNumber;
    }
    
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
    
    public String getDocDate() {
        return docDate;
    }
    
    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }
    
    public String getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getClearedDate() {
        return clearedDate;
    }
    
    public void setClearedDate(String clearedDate) {
        this.clearedDate = clearedDate;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getDueDays() {
        return dueDays;
    }
    
    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }
    
    public String getOrgAmount() {
        return orgAmount;
    }
    
    public void setOrgAmount(String orgAmount) {
        this.orgAmount = orgAmount;
    }
    
    public String getRemAmount() {
        return remAmount;
    }
    
    public void setRemAmount(String remAmount) {
        this.remAmount = remAmount;
    }
    
    public String getSalesOffice() {
        return salesOffice;
    }
    
    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }
    
    public String getCompanyCode() {
        return companyCode;
    }
    
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    
    public String getSalesLocalData() {
        return salesLocalData;
    }
    
    public void setSalesLocalData(String salesLocalData) {
        this.salesLocalData = salesLocalData;
    }
}
