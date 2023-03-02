package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.SerializedName;

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
}
