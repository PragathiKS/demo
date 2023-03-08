package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

public class ImplementationStatusUpdateBean {

    @SerializedName("serialnumber")
    private String serialNumber;

    @SerializedName("reportedrebuildingkit")
    private String reportedRebuildingKit;

    @SerializedName("reportedrebuildingkitname")
    private String reportedRebuildingKitName;

    @SerializedName("reportedby")
    private String reportedBy;

    @SerializedName("comment")
    private String comment;

    @SerializedName("currentstatus")
    private String currentStatus;

    @SerializedName("reportedstatus")
    private String reportedStatus;

    @SerializedName("date")
    private String date;

    @SerializedName("source")
    private String source;

    public boolean isValid() {
        return !StringUtils.isAnyBlank(reportedRebuildingKit);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReportedRebuildingKit() {
        return reportedRebuildingKit;
    }

    public void setReportedRebuildingKit(String reportedRebuildingKit) {
        this.reportedRebuildingKit = reportedRebuildingKit;
    }

    public String getReportedRebuildingKitName() {
        return reportedRebuildingKitName;
    }

    public void setReportedRebuildingKitName(String reportedRebuildingKitName) {
        this.reportedRebuildingKitName = reportedRebuildingKitName;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getReportedStatus() {
        return reportedStatus;
    }

    public void setReportedStatus(String reportedStatus) {
        this.reportedStatus = reportedStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
