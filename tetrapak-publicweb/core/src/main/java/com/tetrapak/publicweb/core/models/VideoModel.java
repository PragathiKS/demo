package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


/**
 * This is a model class for video component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoModel {

    @Inject
    private String anchorId;

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
    private String pwTheme;

    @PostConstruct
    protected void init() {
        if (youtubeVideoID != null) {
            youtubeEmbedURL = "https://www.youtube.com/embed/" + youtubeVideoID;
        }
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

    public String getPwTheme() {
        return pwTheme;
    }

    public String getAnchorId() {
        return anchorId;
    }
}
