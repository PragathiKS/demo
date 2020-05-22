package com.tetrapak.publicweb.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.tetrapak.publicweb.core.beans.DropdownOption;
import com.tetrapak.publicweb.core.services.CountryDetailService;

/**
 * The Class ContactUsModel.
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactUsModel {

    /** The resource. */
    @Self
    private Resource resource;

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

    /** The purpose of contact text. */
    @ValueMapValue
    private String purposeOfContactText;

    /** The contact info text. */
    @ValueMapValue
    private String contactInfoText;

    /** The how can we help text. */
    @ValueMapValue
    private String howCanWeHelpText;

    /** The summary text. */
    @ValueMapValue
    private String summaryText;

    /** The privacy policy. */
    @ValueMapValue
    private String privacyPolicy;

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

    /** The country options. */
    private List<DropdownOption> countryOptions;

    /** The country detail service. */
    @OSGiService
    private CountryDetailService countryDetailService;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
    }

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
     * Gets the purpose of contact text.
     *
     * @return the purpose of contact text
     */
    public String getPurposeOfContactText() {
        return purposeOfContactText;
    }

    /**
     * Gets the contact info text.
     *
     * @return the contact info text
     */
    public String getContactInfoText() {
        return contactInfoText;
    }

    /**
     * Gets the how can we help text.
     *
     * @return the how can we help text
     */
    public String getHowCanWeHelpText() {
        return howCanWeHelpText;
    }

    /**
     * Gets the summary text.
     *
     * @return the summary text
     */
    public String getSummaryText() {
        return summaryText;
    }

    /**
     * Gets the country options.
     *
     * @return the country options
     */
    public List<DropdownOption> getCountryOptions() {
        return countryOptions;
    }

    /**
     * Gets the privacy policy.
     *
     * @return the privacy policy
     */
    public String getPrivacyPolicy() {
        return privacyPolicy;
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
     * Fetches country list from content fragments.
     *
     */
    private void setCountryOptions() {
        this.countryOptions = countryDetailService.fetchCountryList(resource.getResourceResolver());
    }

}
