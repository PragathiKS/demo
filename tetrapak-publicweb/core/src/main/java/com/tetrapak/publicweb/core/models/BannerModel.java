package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class BannerModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The banner type. */
    @ValueMapValue
    private String bannerType;

    /** The subtitle. */
    @ValueMapValue
    private String subtitle;

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

    /** The link target. */
    @ValueMapValue
    private String linkTarget;

    /** The pw link themes. */
    @ValueMapValue
    private String pwLinkTheme;

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

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

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
        return LinkUtils.sanitizeLink(linkPath);
    }

    /**
     * Gets the link target.
     *
     * @return the link target
     */
    public String getLinkTarget() {
        return linkTarget;
    }

    /**
     * Gets the pw link themes.
     *
     * @return the pw link themes
     */
    public String getPwLinkTheme() {
        return pwLinkTheme;
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

}
