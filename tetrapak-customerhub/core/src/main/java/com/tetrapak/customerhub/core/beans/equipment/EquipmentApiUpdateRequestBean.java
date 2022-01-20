package com.tetrapak.customerhub.core.beans.equipment;

import java.util.List;

/**
 * Bean class used to build json request to request update api.
 */
public class EquipmentApiUpdateRequestBean {

    private String serialNumber;
    private String equipmentNumber;
    private String comment;
    private String source;
    private String reportedBy;
    private List<EquipmentMetaData> metaDatas;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
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

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public List<EquipmentMetaData> getMetaDatas() {
        return metaDatas;
    }

    public void setMetaDatas(List<EquipmentMetaData> metaDatas) {
        this.metaDatas = metaDatas;
    }
}
