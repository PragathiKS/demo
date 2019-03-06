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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleNoImageModel {

	@Self
	private Resource resource;

	@Inject
	private String contentType;

	@Inject
	private String articlepath;

	@Inject
	private String title;
	
	@Inject
	private String vanityDescription;

	@Inject
	private String ctaTexti18nKey;

	@Inject
	private Boolean openInNewWindow;

	@PostConstruct
	protected void init() {
		ResourceResolver resolver = resource.getResourceResolver();
		PageManager pageManager = resolver.adaptTo(PageManager.class);
		
		Resource jcrContentResource = currentPage.getContentResource();
		
		if ("automatic".equals(contentType)) {
			title = "";
			vanityDescription = "";
			ctaTexti18nKey = "";
			openInNewWindow = false;
			
			Page landingPage = pageManager.getPage(articlePath);
			if (landingPage != null) {
				Resource jcrContentResource = landingPage.getContentResource();
				LandingPageModel landingPageModel = jcrContentResource.adaptTo(LandingPageModel.class);
				if (landingPageModel != null) {
					title = landingPageModel.getTitle();
					vanityDescription = landingPageModel.getVanityDescription();
					ctaTexti18nKey = landingPageModel.getCtaTexti18nKey();
					openInNewWindow = landingPageModel.isOpenInNewWindow();
				}
			}
				 
		}
	}

	public Resource getResource() {
		return resource;
	}

	public String getTitle() {
		return title;
	}

	public String getVanityDescription() {
		return vanityDescription;
	}

	public String getCtaTexti18nKey() {
		return ctaTexti18nKey;
	}
	
	public Boolean isOpenInNewWindow() {
		return openInNewWindow;
	}
}
