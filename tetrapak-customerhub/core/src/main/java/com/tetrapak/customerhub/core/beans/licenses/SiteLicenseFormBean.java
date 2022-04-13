package com.tetrapak.customerhub.core.beans.licenses;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SiteLicenseFormBean {

    private String nameOfSite;
    private String locationOfSite;
    private String application;
    @SerializedName("plc-type")
    private String plcType;
    @SerializedName("hmi-type")
    private String hmiType;
    @SerializedName("mes-type")
    private String mesType;
    @SerializedName("basic-unit")
    private String numberOfBasicUnit;
    @SerializedName("advanced-unit")
    private String numberOfAdvancedUnit;

    public String getNameOfSite() {
        return nameOfSite;
    }

    public String getLocationOfSite() {
        return locationOfSite;
    }

    public String getApplication() {
        return application;
    }

    public String getPlcType() {
        return plcType;
    }

    public String getHmiType() {
        return hmiType;
    }

    public String getMesType() {
        return mesType;
    }

    public String getNumberOfBasicUnit() {
        return numberOfBasicUnit;
    }

    public String getNumberOfAdvancedUnit() {
        return numberOfAdvancedUnit;
    }

    public String toString(){
        return new ToStringBuilder(this).
                append("nameOfSite", nameOfSite).
                append("locationOfSite", locationOfSite).
                append("application", application).
                append("plcType", plcType).
                append("hmiType", hmiType).
                append("mesType", mesType).
                append("numberOfBasicUnit", numberOfBasicUnit).
                append("numberOfAdvancedUnit", numberOfAdvancedUnit).
                toString();
    }
}
