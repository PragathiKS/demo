package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.google.gson.Gson;

/**
 * Model class for order search component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderDetailsModel {

	@Self
	private Resource resource;

	private String i18nKeys;

	public String getI18nKeys() {
		return i18nKeys;
	}

	@PostConstruct
	protected void init() {
		ValueMap propMap = resource.getValueMap();
		i18nKeys = new Gson().toJson(propMap);
	}
}
