package com.tetrapak.customerhub.core.beans.oderdetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("totalProductsForQuery")
    @Expose
    private String totalProductsForQuery;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("carrier")
    @Expose
    private String carrier;
    @SerializedName("carrierTrackingID")
    @Expose
    private String carrierTrackingID;
    @SerializedName("totalWeight")
    @Expose
    private String totalWeight;
    @SerializedName("totalPricePreVAT")
    @Expose
    private String totalPricePreVAT;
    @SerializedName("totalVAT")
    @Expose
    private String totalVAT;
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

    public String getTotalProductsForQuery() {
        return totalProductsForQuery;
    }

    public void setTotalProductsForQuery(String totalProductsForQuery) {
        this.totalProductsForQuery = totalProductsForQuery;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrierTrackingID() {
        return carrierTrackingID;
    }

    public void setCarrierTrackingID(String carrierTrackingID) {
        this.carrierTrackingID = carrierTrackingID;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public String getTotalPricePreVAT() {
        return totalPricePreVAT;
    }

    public void setTotalPricePreVAT(String totalPricePreVAT) {
        this.totalPricePreVAT = totalPricePreVAT;
    }

    public String getTotalVAT() {
        return totalVAT;
    }

    public void setTotalVAT(String totalVAT) {
        this.totalVAT = totalVAT;
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
		return new ToStringBuilder(this).append("deliveryOrder", deliveryOrder).append("deliveryNumber", deliveryNumber)
				.append("eTD", eTD).append("deliveryAddress", deliveryAddress).append("invoiceAddress", invoiceAddress)
				.append("totalProductsForQuery", totalProductsForQuery).append("products", products)
				.append("orderNumber", orderNumber).append("carrier", carrier)
				.append("carrierTrackingID", carrierTrackingID).append("totalWeight", totalWeight)
				.append("totalPricePreVAT", totalPricePreVAT).append("totalVAT", totalVAT)
				.append("deliveryStatus", deliveryStatus).append("productPlace", productPlace)
				.append("requestedDelivery", requestedDelivery).toString();
    }

}
