package com.tetrapak.publicweb.core.models;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.beans.FooterBean;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LogoModel {

	@Self
	private Resource resource;

	@Inject
	private String headerDummyText;

	@PostConstruct
	protected void init() {
		
	}

	public String getHeaderDummyText() {
		return headerDummyText;
	}
}
