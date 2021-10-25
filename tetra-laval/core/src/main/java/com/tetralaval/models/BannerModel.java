package com.tetralaval.models;

import com.tetralaval.constants.TLConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetralaval.utils.LinkUtils;

/**
 * The Class BannerModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The headingTag. */
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
    private String imageAltText;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button colour. */
    @ValueMapValue
    private String buttonColor;

    /** The image gradient. */
    @ValueMapValue
    private String imageGradient;

    /** The layout. */
    @ValueMapValue
    private String layout;

    /** The background colour colour. */
    @ValueMapValue
    private String backgroundColor;

    /** The text colour. */
    @ValueMapValue
    private String textColor;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The width. */
    @ValueMapValue
    private String width;

    /** The anchor linkType. */
    private String linkType;

    /**
     * Gets the headingTag.
     *
     * @return the headingTag
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
     * Gets the image alt text.
     *
     * @return the image alt text
     */
    public String getImageAltText() {
        return imageAltText;
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
     * Gets the button colour.
     *
     * @return the button colour
     */
    public String getButtonColor() {
        return buttonColor;
    }

    /**
     * Gets the image gradient.
     *
     * @return the image gradient
     */
    public String getImageGradient() {
        return imageGradient;
    }

    /**
     * Gets the layout.
     *
     * @return the layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Gets the colour.
     *
     * @return the background colour
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets the text colour.
     *
     * @return the text colour
     */
    public String getTextColor() {
        return textColor;
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
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNoneEmpty(linkPath)) {
            assetName = StringUtils.substringAfterLast(linkPath, TLConstants.SLASH);
        }
        return assetName;
    }

    /**
     * Gets the link width.
     *
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * Gets the link path.
     *
     * @return the link path
     */
    public String getLinkType() {
        return LinkUtils.linkType(this.linkPath);
    }
}
