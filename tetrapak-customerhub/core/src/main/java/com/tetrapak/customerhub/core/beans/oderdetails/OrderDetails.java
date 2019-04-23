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
    private Integer customerNumber;
    @SerializedName("purchaseOrderNumber")
    @Expose
    private Integer purchaseOrderNumber;
    @SerializedName("customerReference")
    @Expose
    private Integer customerReference;
    @SerializedName("placedOn")
    @Expose
    private String placedOn;
    @SerializedName("webRefID")
    @Expose
    private Integer webRefID;
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

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Integer getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(Integer purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public Integer getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(Integer customerReference) {
        this.customerReference = customerReference;
    }

    public String getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(String placedOn) {
        this.placedOn = placedOn;
    }

    public Integer getWebRefID() {
        return webRefID;
    }

    public void setWebRefID(Integer webRefID) {
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
