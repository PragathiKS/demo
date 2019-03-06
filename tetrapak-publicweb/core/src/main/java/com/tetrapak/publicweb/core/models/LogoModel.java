package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LogoModel {

	@Self
	private Resource resource;

	@Inject
	private String imagePath;

	@Inject
	private String imageAltI18n;

	@Inject
	private String imageLink;

	@PostConstruct
	protected void init() {
		
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
