package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.customerhub.core.utils.LinkUtil;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextVideoModel {

	@Self
	private Resource resource;
	
    @Inject
    private String title;

    @Inject
    private String description;

    @Inject
    private String linkTexti18n;

    @Inject
    private String linkURL;
    
    @Inject
    private Boolean targetBlank;

    @Inject
    private String videoSource;

    @Inject
    private String youtubeVideoID;

    @Inject
    private String damVideoPath;

    @Inject
    private String thumbnailPath;

    private String youtubeEmbedURL;

    @Inject
    private String textAlignment;

    @Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @Inject
    private String pwPadding;

    @Inject
    private String pwDisplay;

    @PostConstruct
    protected void init() {    
    	linkURL = LinkUtil.getValidLink(resource, linkURL);
        if (youtubeVideoID != null) {
            youtubeEmbedURL = "https://www.youtube.com/embed/" + youtubeVideoID;
        }
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
    
    public Boolean getTargetBlank() {
        return targetBlank;
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

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getTextAlignment() {
        return textAlignment;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public String getPwPadding() {
        return pwPadding;
    }

    public String getPwDisplay() {
        return pwDisplay;
    }
}
