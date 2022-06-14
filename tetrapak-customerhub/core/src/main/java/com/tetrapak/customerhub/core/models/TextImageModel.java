package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The Class TextImageModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextImageModel {

    /** The request */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The resource. */
    private Resource resource;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The sub title. */
    @ValueMapValue
    private String subTitle;

    /** The heading tag. */
    @ValueMapValue
    private String headingTag;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The image path. */
    @ValueMapValue
    private String imagePath;

    /** The image alt text. */
    @ValueMapValue
    private String imageAltText;

    @Inject
    @Via("resource")
    private LinkModel link;

    /** The pw theme. */
    @ValueMapValue
    private String pwTheme;

    /** The pw button theme. */
    @ValueMapValue
    private String pwButtonTheme;

    /** The pw display. */
    @ValueMapValue
    private String pwDisplay;

    /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        if (link != null && StringUtils.isNotEmpty(link.getLinkUrl())) {
            link.setLinkUrl(LinkUtils.sanitizeLink(link.getLinkUrl(), request));
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
     * Gets the image alt text.
     *
     * @return the image alt text
     */
    public String getImageAltText() {
        return imageAltText;
    }

    /**
     * Gets the link.
     *
     * @return the link
     */
    public LinkModel getLink() {
        return link;
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
        return LinkUtils.getAssetName(link.getLinkUrl());
    }
}
