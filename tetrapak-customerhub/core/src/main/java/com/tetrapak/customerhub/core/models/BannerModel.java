package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The Class BannerModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;
    
    /** The resource. */
    private Resource resource;

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

    /** The link. */
    @Inject
    @Via("resource")
    private LinkModel link;

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

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";
    
    /** The Constant HERO_TEST. */
    private static final String HERO = "hero";
    
    /** The Constant HERO_TEST. */
    private static final String HERO_WIDE = "hero-wide";

    /** The Constant SKY_BLUE. */
    private static final String SKY_BLUE = "sky-blue";

   /**
     * The init method.
     */
    @PostConstruct
    protected void init() {
        resource = request.getResource();
        final ValueMap vMap = resource.getValueMap();
        if(link != null) {
            link.setLinkUrl(LinkUtils.sanitizeLink(link.getLinkUrl(), request));
        }
    }

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
    public LinkModel getLink() {
        return link;
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
        if (Objects.isNull(pwCardTheme) && Objects.nonNull(bannerType) && (bannerType.equalsIgnoreCase(HERO)
                || bannerType.equalsIgnoreCase(HERO_WIDE))) {
            pwCardTheme = SKY_BLUE;
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
        if (link != null && StringUtils.isNoneEmpty(link.getLinkUrl())) {
            assetName = StringUtils.substringAfterLast(link.getLinkUrl(), FORWARD_SLASH);
        }
        return assetName;
    }
}
