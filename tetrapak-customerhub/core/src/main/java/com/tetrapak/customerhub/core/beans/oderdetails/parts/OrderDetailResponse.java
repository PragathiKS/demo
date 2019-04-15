
package com.tetrapak.customerhub.core.beans.oderdetails.parts;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetrapak.customerhub.core.beans.oderdetails.CustomerSupportCenter;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderDetailResponse {

    @SerializedName("orderDetails")
    @Expose
    private OrderDetails orderDetails;
    @SerializedName("customerSupportCenter")
    @Expose
    private CustomerSupportCenter customerSupportCenter;
    @SerializedName("deliveryList")
    @Expose
    private List<DeliveryList> deliveryList = null;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("orderDetails", orderDetails).append("customerSupportCenter", customerSupportCenter).append("deliveryList", deliveryList).toString();
    }

}
