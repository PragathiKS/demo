package com.tetrapak.customerhub.core.beans.equipment;

/**
 * Bean class used to build json request to request add equipment on api.
 */
public class AddEquipmentApiRequestBean {

    private String reportedBy;
    private String position;
    private String country;
    private String site;
    private String line;
    private String manufactureModelNumber;
    private String equipmentType;
    private String equipmentProduce;
    private String entityId;
    private String serialNumber;
    private String materialNumber;
    private String machineSystem;
    private String equipmentDesciption;
    private String manufacture;
    private String manufactureCountry;
    private String manufactureYear;
    private String userStatus;
    private String comment;
    private String source;
    private int numberOfAttachments;

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getManufactureModelNumber() {
        return manufactureModelNumber;
    }

    public void setManufactureModelNumber(String manufactureModelNumber) {
        this.manufactureModelNumber = manufactureModelNumber;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentProduce() {
        return equipmentProduce;
    }

    public void setEquipmentProduce(String equipmentProduce) {
        this.equipmentProduce = equipmentProduce;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMachineSystem() {
        return machineSystem;
    }

    public void setMachineSystem(String machineSystem) {
        this.machineSystem = machineSystem;
    }

    public String getEquipmentDesciption() {
        return equipmentDesciption;
    }

    public void setEquipmentDesciption(String equipmentDesciption) {
        this.equipmentDesciption = equipmentDesciption;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getManufactureCountry() {
        return manufactureCountry;
    }

    public void setManufactureCountry(String manufactureCountry) {
        this.manufactureCountry = manufactureCountry;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getNumberOfAttachments() {
        return numberOfAttachments;
    }

    public void setNumberOfAttachments(int numberOfAttachments) {
        this.numberOfAttachments = numberOfAttachments;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
