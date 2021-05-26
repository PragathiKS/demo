package com.tetrapak.publicweb.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class BannerModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerModel {

    /** The resource. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The banner type. */
    @ValueMapValue
    private String bannerType;

    /** The subtitle. */
    @ValueMapValue
    private String subtitle;

    /** The heading tag. */
    @ValueMapValue
    private String headingTag;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The text. */
    @ValueMapValue
    private String text;

    /** The link label. */
    @ValueMapValue
    private String linkLabel;

    /** The link path. */
    @ValueMapValue
    private String linkPath;

    /** The file reference. */
    @ValueMapValue
    private String fileReference;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The pw card theme. */
    @ValueMapValue
    private String pwCardTheme;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The image crop. */
    @ValueMapValue
    private String imageCrop;

    /** The enable forms. */
    @ValueMapValue
    private String formType;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

    /** The Constant HERO_TEST. */
    private static final String HERO_TEST = "hero";

    /** The Constant SKY_BLUE. */
    private static final String SKY_BLUE = "sky-blue";

    /** The enable softcoversion. */
    @ValueMapValue
    private String enableSoftcoversion;

    /**
     * Gets the banner type.
     *
     * @return the banner type
     */
    public String getBannerType() {
        return bannerType;
    }

    /**
     * Gets the subtitle.
     *
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Gets the heading tag.
     *
     * @return the heading tag
     */
    public String getHeadingTag() {
        return headingTag;
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
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the link label.
     *
     * @return the link label
     */
    public String getLinkLabel() {
        return linkLabel;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkPath() {
        return LinkUtils.sanitizeLink(linkPath, request);
    }

    /**
     * Gets the file reference.
     *
     * @return the file reference
     */
    public String getFileReference() {
        return fileReference;
    }

    /**
     * Gets the alt.
     *
     * @return the alt
     */
    public String getAlt() {
        return alt;
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
     * Gets the pw card theme.
     *
     * @return the pw card theme
     */
    public String getPwCardTheme() {
        if(bannerType.equalsIgnoreCase(HERO_TEST)){
            return SKY_BLUE;
        }
        return pwCardTheme;
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
     * Gets the image crop.
     *
     * @return the image crop
     */
    public String getImageCrop() {
        return imageCrop;
    }


    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNoneEmpty(linkPath)) {
            assetName = StringUtils.substringAfterLast(linkPath, FORWARD_SLASH);
        }
        return assetName;
    }
    /**
     * Gets the enable form.
     *
     * @return the enable form
     */
    public String getFormType() {
        return formType;
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
     * Gets the enable softcoversion.
     *
     * @return the enable softcoversion
     */
    public String getEnableSoftcoversion() {
        return enableSoftcoversion;
    }
}
