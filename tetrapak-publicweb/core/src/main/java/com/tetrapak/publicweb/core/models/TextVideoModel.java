package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.tetrapak.publicweb.core.utils.LinkUtils;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextVideoModel {

	@Inject
	private String title;

	@Inject
	private String description;

	@Inject
	private String linkTexti18n;

	@Inject
	private String linkURL;

	@Inject
	private String videoSource;

	@Inject
	private String youtubeVideoID;
	
	@Inject
	private String damVideoPath;
	
	private String youtubeEmbedURL;
	
	@PostConstruct
	protected void init() {
		if (StringUtils.isNotEmpty(linkURL)) {
			linkURL = LinkUtils.sanitizeLink(linkURL);
		}
		
		youtubeEmbedURL = "https://www.youtube.com/embed/" + youtubeVideoID; 
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getLinkTexti18n() {
		return linkTexti18n;
	}

	public String getLinkURL() {
		return linkURL;
	}

	public String getVideoSource() {
		return videoSource;
	}

	public String getYoutubeVideoID() {
		return youtubeVideoID;
	}
	
	public String getYoutubeEmbedURL() {
		return youtubeEmbedURL;
	}

	public String getDamVideoPath() {
		return damVideoPath;
	}

}
