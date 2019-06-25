package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Bean class for Customer Data response json
 */
public class CustomerData {

    @SerializedName("customerNumber")
    @Expose
    private String customerNumber;

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("info")
    @Expose
    private Info info;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
