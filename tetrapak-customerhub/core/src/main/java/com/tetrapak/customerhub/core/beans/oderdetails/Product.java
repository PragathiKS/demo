package com.tetrapak.customerhub.core.beans.oderdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Bean class for product json response
 */
public class Product {

    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("orderQuantity")
    @Expose
    private String orderQuantity;
    @SerializedName("deliveredQuantity")
    @Expose
    private String deliveredQuantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("remainingQuantity")
    @Expose
    private String remainingQuantity;
    @SerializedName("orderNumber")
    @Expose
    private Integer orderNumber;
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("ETA")
    @Expose
    private String eTA;
    @SerializedName("unitPrice")
    @Expose
    private String unitPrice;
    @SerializedName("materialCode")
    @Expose
    private String materialCode;
    @SerializedName("SKU")
    @Expose
    private String sKU;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(String deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getETA() {
        return eTA;
    }

    public void setETA(String eTA) {
        this.eTA = eTA;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getSKU() {
        return sKU;
    }

    public void setSKU(String sKU) {
        this.sKU = sKU;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("productName", productName)
                .append("orderQuantity", orderQuantity).append("deliveredQuantity", deliveredQuantity)
                .append("price", price).append("remainingQuantity", remainingQuantity)
                .append("orderNumber", orderNumber).append("productID", productID)
                .append("weight", weight).append("eTA", eTA).append("unitPrice", unitPrice)
                .append("materialCode", materialCode).append("sKU", sKU).toString();
    }

}
