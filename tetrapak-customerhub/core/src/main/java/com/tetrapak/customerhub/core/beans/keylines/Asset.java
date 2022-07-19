
package com.tetrapak.customerhub.core.beans.keylines;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Asset {

    @SerializedName("keyname")
    @Expose
    private String keyname;
    @SerializedName("assetname")
    @Expose
    private String assetname;
    @SerializedName("assetpath")
    @Expose
    private String assetpath;

    public String getKeyname() {
	return keyname;
    }

    public void setKeyname(String keyname) {
	this.keyname = keyname;
    }

    public String getAssetname() {
	return assetname;
    }

    public void setAssetname(String assetname) {
	this.assetname = assetname;
    }

    public String getAssetpath() {
	return assetpath;
    }

    public void setAssetpath(String assetpath) {
	this.assetpath = assetpath;
    }

}
