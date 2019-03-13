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

	@Inject
	private Boolean showBox;

	private String[] totalColumns;

	@PostConstruct
	protected void init() {
		if (numberOfcolumns != null) {
			if (numberOfcolumns.equalsIgnoreCase("one-column")) {
				totalColumns = new String[] { "col1" };

			} else if (numberOfcolumns.equalsIgnoreCase("two-column")) {
				totalColumns = new String[] { "col1", "col2" };

			} else {
				totalColumns = new String[] { "col1", "col2", "col3" };

			}
		}

	}

	public String[] getTotalColumns() {
		return totalColumns;
	}

	public String getTitleI18n() {
		return titleI18n;
	}

	public String getTitleAlignment() {
		return titleAlignment;
	}

	public Boolean getShowBox(){
		return showBox;
	}

}
