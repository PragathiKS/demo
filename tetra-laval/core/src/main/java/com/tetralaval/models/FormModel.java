package com.tetralaval.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class FormModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FormModel {

    /** The heading. */
    @ValueMapValue
    private String heading;

    /** The image. */
    @ValueMapValue
    private String image;

    /** The alt. */
    @ValueMapValue
    private String alt;

    /** The description text. */
    @ValueMapValue
    private String descriptionText;

    /** The anchor id. */
    @ValueMapValue
    private String anchorId;

    /** The anchor title. */
    @ValueMapValue
    private String anchorTitle;

    /** The pw theme. */
    @ValueMapValue
    @Default(values = "grayscale-white")
    private String pwTheme;

    /** The thankyou heading. */
    @ValueMapValue
    private String thankyouHeading;

    /** The thankyou image. */
    @ValueMapValue
    private String thankyouImage;

    /** The thankyou image alt text. */
    @ValueMapValue
    private String thankyouImageAltText;

    /** The thankyou description text. */
    @ValueMapValue
    private String thankyouDescriptionText;

    /** The legal info link. */
    @ValueMapValue
    private String legalInfoLink;

    /** Contact us **/
    @ValueMapValue
    private String contactUsLink;

    /** The newsroom link. */
    @ValueMapValue
    private String newsroomLink;

    /**
     * Gets the heading.
     *
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
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
     * Gets the description text.
     *
     * @return the description text
     */
    public String getDescriptionText() {
        return descriptionText;
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
     * Gets the pw theme.
     *
     * @return the pw theme
     */
    public String getPwTheme() {
        return pwTheme;
    }

    /**
     * Gets the thankyou heading.
     *
     * @return the thankyou heading
     */
    public String getThankyouHeading() {
        return thankyouHeading;
    }

    /**
     * Gets the thankyou image.
     *
     * @return the thankyou image
     */
    public String getThankyouImage() {
        return thankyouImage;
    }

    /**
     * Gets the thankyou image alt text.
     *
     * @return the thankyou image alt text
     */
    public String getThankyouImageAltText() {
        return thankyouImageAltText;
    }

    /**
     * Gets the thankyou description text.
     *
     * @return the thankyou description text
     */
    public String getThankyouDescriptionText() {
        return thankyouDescriptionText;
    }

    /**
     * Gets the legal info link.
     *
     * @return the legal info link
     */
    public String getLegalInfoLink() {
        return legalInfoLink;
    }

    /**
     * Gets the contact us link.
     *
     * @return the contact us link
     */

    public String getContactUsLink() {
        return contactUsLink;
    }

    /**
     * Gets the news room link.
     *
     * @return the news room link
     */
    public String getNewsroomLink() {
        return newsroomLink;
    }

}
