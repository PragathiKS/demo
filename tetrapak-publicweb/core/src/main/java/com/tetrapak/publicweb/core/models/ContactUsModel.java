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
import com.tetrapak.publicweb.core.services.FindMyOfficeService;

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
    private String firstNameLabel;
    @ValueMapValue
    private String lastNameLabel;
    @ValueMapValue
    private String emailLabel;
    @ValueMapValue
    private String emailPlaceholder;
    @ValueMapValue
    private String messageLabel;
    @ValueMapValue
    private String countryLabel;
    @ValueMapValue
    private String previousbuttonLabel;
    @ValueMapValue
    private String nextbuttonLabel;
    @ValueMapValue
    private String submitbuttonLabel;
    @ValueMapValue
    private String newrequestbuttonLabel;

    private List<DropdownOption> purposeOfContactOptions = null;
    private List<DropdownOption> countryOptions = null;

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

    @OSGiService
    private FindMyOfficeService findMyOffice;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        setCountryOptions();
        setPurposeOfContactOptions();
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

    public String getFirstNameLabel() {
        return firstNameLabel;
    }

    public String getLastNameLabel() {
        return lastNameLabel;
    }

    public String getEmailLabel() {
        return emailLabel;
    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getMessageLabel() {
        return messageLabel;
    }

    public String getCountryLabel() {
        return countryLabel;
    }

    public String getPreviousbuttonLabel() {
        return previousbuttonLabel;
    }

    public String getNextbuttonLabel() {
        return nextbuttonLabel;
    }

    public String getSubmitbuttonLabel() {
        return submitbuttonLabel;
    }

    public String getNewrequestbuttonLabel() {
        return newrequestbuttonLabel;
    }

    public List<DropdownOption> getPurposeOfContactOptions() {
        return purposeOfContactOptions;
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

    private void setPurposeOfContactOptions() {
        this.purposeOfContactOptions = findMyOffice.fetchPurposeOfContacts(resource.getResourceResolver());
    }

    private void setCountryOptions() {
        this.countryOptions = findMyOffice.fetchCountryList(resource.getResourceResolver());
    }

}
