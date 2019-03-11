package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;

@Model(adaptables = Resource.class)
public class LogoModel {

	@Self
	private Resource resource;

	private String imagePath;
	private String imageAltI18n;
	private String imageLink;

	@PostConstruct
	protected void init() {
		InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
		imagePath = inheritanceValueMap1.getInherited("imagePath", String.class);
		imageAltI18n = inheritanceValueMap1.getInherited("imageAltI18n", String.class);
		imageLink = inheritanceValueMap1.getInherited("imageLink", String.class);

	}

	public Resource getResource() {
		return resource;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getImageAltI18n() {
		return imageAltI18n;
	}

	public String getImageLink() {
		return imageLink;
	}
}
