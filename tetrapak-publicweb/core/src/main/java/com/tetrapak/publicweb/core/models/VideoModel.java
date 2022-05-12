package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;

/**
 * This is a model class for video component.
 *
 * @author Nitin Kumar
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The dynamic media service. */
    @OSGiService
    private DynamicMediaService dynamicMediaService;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The video source. */
    @ValueMapValue
    private String videoSource;

    /** The youtube video ID. */
    @ValueMapValue
    private String youtubeVideoID;

    /** The dam video path. */
    @ValueMapValue
    private String damVideoPath;

    /** The thumbnail path. */
    @ValueMapValue
    private String thumbnailPath;

    /** The poster image alt text. */
    @ValueMapValue
    private String posterImageAltText;

    /** The youtube embed URL. */
    private String youtubeEmbedURL;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;
    
    /** The original DAM video path. */
    private String originalDamVideoPath;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
    	originalDamVideoPath = damVideoPath;
        if (youtubeVideoID != null) {
            youtubeEmbedURL = "https://www.youtube.com/embed/"
                    + (youtubeVideoID.contains("?") ? (youtubeVideoID + "&" + PWConstants.ENABLE_JS_API)
                            : (youtubeVideoID + "?" + PWConstants.ENABLE_JS_API));
        }

        if (!slingSettingsService.getRunModes().contains("author") && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(resource.getResourceResolver(), damVideoPath,
                    dynamicMediaService);
        }
    }

    /**
     * Gets the video source.
     *
     * @return the video source
     */
    public String getVideoSource() {
        return videoSource;
    }

    /**
     * Gets the youtube video ID.
     *
     * @return the youtube video ID
     */
    public String getYoutubeVideoID() {
        return youtubeVideoID;
    }

    /**
     * Gets the youtube embed URL.
     *
     * @return the youtube embed URL
     */
    public String getYoutubeEmbedURL() {
        return youtubeEmbedURL;
    }

    /**
     * Gets the dam video path.
     *
     * @return the dam video path
     */
    public String getDamVideoPath() {
        return damVideoPath;
    }

    /**
     * Gets the thumbnail path.
     *
     * @return the thumbnail path
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    /**
     * Gets the poster image alt text.
     *
     * @return the poster image alt text
     */
    public String getPosterImageAltText() {
        return posterImageAltText;
    }

    /**
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the anchor id.
     *
     * @return the anchor id
     */
    public String getAnchorId() {
        return anchorId;
    }

    /**
     * Gets the anchor title.
     *
     * @return the anchor title
     */
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

    /**
     * Gets the original video path.
     *
     * @return the original video path
     */
	public String getOriginalDamVideoPath() {
		return originalDamVideoPath;
	}
}
