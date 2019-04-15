package com.tetrapak.customerhub.core.beans.oderdetails.packagingmaterial;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderSummary {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("orderQuantity")
    @Expose
    private String orderQuantity;
    @SerializedName("deliveredQuantity")
    @Expose
    private String deliveredQuantity;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("product", product).append("orderQuantity", orderQuantity).append("deliveredQuantity", deliveredQuantity).toString();
    }

}
