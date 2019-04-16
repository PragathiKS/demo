
package com.tetrapak.customerhub.core.beans.oderdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class OrderDetailsData {

    @SerializedName("orderDetails")
    @Expose
    private OrderDetails orderDetails;
    @SerializedName("customerSupportCenter")
    @Expose
    private CustomerSupportCenter customerSupportCenter;
    @SerializedName("deliveryList")
    @Expose
    private List<DeliveryList> deliveryList = null;
    @SerializedName("orderSummary")
    @Expose
    private List<OrderSummary> orderSummary = null;

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public CustomerSupportCenter getCustomerSupportCenter() {
        return customerSupportCenter;
    }

    public void setCustomerSupportCenter(CustomerSupportCenter customerSupportCenter) {
        this.customerSupportCenter = customerSupportCenter;
    }

    public List<DeliveryList> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(List<DeliveryList> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public List<OrderSummary> getOrderSummary() {
        return orderSummary;
    }

    public void setOrderSummary(List<OrderSummary> orderSummary) {
        this.orderSummary = orderSummary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("orderDetails", orderDetails).append("customerSupportCenter", customerSupportCenter).append("deliveryList", deliveryList).append("orderSummary", orderSummary).toString();
    }

}
