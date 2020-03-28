package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;


/**
 * This is a model class for video component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoModel {

    @ValueMapValue
    private String anchorId;

    @ValueMapValue
    private String anchorTitle;

    @ValueMapValue
    private String videoSource;

    @ValueMapValue
    private String youtubeVideoID;

    @ValueMapValue
    private String damVideoPath;

    @ValueMapValue
    private String thumbnailPath;

    private String youtubeEmbedURL;

    @ValueMapValue
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

    public String getAnchorTitle() {
        return anchorTitle;
    }
}
