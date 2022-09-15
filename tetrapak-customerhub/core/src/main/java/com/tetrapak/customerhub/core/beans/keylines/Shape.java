
package com.tetrapak.customerhub.core.beans.keylines;

import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shape {

    @SerializedName("name")
    @Expose
    private String name;
    
    @SerializedName("volumes")
    @Expose
    private Set<Volume> volumes = null;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<Volume> getVolumes() {
	return volumes;
    }

    public void setVolumes(Set<Volume> volumes) {
	this.volumes = volumes;
    }

}
