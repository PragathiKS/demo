package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Bean class for Params response json
 */
public class Params {

    @SerializedName("customerData")
    @Expose
    private CustomerData customerData;

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("soaDate")
    @Expose
    private String soaDate;

    @SerializedName("status")
    @Expose
    private Status status;

    @SerializedName("documentType")
    @Expose
    private DocumentType documentType;

    @SerializedName("documentNumber")
    @Expose
    private String documentNumber;

    @SerializedName("statusList")
    @Expose
    private List<DocumentType> statusList;

    @SerializedName("documentTypeList")
    @Expose
    private List<DocumentType> documentTypeList;

    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSoaDate() {
        return soaDate;
    }

    public void setSoaDate(String soaDate) {
        this.soaDate = soaDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public List<DocumentType> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<DocumentType> statusList) {
        this.statusList = statusList;
    }

    public List<DocumentType> getDocumentTypeList() {
        return documentTypeList;
    }

    public void setDocumentTypeList(List<DocumentType> documentTypeList) {
        this.documentTypeList = documentTypeList;
    }
}
