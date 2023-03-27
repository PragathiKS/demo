package com.tetrapak.customerhub.core.beans.rebuildingkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RKMeta {

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("skip")
    @Expose
    private int skip;

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("nextLink")
    @Expose
    private String nextLink;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNextLink() {
        return nextLink;
    }

    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
                .append("count", count)
                .append("skip", skip)
                .append("total", total)
                .append("version",version)
                .append("nextLink",nextLink)
                .toString();
    }

}
