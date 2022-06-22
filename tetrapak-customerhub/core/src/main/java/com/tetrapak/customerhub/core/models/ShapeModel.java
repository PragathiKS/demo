package com.tetrapak.customerhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShapeModel {

    /** The alt. */
    @ValueMapValue
    private String shape;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    public String getShape() {
	return shape;
    }

    public void setShape(String shape) {
	this.shape = shape;
    }

    public String getFileReference() {
	return fileReference;
    }

    public void setFileReference(String fileReference) {
	this.fileReference = fileReference;
    }

    public String getAlt() {
	return alt;
    }

    public void setAlt(String alt) {
	this.alt = alt;
    }

}
