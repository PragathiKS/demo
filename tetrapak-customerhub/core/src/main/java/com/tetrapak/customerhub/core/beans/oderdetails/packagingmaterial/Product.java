package com.tetrapak.customerhub.core.beans.oderdetails.packagingmaterial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    @SerializedName("materialCode")
    @Expose
    private String materialCode;
    @SerializedName("SKU")
    @Expose
    private String sKU;
    @SerializedName("orderNumber")
    @Expose
    private long orderNumber;

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

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("productName", productName).append("orderQuantity", orderQuantity).append("deliveredQuantity", deliveredQuantity).append("price", price).append("remainingQuantity", remainingQuantity).append("materialCode", materialCode).append("sKU", sKU).append("orderNumber", orderNumber).toString();
    }

}