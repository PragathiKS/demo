package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RebuildingKits {

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("lineCode")
    @Expose
    private String lineCode;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("rkNumber")
    @Expose
    private String rkNumber;

    @SerializedName("rkDesc")
    @Expose
    private String rkDesc;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    @SerializedName("equipmentDesc")
    @Expose
    private String equipmentDesc;

    @SerializedName("implStatus")
    @Expose
    private String implStatus;

    @SerializedName("permanentVolumeConversion")
    @Expose
    private String permanentVolumeConversion;

    @SerializedName("implStatusDate")
    @Expose
    private String implStatusDate;


    @SerializedName("implDate")
    @Expose
    private String implDate;


    @SerializedName("implDeadline")
    @Expose
    private String implDeadline;


    @SerializedName("equipmentStatus")
    @Expose
    private String equipmentStatus;
    
    @SerializedName("equipmentStatusDesc")
    @Expose
    private String equipmentStatusDesc;

    @SerializedName("equipmentStructure")
    @Expose
    private String equipmentStructure;

    @SerializedName("serviceOrder")
    @Expose
    private String serviceOrder;

    @SerializedName("order")
    @Expose
    private String order;

    @SerializedName("rebuildingKitStatus")
    @Expose
    private String rebuildingKitStatus;

    @SerializedName("technicalBulletin")
    @Expose
    private String technicalBulletin;

    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;

    @SerializedName("releaseDateFirst")
    @Expose
    private String releaseDateFirst;

    @SerializedName("rkTypeDesc")
    @Expose
    private String rkTypeDesc;

    @SerializedName("plannedDate")
    @Expose
    private String plannedDate;

    @SerializedName("mechanicalSkills")
    @Expose
    private String mechanicalSkills;

    @SerializedName("automationSkills")
    @Expose
    private String automationSkills;

    @SerializedName("customerNumber")
    @Expose
    private String customerNumber;

    @SerializedName("machineSystem")
    @Expose
    private String machineSystem;


    @SerializedName("machineSystemDesc")
    @Expose
    private String machineSystemDesc;

    @SerializedName("rkHandling")
    @Expose
    private String rkHandling;


    @SerializedName("equipmentMaterial")
    @Expose
    private String equipmentMaterial;

    @SerializedName("equipmentMaterialDesc")
    @Expose
    private String equipmentMaterialDesc;

    @SerializedName("equipmentType")
    @Expose
    private String equipmentType;
    
    @SerializedName("equipmentTypeDesc")
    @Expose
    private String equipmentTypeDesc;

    @SerializedName("equipmentNumber")
    @Expose
    private String equipmentNumber;

    @SerializedName("countryName")
    @Expose
    private String countryName;


    @SerializedName("lineName")
    @Expose
    private String lineName;

    @SerializedName("electricalSkills")
    @Expose
    private String electricalSkills;

    @SerializedName("rkTime")
    @Expose
    private String rkTime;

    @SerializedName("kpiExcl")
    @Expose
    private String kpiExcl;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }
    
    public String getEquipmentStatusDesc() {
        return equipmentStatusDesc;
    }
    
    public void setEquipmentStatusDesc(String equipmentStatusDesc) {
        this.equipmentStatusDesc = equipmentStatusDesc;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }


    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentTypeDesc(String equipmentTypeDesc) {
        this.equipmentTypeDesc = equipmentTypeDesc;
    }

    public String getEquipmentTypeDesc() {
        return equipmentTypeDesc;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPermanentVolumeConversion() {
        return permanentVolumeConversion;
    }

    public void setPermanentVolumeConversion(String permanentVolumeConversion) {
        this.permanentVolumeConversion = permanentVolumeConversion;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMachineSystem() {
        return machineSystem;
    }

    public void setMachineSystem(String machineSystem) {
        this.machineSystem = machineSystem;
    }

    public String getRkNumber() {
        return rkNumber;
    }

    public void setRkNumber(String rkNumber) {
        this.rkNumber = rkNumber;
    }

    public String getRkDesc() {
        return rkDesc;
    }

    public void setRkDesc(String rkDesc) {
        this.rkDesc = rkDesc;
    }

    public String getImplDate() {
        return implDate;
    }

    public void setImplDate(String implDate) {
        this.implDate = implDate;
    }

    public String getImplStatus() {
        return implStatus;
    }

    public void setImplStatus(String implStatus) {
        this.implStatus = implStatus;
    }

    public String getImplStatusDate() {
        return implStatusDate;
    }

    public void setImplStatusDate(String implStatusDate) {
        this.implStatusDate = implStatusDate;
    }

    public String getRebuildingKitStatus() {
        return rebuildingKitStatus;
    }

    public void setRebuildingKitStatus(String rebuildingKitStatus) {
        this.rebuildingKitStatus = rebuildingKitStatus;
    }

    public String getReleaseDateFirst() {
        return releaseDateFirst;
    }

    public void setReleaseDateFirst(String releaseDateFirst) {
        this.releaseDateFirst = releaseDateFirst;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMechanicalSkills() {
        return mechanicalSkills;
    }

    public void setMechanicalSkills(String mechanicalSkills) {
        this.mechanicalSkills = mechanicalSkills;
    }

    public String getAutomationSkills() {
        return automationSkills;
    }

    public void setAutomationSkills(String automationSkills) {
        this.automationSkills = automationSkills;
    }

    public String getElectricalSkills() {
        return electricalSkills;
    }

    public void setElectricalSkills(String electricalSkills) {
        this.electricalSkills = electricalSkills;
    }

    public String getMachineSystemDesc() {
        return machineSystemDesc;
    }

    public void setMachineSystemDesc(String machineSystemDesc) {
        this.machineSystemDesc = machineSystemDesc;
    }

    public String getEquipmentDesc() {
        return equipmentDesc;
    }

    public void setEquipmentDesc(String equipmentDesc) {
        this.equipmentDesc = equipmentDesc;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getImplDeadline() {
        return implDeadline;
    }

    public void setImplDeadline(String implDeadline) {
        this.implDeadline = implDeadline;
    }

    public String getEquipmentStructure() {
        return equipmentStructure;
    }

    public void setEquipmentStructure(String equipmentStructure) {
        this.equipmentStructure = equipmentStructure;
    }

    public String getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(String serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTechnicalBulletin() {
        return technicalBulletin;
    }

    public void setTechnicalBulletin(String technicalBulletin) {
        this.technicalBulletin = technicalBulletin;
    }

    public String getRkTypeDesc() {
        return rkTypeDesc;
    }

    public void setRkTypeDesc(String rkTypeDesc) {
        this.rkTypeDesc = rkTypeDesc;
    }

    public String getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(String plannedDate) {
        this.plannedDate = plannedDate;
    }

    public String getRkHandling() {
        return rkHandling;
    }

    public void setRkHandling(String rkHandling) {
        this.rkHandling = rkHandling;
    }

    public String getEquipmentMaterial() {
        return equipmentMaterial;
    }

    public void setEquipmentMaterial(String equipmentMaterial) {
        this.equipmentMaterial = equipmentMaterial;
    }

    public String getEquipmentMaterialDesc() {
        return equipmentMaterialDesc;
    }

    public void setEquipmentMaterialDesc(String equipmentMaterialDesc) {
        this.equipmentMaterialDesc = equipmentMaterialDesc;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getRkTime() {
        return rkTime;
    }

    public void setRkTime(String rkTime) {
        this.rkTime = rkTime;
    }

    public String getKpiExcl() {
        return kpiExcl;
    }

    public void setKpiExcl(String kpiExcl) {
        this.kpiExcl = kpiExcl;
    }
}
