package com.tetrapak.customerhub.core.beans.equipment;

import com.google.gson.annotations.SerializedName;

/**
 * Bean that maps Add equipment form request to json;
 */
public class AddEquipmentFormBean {

    @SerializedName("addEquipmentSerialNumber")
    private String equipmentSerialNumber;

    @SerializedName("addEquipmentCountry")
    private String equipmentCountry;

    @SerializedName("addEquipmentSite")
    private String equipmentSite;

    @SerializedName("addEquipmentLine")
    private String equipmentLine;

    @SerializedName("addEquipmentPosition")
    private String equipmentPosition;

    @SerializedName("addEquipmentEquipmentStatus")
    private String equipmentStatus;

    @SerializedName("addEquipmentMachineSystem")
    private String equipmentMachineSystem;

    @SerializedName("addEquipmentEquipmentDescription")
    private String equipmentDescription;

    @SerializedName("addEquipmentManufactureModelNumber")
    private String equipmentManufactureModelNumber;

    @SerializedName("addEquipmentManufactureOfAsset")
    private String equipmentManufactureOfAsset;

    @SerializedName("addEquipmentCountryOfManufacture")
    private String equipmentCountryOfManufacture;

    @SerializedName("addEquipmentConstructionYear")
    private String equipmentConstructionYear;

    @SerializedName("addEquipmentComments")
    private String equipmentComments;

    public String getEquipmentSerialNumber() {
        return equipmentSerialNumber;
    }

    public String getEquipmentCountry() {
        return equipmentCountry;
    }

    public String getEquipmentSite() {
        return equipmentSite;
    }

    public String getEquipmentLine() {
        return equipmentLine;
    }

    public String getEquipmentPosition() {
        return equipmentPosition;
    }

    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public String getEquipmentMachineSystem() {
        return equipmentMachineSystem;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public String getEquipmentManufactureModelNumber() {
        return equipmentManufactureModelNumber;
    }

    public String getEquipmentManufactureOfAsset() {
        return equipmentManufactureOfAsset;
    }

    public String getEquipmentCountryOfManufacture() {
        return equipmentCountryOfManufacture;
    }

    public String getEquipmentConstructionYear() {
        return equipmentConstructionYear;
    }

    public String getEquipmentComments() {
        return equipmentComments;
    }
}
