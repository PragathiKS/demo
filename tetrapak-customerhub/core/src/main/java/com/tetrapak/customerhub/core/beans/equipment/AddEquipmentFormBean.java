package com.tetrapak.customerhub.core.beans.equipment;

import com.google.gson.annotations.SerializedName;

/**
 * Bean that maps Add equipment form request to json;
 */
public class AddEquipmentFormBean {

    @SerializedName("addEquipmentSerialNumber") private String[] equipmentSerialNumber;

    @SerializedName("addEquipmentCountry") private String[] equipmentCountry;

    @SerializedName("addEquipmentSite") private String[] equipmentSite;

    @SerializedName("addEquipmentLine") private String[] equipmentLine;

    @SerializedName("addEquipmentPosition") private String[] equipmentPosition;

    @SerializedName("addEquipmentEquipmentStatus") private String[] equipmentStatus;

    @SerializedName("addEquipmentMachineSystem") private String[] equipmentMachineSystem;

    @SerializedName("addEquipmentEquipmentDescription") private String[] equipmentDescription;

    @SerializedName("addEquipmentManufactureModelNumber") private String[] equipmentManufactureModelNumber;

    @SerializedName("addEquipmentManufactureOfAsset") private String[] equipmentManufactureOfAsset;

    @SerializedName("addEquipmentCountryOfManufacture") private String[] equipmentCountryOfManufacture;

    @SerializedName("addEquipmentConstructionYear") private String[] equipmentConstructionYear;

    @SerializedName("addEquipmentComments") private String[] equipmentComments;

    public String getEquipmentSerialNumber() {
        return equipmentSerialNumber[0];
    }

    public String getEquipmentCountry() {
        return equipmentCountry[0];
    }

    public String getEquipmentSite() {
        return equipmentSite[0];
    }

    public String getEquipmentLine() {
        return equipmentLine[0];
    }

    public String getEquipmentPosition() {
        return equipmentPosition[0];
    }

    public String getEquipmentStatus() {
        return equipmentStatus[0];
    }

    public String getEquipmentMachineSystem() {
        return equipmentMachineSystem[0];
    }

    public String getEquipmentDescription() {
        return equipmentDescription[0];
    }

    public String getEquipmentManufactureModelNumber() {
        return equipmentManufactureModelNumber[0];
    }

    public String getEquipmentManufactureOfAsset() {
        return equipmentManufactureOfAsset[0];
    }

    public String getEquipmentCountryOfManufacture() {
        return equipmentCountryOfManufacture[0];
    }

    public String getEquipmentConstructionYear() {
        return equipmentConstructionYear[0];
    }

    public String getEquipmentComments() {
        return equipmentComments[0];
    }

}
