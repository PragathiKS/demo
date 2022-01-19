package com.tetrapak.customerhub.core.beans.equipment;

import org.apache.commons.lang3.StringUtils;

/**
 * Metadata class used to create json for api request.
 */
public class EquipmentMetaData {

    private String metaDataName;
    private String metaDataActualValue;
    private String metaDataRequestedValue;

    public EquipmentMetaData(String metaDataName, String metaDataActualValue, String metaDataRequestedValue) {
        this.metaDataName = metaDataName;
        this.metaDataActualValue = metaDataActualValue;
        this.metaDataRequestedValue = metaDataRequestedValue;
    }

    public String getMetaDataName() {
        return metaDataName;
    }

    public String getMetaDataActualValue() {
        return metaDataActualValue;
    }

    public String getMetaDataRequestedValue() {
        return metaDataRequestedValue;
    }

    public boolean isChanged() {
        return !StringUtils.equals(metaDataActualValue, metaDataRequestedValue);
    }
}
