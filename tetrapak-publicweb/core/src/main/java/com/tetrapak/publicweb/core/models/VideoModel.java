package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;

/**
 * This is a model class for video component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoModel {

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @OSGiService
    private DynamicMediaService dynamicMediaService;

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

        if (!slingSettingsService.getRunModes().contains("author") && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
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

    /**
     * Gets the video name.
     *
     * @return the video name
     */
    public String getVideoName() {
        return LinkUtils.getAssetName(damVideoPath);
    }
}
