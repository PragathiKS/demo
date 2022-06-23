
package com.tetrapak.customerhub.core.beans.keylines;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Keylines {

    @SerializedName("shapes")
    @Expose
    private List<Shape> shapes = null;
    @SerializedName("assets")
    @Expose
    private List<Asset> assets = null;

    public List<Shape> getShapes() {
	return shapes;
    }

    public void setShapes(List<Shape> shapes) {
	this.shapes = shapes;
    }

    public List<Asset> getAssets() {
	return assets;
    }

    public void setAssets(List<Asset> assets) {
	this.assets = assets;
    }

    @Override
    public String toString() {
	return "Keylines [shapes=" + shapes + ", assets=" + assets + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((assets == null) ? 0 : assets.hashCode());
	result = prime * result + ((shapes == null) ? 0 : shapes.hashCode());
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
	Keylines other = (Keylines) obj;
	if (assets == null) {
	    if (other.assets != null) {
		return false;
	    }
	} else if (!assets.equals(other.assets))
	    return false;
	if (shapes == null) {
	    if (other.shapes != null) {
		return false;
	    }
	} else if (!shapes.equals(other.shapes))
	    return false;
	return true;
    }

}
