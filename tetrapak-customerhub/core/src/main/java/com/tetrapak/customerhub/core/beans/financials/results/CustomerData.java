
package com.tetrapak.customerhub.core.beans.financials.results;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerData {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("info")
    @Expose
    private Info info;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
