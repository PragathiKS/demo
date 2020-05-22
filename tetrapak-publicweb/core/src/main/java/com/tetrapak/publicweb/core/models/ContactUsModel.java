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

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactUsModel {

    /** The resource. */
    @Self
    private Resource resource;

    @ValueMapValue
    private String heading;
    @ValueMapValue
    private String image;
    @ValueMapValue
    private String alt;
    @ValueMapValue
    private String descriptionText;
    @ValueMapValue
    private String anchorId;
    @ValueMapValue
    private String anchorTitle;

    @ValueMapValue
    @Default(values = "grayscale-white")
    private String pwTheme;

    @ValueMapValue
    private String purposeOfContactText;
    @ValueMapValue
    private String contactInfoText;
    @ValueMapValue
    private String howCanWeHelpText;
    @ValueMapValue
    private String summaryText;
    @ValueMapValue
    private String privacyPolicy;
    @ValueMapValue
    private String thankyouHeading;
    @ValueMapValue
    private String thankyouImage;
    @ValueMapValue
    private String thankyouImageAltText;
    @ValueMapValue
    private String thankyouDescriptionText;

    private List<DropdownOption> countryOptions;

    @OSGiService
    private CountryDetailService countryDetailService;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
    }

    public String getHeading() {
        return heading;
    }

    public String getImage() {
        return image;
    }

    public String getAlt() {
        return alt;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPurposeOfContactText() {
        return purposeOfContactText;
    }

    public String getContactInfoText() {
        return contactInfoText;
    }

    public String getHowCanWeHelpText() {
        return howCanWeHelpText;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public List<DropdownOption> getCountryOptions() {
        return countryOptions;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public String getThankyouHeading() {
        return thankyouHeading;
    }

    public String getThankyouImage() {
        return thankyouImage;
    }

    public String getThankyouImageAltText() {
        return thankyouImageAltText;
    }

    public String getThankyouDescriptionText() {
        return thankyouDescriptionText;
    }

    private void setCountryOptions() {
        this.countryOptions = countryDetailService.fetchCountryList(resource.getResourceResolver());
    }

}
