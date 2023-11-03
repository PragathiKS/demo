package com.tetrapak.customerhub.core.beans.licenses;

import com.google.gson.annotations.SerializedName;

/**
 * Site License Form Bean
 *
 * @author Pathak Ankit
 */
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
}