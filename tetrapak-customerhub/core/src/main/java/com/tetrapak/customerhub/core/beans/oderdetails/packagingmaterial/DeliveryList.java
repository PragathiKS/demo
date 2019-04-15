package com.tetrapak.customerhub.core.beans.oderdetails.packagingmaterial;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetrapak.customerhub.core.beans.oderdetails.DeliveryAddress;
import com.tetrapak.customerhub.core.beans.oderdetails.InvoiceAddress;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DeliveryList {

    @SerializedName("deliveryOrder")
    @Expose
    private String deliveryOrder;
    @SerializedName("deliveryNumber")
    @Expose
    private String deliveryNumber;
    @SerializedName("ETD")
    @Expose
    private String eTD;
    @SerializedName("deliveryAddress")
    @Expose
    private DeliveryAddress deliveryAddress;
    @SerializedName("invoiceAddress")
    @Expose
    private InvoiceAddress invoiceAddress;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;
    @SerializedName("productPlace")
    @Expose
    private String productPlace;
    @SerializedName("requestedDelivery")
    @Expose
    private String requestedDelivery;

    public String getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(String deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getETD() {
        return eTD;
    }

    public void setETD(String eTD) {
        this.eTD = eTD;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public InvoiceAddress getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(InvoiceAddress invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProductPlace() {
        return productPlace;
    }

    public void setProductPlace(String productPlace) {
        this.productPlace = productPlace;
    }

    public String getRequestedDelivery() {
        return requestedDelivery;
    }

    public void setRequestedDelivery(String requestedDelivery) {
        this.requestedDelivery = requestedDelivery;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("deliveryOrder", deliveryOrder).append("deliveryNumber", deliveryNumber).append("eTD", eTD).append("deliveryAddress", deliveryAddress).append("invoiceAddress", invoiceAddress).append("products", products).append("deliveryStatus", deliveryStatus).append("productPlace", productPlace).append("requestedDelivery", requestedDelivery).toString();
    }

}
