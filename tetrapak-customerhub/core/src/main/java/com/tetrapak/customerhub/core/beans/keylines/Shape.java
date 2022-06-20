
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

    @Override
    public String toString() {
	return "Shape [name=" + name + ", openings=" + openings + ", volumes=" + volumes + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((openings == null) ? 0 : openings.hashCode());
	result = prime * result + ((volumes == null) ? 0 : volumes.hashCode());
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
	Shape other = (Shape) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (openings == null) {
	    if (other.openings != null)
		return false;
	} else if (!openings.equals(other.openings))
	    return false;
	if (volumes == null) {
	    if (other.volumes != null)
		return false;
	} else if (!volumes.equals(other.volumes))
	    return false;
	return true;
    }
    
    

}
