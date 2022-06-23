
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

    @Override
    public String toString() {
	return "Asset [keyname=" + keyname + ", assetname=" + assetname + ", assetpath=" + assetpath + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((assetname == null) ? 0 : assetname.hashCode());
	result = prime * result + ((assetpath == null) ? 0 : assetpath.hashCode());
	result = prime * result + ((keyname == null) ? 0 : keyname.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Asset other = (Asset) obj;
	if (assetname == null) {
	    if (other.assetname != null) {
		return false;
	    }
	} else if (!assetname.equals(other.assetname))
	    return false;
	if (assetpath == null) {
	    if (other.assetpath != null) {
		return false;
	    }
	} else if (!assetpath.equals(other.assetpath))
	    return false;
	if (keyname == null) {
	    if (other.keyname != null) {
		return false;
	    }
	} else if (!keyname.equals(other.keyname))
	    return false;
	return true;
    }

}
