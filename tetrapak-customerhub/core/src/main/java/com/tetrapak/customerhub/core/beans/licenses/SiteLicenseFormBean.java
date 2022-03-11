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
    @SerializedName("advanced-units")
    private String numberOfAdvancedUnit;

    public String getNameOfSite() {
        return nameOfSite;
    }

    public void setNameOfSite(String nameOfSite) {
        this.nameOfSite = nameOfSite;
    }

    public String getLocationOfSite() {
        return locationOfSite;
    }

    public void setLocationOfSite(String locationOfSite) {
        this.locationOfSite = locationOfSite;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPlcType() {
        return plcType;
    }

    public void setPlcType(String plcType) {
        this.plcType = plcType;
    }

    public String getHmiType() {
        return hmiType;
    }

    public void setHmiType(String hmiType) {
        this.hmiType = hmiType;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getNumberOfBasicUnit() {
        return numberOfBasicUnit;
    }

    public void setNumberOfBasicUnit(String numberOfBasicUnit) {
        this.numberOfBasicUnit = numberOfBasicUnit;
    }

    public String getNumberOfAdvancedUnit() {
        return numberOfAdvancedUnit;
    }

    public void setNumberOfAdvancedUnit(String numberOfAdvancedUnit) {
        this.numberOfAdvancedUnit = numberOfAdvancedUnit;
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
