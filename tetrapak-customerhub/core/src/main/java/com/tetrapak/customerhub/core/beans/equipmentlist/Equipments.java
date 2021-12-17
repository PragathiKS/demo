package com.tetrapak.customerhub.core.beans.equipmentlist;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

public class Equipments {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("countryName")
    @Expose
    private String countryName;

    @SerializedName("customer")
    @Expose
    private String customer;

    @SerializedName("customerNumber")
    @Expose
    private String customerNumber;

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("lineName")
    @Expose
    private String lineName;

    @SerializedName("equipmentStatus")
    @Expose
    private String equipmentStatus;

    @SerializedName("isSecondhand")
    @Expose
    private String isSecondhand;

    @SerializedName("equipmentType")
    @Expose
    private String equipmentType;

    @SerializedName("businessType")
    @Expose
    private String businessType;

    @SerializedName("equipmentTypeDesc")
    @Expose
    private String equipmentTypeDesc;

    @SerializedName("functionalLocation")
    @Expose
    private String functionalLocation;

    @SerializedName("functionalLocationDesc")
    @Expose
    private String functionalLocationDesc;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    @SerializedName("siteName")
    @Expose
    private String siteName;

    @SerializedName("siteDesc")
    @Expose
    private String siteDesc;

    @SerializedName("permanentVolumeConversion")
    @Expose
    private String permanentVolumeConversion;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("machineSystem")
    @Expose
    private String machineSystem;

    @SerializedName("material")
    @Expose
    private String material;

    @SerializedName("materialDesc")
    @Expose
    private String materialDesc;

    @SerializedName("manufacturerModelNumber")
    @Expose
    private String manufacturerModelNumber;

    @SerializedName("manufacturerSerialNumber")
    @Expose
    private String manufacturerSerialNumber;

    @SerializedName("superiorEquipment")
    @Expose
    private String superiorEquipment;

    @SerializedName("superiorEquipmentName")
    @Expose
    private String superiorEquipmentName;

    @SerializedName("superiorEquipmentSerialNumber")
    @Expose
    private String superiorEquipmentSerialNumber;

    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;

    @SerializedName("manufacturerCountry")
    @Expose
    private String manufacturerCountry;

    @SerializedName("constructionYear")
    @Expose
    private String constructionYear;

    @SerializedName("customerWarrantyStartDate")
    @Expose
    private String customerWarrantyStartDate;

    @SerializedName("customerWarrantyEndDate")
    @Expose
    private String customerWarrantyEndDate;

    @SerializedName("equipmentCategory")
    @Expose
    private String equipmentCategory;

    @SerializedName("equipmentCategoryDesc")
    @Expose
    private String equipmentCategoryDesc;

    @SerializedName("eofsConfirmationDate")
    @Expose
    private String eofsConfirmationDate;

    @SerializedName("eofsValidFromDate")
    @Expose
    private String eofsValidFromDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getIsSecondhand() {
        return isSecondhand;
    }

