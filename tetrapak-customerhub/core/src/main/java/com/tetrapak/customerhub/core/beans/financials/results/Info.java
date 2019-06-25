package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Bean class for Info response json
 */
public class Info {
    
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    
    @SerializedName("name1")
    @Expose
    private String name1;
    
    @SerializedName("name2")
    @Expose
    private String name2;
    
    @SerializedName("street")
    @Expose
    private String street;
    
    @SerializedName("city")
    @Expose
    private String city;
    
    @SerializedName("state")
    @Expose
    private String state;
    
    @SerializedName("postalcode")
    @Expose
    private String postalcode;
    
    @SerializedName("country")
    @Expose
    private String country;
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    public String getName1() {
        return name1;
    }
    
    public void setName1(String name1) {
        this.name1 = name1;
    }
    
    public String getName2() {
        return name2;
    }
    
    public void setName2(String name2) {
        this.name2 = name2;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getPostalcode() {
        return postalcode;
    }
    
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}
