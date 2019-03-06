package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleContainerModel {

	@Inject
	private String numberOfcolumns;

	@Inject
	private String titleI18n;

	@Inject
	private String titleAlignment;

	@PostConstruct
	protected void init() {
		
	}

	public String getNumberOfcolumns() {
		return numberOfcolumns;
	}

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getTitleAlignment() {
		return titleAlignment;
	}

}
