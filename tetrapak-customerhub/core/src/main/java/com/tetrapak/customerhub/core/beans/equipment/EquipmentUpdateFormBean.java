package com.tetrapak.customerhub.core.beans.equipment;

import org.apache.commons.lang3.StringUtils;

/**
 * Bean that maps Edit equipment form request to json;
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

    /**
     * Creates metadata entry for Country.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getCountryMetadata() {
        return new EquipmentMetaData("Country", oldCountry, country);
    }

    /**
     * Creates metadata entry for Location.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getLocationMetadata() {
        return new EquipmentMetaData("Location", oldLocation, location);
    }

    /**
     * Creates metadata entry for Site.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getSiteMetadata() {
        return new EquipmentMetaData("Site", oldSiteName, siteName);
    }

    /**
     * Creates metadata entry for Line.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getLineMetadata() {
        return new EquipmentMetaData("Line", oldLineName, lineName);
    }

    /**
     * Creates metadata entry for Status.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getStatusMetadata() {
        return new EquipmentMetaData("Status", oldEquipmentStatus, equipmentStatus);
    }

    /**
     * Creates metadata entry for Position.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getPositionMetadata() {
        return new EquipmentMetaData("Position", oldPosition, position);
    }

    /**
     * Creates metadata entry for Description.
     *
     * @return EquipmentMetaData bean.
     */
    public EquipmentMetaData getDescriptionMetadata() {
        return new EquipmentMetaData("Description", oldEquipmentTypeDesc, equipmentTypeDesc);
    }
}
