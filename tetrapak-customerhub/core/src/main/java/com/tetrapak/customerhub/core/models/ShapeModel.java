package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

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

    private String title;

    private String name;

    @Self
    @Via("resourceResolver")
    TagManager tagManager;

    @PostConstruct
    protected void init() {
	if (StringUtils.isNotBlank(shape)) {
	    Tag tag = tagManager.resolve(shape);
	    if (null != tag) {
		title = tag.getTitle();
		name = tag.getName();
	    }
	}

    }

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

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
