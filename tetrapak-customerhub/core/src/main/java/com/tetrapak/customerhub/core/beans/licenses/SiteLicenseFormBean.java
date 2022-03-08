package com.tetrapak.customerhub.core.beans.licenses;

public class SiteLicenseFormBean {

    private String nameOfSite;
    private String locationOfSite;
    private String application;
    private String plcType;
    private String hmiType;
    private String mesType;
    private String numberOfBasicUnit;
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
}
