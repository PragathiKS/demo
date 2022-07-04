
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

}
