package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextVideoModel {

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @OSGiService
    private DynamicMediaService dynamicMediaService;

    @ValueMapValue
    private String anchorId;

    @ValueMapValue
    private String anchorTitle;

    @ValueMapValue
    private String subTitle;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String linkTexti18n;

    @ValueMapValue
    private String linkURL;

    @ValueMapValue
    private Boolean targetBlank;

    @ValueMapValue
    private String videoSource;

    @ValueMapValue
    private String youtubeVideoID;

    @ValueMapValue
    private String damVideoPath;

    private String youtubeEmbedURL;

    @ValueMapValue
    private String thumbnailPath;

    @ValueMapValue
    private String thumbnailAltText;

    @ValueMapValue
    private String pwTheme;

    @ValueMapValue
    private String pwButtonTheme;

    @ValueMapValue
    private String pwDisplay;

    private static final String YOUTUBE_URL_PREFIX = "https://www.youtube.com/embed/";
    private static final String AUTHOR = "author";

    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(linkURL)) {
            linkURL = LinkUtils.sanitizeLink(linkURL);
        }

        if (youtubeVideoID != null) {
            youtubeEmbedURL = YOUTUBE_URL_PREFIX + youtubeVideoID;
        }

        if (!slingSettingsService.getRunModes().contains(AUTHOR) && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
        }
    }

    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }

    public String getSubTitle() {
        return subTitle;
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

    public String getThumbnailAltText() {
        return thumbnailAltText;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public String getPwDisplay() {
        return pwDisplay;
    }
}
