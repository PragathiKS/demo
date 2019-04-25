
package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("acountNo")
    @Expose
    private String acountNo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("address")
    @Expose
    private String address;

    public String getAcountNo() {
        return acountNo;
    }

    public void setAcountNo(String acountNo) {
        this.acountNo = acountNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
