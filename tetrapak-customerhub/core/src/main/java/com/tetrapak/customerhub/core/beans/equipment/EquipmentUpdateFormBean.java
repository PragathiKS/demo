package com.tetrapak.customerhub.core.beans.equipment;

import org.apache.commons.lang3.StringUtils;

/**
 * Bean that maps Edit equipment request json;
 */
public class EquipmentUpdateFormBean {

    private String equipmentId;
    private String comments;

    private String country;
    private String oldCountry;

    private String equipmentStatus;
    private String oldEquipmentStatus;

    private String equipmentTypeDesc;
    private String oldEquipmentTypeDesc;

    private String lineName;
    private String oldLineName;

    private String location;
    private String oldLocation;

    private String position;
    private String oldPosition;

    private String siteName;
    private String oldSiteName;

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getComments() {
        return comments;
    }

    public boolean isValid() {
        return !StringUtils.isAnyBlank(equipmentId);
    }

    public EquipmentMetaData getCountryMetadata() {
        return new EquipmentMetaData("Country", oldCountry, country);
    }

    public EquipmentMetaData getLocationMetadata() {
        return new EquipmentMetaData("Location", oldLocation, location);
    }

    public EquipmentMetaData getSiteMetadata() {
        return new EquipmentMetaData("Site", oldSiteName, siteName);
    }

    public EquipmentMetaData getLineMetadata() {
        return new EquipmentMetaData("Line", oldLineName, lineName);
    }

    public EquipmentMetaData getStatusMetadata() {
        return new EquipmentMetaData("Status", oldEquipmentStatus, equipmentStatus);
    }

    public EquipmentMetaData getPositionMetadata() {
        return new EquipmentMetaData("Position", oldPosition, position);
    }

    public EquipmentMetaData getDescriptionMetadata() {
        return new EquipmentMetaData("Description", oldEquipmentTypeDesc, equipmentTypeDesc);
    }
}
