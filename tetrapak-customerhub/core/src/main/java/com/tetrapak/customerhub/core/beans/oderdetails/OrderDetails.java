package com.tetrapak.customerhub.core.beans.oderdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Bean class for order details json response
 */
public class OrderDetails {

    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerNumber")
    @Expose
    private String customerNumber;
    @SerializedName("purchaseOrderNumber")
    @Expose
    private String purchaseOrderNumber;
    @SerializedName("customerReference")
    @Expose
    private String customerReference;
    @SerializedName("placedOn")
    @Expose
    private String placedOn;
    @SerializedName("webRefID")
    @Expose
    private String webRefID;
    @SerializedName("status")
    @Expose
    private String status;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(String placedOn) {
        this.placedOn = placedOn;
    }

    public String getWebRefID() {
        return webRefID;
    }

    public void setWebRefID(String webRefID) {
        this.webRefID = webRefID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("orderNumber", orderNumber)
                .append("customerName", customerName).append("customerNumber", customerNumber)
                .append("purchaseOrderNumber", purchaseOrderNumber).append("customerReference", customerReference)
                .append("placedOn", placedOn).append("webRefID", webRefID).append("status", status).toString();
    }

}
