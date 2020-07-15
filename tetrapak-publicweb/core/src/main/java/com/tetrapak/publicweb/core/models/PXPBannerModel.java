package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class PXPBannerModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PXPBannerModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The subTitle. */
    private String subTitle;

    /** The heading tag. */
    @ValueMapValue
    private String headingTag;

    /** The title. */
    private String title;

    /** The description. */
    private String description;

    /** The image Alt Text. */
    private String imageAltText;

    /** The image Path. */
    private String imagePath;

    /** The pw display. */
    @ValueMapValue
    @Default(values = "display-row")
    private String pwDisplay;

    /** The pw theme. */
    @ValueMapValue
    @Default(values = "grayscale-white")
    private String pwTheme;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        ProductModel product = resource.adaptTo(ProductModel.class);
        title = product.getHeader();
        description = product.getBenifits();
        imagePath = product.getBenefitsimage();
        subTitle = product.getName();
        imageAltText = product.getName();
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
     * Gets the image alt text.
     *
     * @return the image alt text
     */
    public String getImageAltText() {
        return imageAltText;
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
