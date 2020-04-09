package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextImageModel {

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

    /** The image path. */
    @ValueMapValue
    private String imagePath;

    /** The image alt i18n. */
    @ValueMapValue
    private String imageAltI18n;

    /** The link text i18n. */
    @ValueMapValue
    private String linkTexti18n;

    /** The link URL. */
    @ValueMapValue
    private String linkURL;

    /** The target blank. */
    @ValueMapValue
    private String targetBlank;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The pw link theme. */
    @ValueMapValue
    private String pwLinkTheme;

    /** The pw padding. */
    @ValueMapValue
    private String pwPadding;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(linkURL)) {
            linkURL = LinkUtils.sanitizeLink(linkURL);
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
     * Gets the image path.
     *
     * @return the image path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Gets the image alt I 18 n.
     *
     * @return the image alt I 18 n
     */
    public String getImageAltI18n() {
        return imageAltI18n;
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
     * Gets the pw link theme.
     *
     * @return the pw link theme
     */
    public String getPwLinkTheme() {
        return pwLinkTheme;
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
}
