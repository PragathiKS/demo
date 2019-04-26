
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
    @SerializedName("desc")
    @Expose
    private String desc;
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
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("orgAmount")
    @Expose
    private String orgAmount;
    @SerializedName("salesOffice")
    @Expose
    private String salesOffice;
    @SerializedName("salesLocalData")
    @Expose
    private String salesLocalData;
    @SerializedName("PONumber")
    @Expose
    private String pONumber;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(String orgAmount) {
        this.orgAmount = orgAmount;
    }

    public String getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getSalesLocalData() {
        return salesLocalData;
    }

    public void setSalesLocalData(String salesLocalData) {
        this.salesLocalData = salesLocalData;
    }

    public String getPONumber() {
        return pONumber;
    }

    public void setPONumber(String pONumber) {
        this.pONumber = pONumber;
    }

}