    public void setIsSecondhand(String isSecondhand) {
        this.isSecondhand = isSecondhand;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentTypeDesc() {
        return equipmentTypeDesc;
    }

    public void setEquipmentTypeDesc(String equipmentTypeDesc) {
        this.equipmentTypeDesc = equipmentTypeDesc;
    }

    public String getFunctionalLocation() {
        return functionalLocation;
    }

    public void setFunctionalLocation(String functionalLocation) {
        this.functionalLocation = functionalLocation;
    }

    public String getFunctionalLocationDesc() {
        return functionalLocationDesc;
    }

    public void setFunctionalLocationDesc(String functionalLocationDesc) {
        this.functionalLocationDesc = functionalLocationDesc;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDesc() {
        return siteDesc;
    }

    public void setSiteDesc(String siteDesc) {
        this.siteDesc = siteDesc;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getManufacturerModelNumber() {
        return manufacturerModelNumber;
    }

    public void setManufacturerModelNumber(String manufacturerModelNumber) {
        this.manufacturerModelNumber = manufacturerModelNumber;
    }

    public String getManufacturerSerialNumber() {
        return manufacturerSerialNumber;
    }

    public void setManufacturerSerialNumber(String manufacturerSerialNumber) {
        this.manufacturerSerialNumber = manufacturerSerialNumber;
    }

    public String getSuperiorEquipment() {
        return superiorEquipment;
    }

    public void setSuperiorEquipment(String superiorEquipment) {
        this.superiorEquipment = superiorEquipment;
    }


    public String getSuperiorEquipmentName() {
        return superiorEquipmentName;
    }

    public void setSuperiorEquipmentName(String superiorEquipmentName) {
        this.superiorEquipmentName = superiorEquipmentName;
    }

    public String getSuperiorEquipmentSerialNumber() {
        return superiorEquipmentSerialNumber;
    }

    public void setSuperiorEquipmentSerialNumber(String superiorEquipmentSerialNumber) {
        this.superiorEquipmentSerialNumber = superiorEquipmentSerialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerCountry() {
        return manufacturerCountry;
    }

    public void setManufacturerCountry(String manufacturerCountry) {
        this.manufacturerCountry = manufacturerCountry;
    }

    public String getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(String constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getCustomerWarrantyStartDate() {
        return customerWarrantyStartDate;
    }

    public void setCustomerWarrantyStartDate(String customerWarrantyStartDate) {
        this.customerWarrantyStartDate = customerWarrantyStartDate;
    }

    public String getCustomerWarrantyEndDate() {
        return customerWarrantyEndDate;
    }

    public void setCustomerWarrantyEndDate(String customerWarrantyEndDate) {
        this.customerWarrantyEndDate = customerWarrantyEndDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public String getEquipmentCategoryDesc() {
        return equipmentCategoryDesc;
    }

    public void setEquipmentCategoryDesc(String equipmentCategoryDesc) {
        this.equipmentCategoryDesc = equipmentCategoryDesc;
    }

    public String getEofsConfirmationDate() {
        return eofsConfirmationDate;
    }

    public void setEofsConfirmationDate(String eofsConfirmationDate) {
        this.eofsConfirmationDate = eofsConfirmationDate;
    }

    public String getEofsValidFromDate() {
        return eofsValidFromDate;
    }

    public void setEofsValidFromDate(String eofsValidFromDate) {
        this.eofsValidFromDate = eofsValidFromDate;
    }

    public String getCustomer ( ) {
        return customer;
    }

    public void setCustomer (String customer) {
        this.customer = customer;
    }

    public String getCustomerNumber ( ) {
        return customerNumber;
    }

    public void setCustomerNumber (String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName ( ) {
        return customerName;
    }

    public void setCustomerName (String customerName) {
        this.customerName = customerName;
    }

    public String getLocation ( ) {
        return location;
    }

    public void setLocation (String location) {
        this.location = location;
    }

	@Override
	public String toString() {
		return tidyCSVOutput(id) + CustomerHubConstants.COMMA + tidyCSVOutput(countryCode) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(countryName) + CustomerHubConstants.COMMA + tidyCSVOutput(customer)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(customerNumber) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(customerName) + CustomerHubConstants.COMMA + tidyCSVOutput(location)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(lineName) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(equipmentStatus) + CustomerHubConstants.COMMA + tidyCSVOutput(isSecondhand)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(equipmentType) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(businessType) + CustomerHubConstants.COMMA + tidyCSVOutput(equipmentTypeDesc)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(functionalLocation) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(functionalLocationDesc) + CustomerHubConstants.COMMA + tidyCSVOutput(serialNumber)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(siteName) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(siteDesc) + CustomerHubConstants.COMMA + tidyCSVOutput(permanentVolumeConversion)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(position) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(machineSystem) + CustomerHubConstants.COMMA + tidyCSVOutput(material)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(materialDesc) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(manufacturerModelNumber) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(manufacturerSerialNumber) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(superiorEquipment) + CustomerHubConstants.COMMA + tidyCSVOutput(superiorEquipmentName)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(superiorEquipmentSerialNumber) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(manufacturer) + CustomerHubConstants.COMMA + tidyCSVOutput(manufacturerCountry)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(constructionYear) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(customerWarrantyStartDate) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(customerWarrantyEndDate) + CustomerHubConstants.COMMA + tidyCSVOutput(equipmentCategory)
				+ CustomerHubConstants.COMMA + tidyCSVOutput(equipmentCategoryDesc) + CustomerHubConstants.COMMA
				+ tidyCSVOutput(eofsConfirmationDate) + CustomerHubConstants.COMMA + tidyCSVOutput(eofsValidFromDate)
				+ CustomerHubConstants.NEWLINE;
	}

	public String tidyCSVOutput(String field) {
		if (Objects.isNull(field)) {
			return StringUtils.EMPTY;
		}
		return CustomerHubConstants.QUOTE_ESCAPED + field + CustomerHubConstants.QUOTE_ESCAPED;
	}
}
