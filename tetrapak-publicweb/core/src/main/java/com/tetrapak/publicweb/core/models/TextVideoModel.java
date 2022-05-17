package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;

/**
 * The Class TextVideoModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextVideoModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The resource. */
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

    /** The sub title. */
    @ValueMapValue
    private String subTitle;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The link texti 18 n. */
    @ValueMapValue
    private String linkTexti18n;

    /** The link URL. */
    @ValueMapValue
    private String linkURL;

    /** The target blank. */
    @ValueMapValue
    private String targetBlank;

    /** The video source. */
    @ValueMapValue
    private String videoSource;

    /** The youtube video ID. */
    @ValueMapValue
    private String youtubeVideoID;

    /** The dam video path. */
    @ValueMapValue
    private String damVideoPath;

    /** The youtube embed URL. */
    private String youtubeEmbedURL;

    /** The thumbnail path. */
    @ValueMapValue
    private String thumbnailPath;

    /** The thumbnail alt text. */
    @ValueMapValue
    private String thumbnailAltText;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;


    /** The enable forms. */
    @ValueMapValue
    private String formType;

    /** The Constant YOUTUBE_URL_PREFIX. */
    private static final String YOUTUBE_URL_PREFIX = "https://www.youtube.com/embed/";

    /** The Constant AUTHOR. */
    private static final String AUTHOR = "author";

    /** The enable softconversion. */
    @ValueMapValue
    private String enableSoftcoversion;
    
    /** The original DAM video path. */
    private String originalDamVideoPath;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        originalDamVideoPath = damVideoPath;
        final ValueMap vMap = resource.getValueMap();
        enableSoftcoversion = vMap.get("enableSoftcoversion", StringUtils.EMPTY);
        formType = vMap.get("formType", StringUtils.EMPTY);
        if (StringUtils.isNotEmpty(linkURL)) {
            linkURL = LinkUtils.sanitizeLink(linkURL, request);
        }

        if (youtubeVideoID != null) {
            youtubeEmbedURL = YOUTUBE_URL_PREFIX
                    + (youtubeVideoID.contains("?") ? (youtubeVideoID + "&" + PWConstants.ENABLE_JS_API)
                            : (youtubeVideoID + "?" + PWConstants.ENABLE_JS_API));
        }

        if (!slingSettingsService.getRunModes().contains(AUTHOR) && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(request.getResourceResolver(), damVideoPath,
                    dynamicMediaService);
        }
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
     * Gets the sub title.
     *
     * @return the sub title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the link texti 18 n.
     *
     * @return the link texti 18 n
     */
    public String getLinkTexti18n() {
        return linkTexti18n;
    }

    /**
     * Gets the link URL.
     *
     * @return the link URL
     */
    public String getLinkURL() {
        return linkURL;
    }

    /**
     * Gets the target blank.
     *
     * @return the target blank
     */
    public String getTargetBlank() {
        return targetBlank;
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
     * Gets the thumbnail alt text.
     *
     * @return the thumbnail alt text
     */
    public String getThumbnailAltText() {
        return thumbnailAltText;
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
     * Gets the pw button theme.
     *
     * @return the pw button theme
     */
    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    /**
     * Gets the pw padding.
     *
     * @return the pw padding
     */
    public String getPwPadding() {
        return pwPadding;
    }

    /**
     * Gets the pw display.
     *
     * @return the pw display
     */
    public String getPwDisplay() {
        return pwDisplay;
    }

    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        return LinkUtils.getAssetName(linkURL);
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
     * Gets the soft conversion data.
     *
     * @return the soft conversion data
     */
    public SoftConversionModel getSoftConversionData() {
        return request.adaptTo(SoftConversionModel.class);
    }


    /**
     * Gets the subscription form data.
     *
     * @return the subscription form data
     */
    public SubscriptionFormModel getSubscriptionData() {
        return request.adaptTo(SubscriptionFormModel.class);
    }

    /**
     * Gets the enable Form Type.
     *
     * @return the enable formType
     */

    public String getFormType() {
        return formType;
    }


    /**
     * Gets the enable softcoversion.
     *
     * @return the enable softcoversion
     */
    public String getEnableSoftcoversion() {
        return enableSoftcoversion;
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
