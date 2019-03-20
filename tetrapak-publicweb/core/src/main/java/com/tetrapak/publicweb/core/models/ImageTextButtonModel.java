package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.tetrapak.publicweb.core.utils.LinkUtils;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTextButtonModel {

	@Inject
	private String title;

	@Inject
	private String subtitle;

	@Inject
	private String description;

	@Inject
	private String ctaLinkTexti18n;

	@Inject
	private String ctaLinkURL;
	
	@Inject
	private String processingHotspotUrl;

	@Inject
	private Boolean openProcessingHotspotInNewWindow;
	
	@Inject
	private String packagingHotspotUrl;

	@Inject
	private Boolean openPackagingHotspotInNewWindow;

	@Inject
	private String servicesHotspotUrl;
	
	@Inject
	private Boolean openServicesHotspotInNewWindow;

	@PostConstruct
	protected void init() {
		if (StringUtils.isNotEmpty(ctaLinkURL)) {
			ctaLinkURL = LinkUtils.sanitizeLink(ctaLinkURL);
		}
		if (StringUtils.isNotEmpty(processingHotspotUrl)) {
			processingHotspotUrl = LinkUtils.sanitizeLink(processingHotspotUrl);
		}
		if (StringUtils.isNotEmpty(packagingHotspotUrl)) {
			packagingHotspotUrl = LinkUtils.sanitizeLink(packagingHotspotUrl);
		}
		if (StringUtils.isNotEmpty(servicesHotspotUrl)) {
			servicesHotspotUrl = LinkUtils.sanitizeLink(servicesHotspotUrl);
		}

	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getDescription() {
		return description;
	}

	public String getCtaLinkTexti18n() {
		return ctaLinkTexti18n;
	}

	public String getCtaLinkURL() {
		return ctaLinkURL;
	}

	public String getProcessingHotspotUrl() {
		return processingHotspotUrl;
	}

	public Boolean getOpenProcessingHotspotInNewWindow() {
		return openProcessingHotspotInNewWindow;
	}

	public String getPackagingHotspotUrl() {
		return packagingHotspotUrl;
	}

	public Boolean getOpenPackagingHotspotInNewWindow() {
		return openPackagingHotspotInNewWindow;
	}

	public String getServicesHotspotUrl() {
		return servicesHotspotUrl;
	}

	public Boolean getOpenServicesHotspotInNewWindow() {
		return openServicesHotspotInNewWindow;
	}
	
}
