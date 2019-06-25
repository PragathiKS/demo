package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Bean class for Summary response json
 */
public class Summary {

    @SerializedName("customer")
    @Expose
    private String customer;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("current")
    @Expose
    private String current;

    @SerializedName("overdue")
    @Expose
    private String overdue;

    @SerializedName("thirty")
    @Expose
    private String thirty;

    @SerializedName("sixty")
    @Expose
    private String sixty;

    @SerializedName("ninty")
    @Expose
    private String ninty;

    @SerializedName("nintyPlus")
    @Expose
    private String nintyPlus;

    @SerializedName("total")
    @Expose
    private String total;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public String getThirty() {
        return thirty;
    }

    public void setThirty(String thirty) {
        this.thirty = thirty;
    }

    public String getSixty() {
        return sixty;
    }

    public void setSixty(String sixty) {
        this.sixty = sixty;
    }

    public String getNinty() {
        return ninty;
    }

    public void setNinty(String ninty) {
        this.ninty = ninty;
    }

    public String getNintyPlus() {
        return nintyPlus;
    }

    public void setNintyPlus(String nintyPlus) {
        this.nintyPlus = nintyPlus;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
