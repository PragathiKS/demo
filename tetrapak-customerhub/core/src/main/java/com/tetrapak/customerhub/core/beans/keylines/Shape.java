
package com.tetrapak.customerhub.core.beans.keylines;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shape {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("openings")
    @Expose
    private Set<Opening> openings = null;
    @SerializedName("volumes")
    @Expose
    private Set<Volume> volumes = null;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<Opening> getOpenings() {
	return openings;
    }

    public void setOpenings(Set<Opening> openings) {
	this.openings = openings;
    }

    public Set<Volume> getVolumes() {
	return volumes;
    }

    public void setVolumes(Set<Volume> volumes) {
	this.volumes = volumes;
    }

}
