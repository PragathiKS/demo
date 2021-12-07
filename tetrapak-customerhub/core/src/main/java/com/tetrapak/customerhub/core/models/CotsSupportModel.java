package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CotsSupportModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The title label. */
    @Inject
    private String title;

    /** The subtitle label. */
    @Inject
    private String subTitle;

    /** The select type of log label. */
    @Inject
    private String selectTypeOfLog;

    /** The technical issues label. */
    @Inject
    private String technicalIssues;

    /** The product support label. */
    @Inject
    private String productSupport;

    /** The dropdown placeholder. */
    @Inject
    private String dropdownPlaceholder;

    /** The urgency label. */
    @Inject
    private String urgency;

    /** The urgency error message. */
    @Inject
    private String urgencyErrorMsg;

    /** The input error message. */
    @Inject
    private String inputErrorMsg;

    /** The affected systems label. */
    @Inject
    private String affectedSystems;

    /** The software version label. */
    @Inject
    private String softwareVersion;

    /** The description label. */
    @Inject
    private String description;

    /** The comments label. */
    @Inject
    private String comments;

    /** The comments error message. */
    @Inject
    private String commentsErrorMsg;

    /** The marketing consent. */
    @Inject
    private String marketingConsent;

    /** The submit button label. */
    @Inject
    private String submitButtonLabel;

    /** The confirm your details header. */
    @Inject
    private String confirmYourDetailsHeader;

    /** The customer site header. */
    @Inject
    private String customerSiteHeader;

    /** The company name label. */
    @Inject
    private String companyName;

    /** The street name label. */
    @Inject
    private String streetName;

    /** The city label. */
    @Inject
    private String city;

    /** The country label. */
    @Inject
    private String country;

    /** The id header. */
    @Inject
    private String idHeader;

    /** The id name label. */
    @Inject
    private String idName;

    /** The supplier details header. */
    @Inject
    private String supplierDetailsHeader;

    /** The contact details header. */
    @Inject
    private String contactDetailsHeader;

    /** The supplier company name label. */
    @Inject
    private String supplierCompanyName;

    /** The supplier street name label. */
    @Inject
    private String supplierStreetName;

    /** The supplier city label. */
    @Inject
    private String supplierCity;

    /** The supplier country label. */
    @Inject
    private String supplierCountry;

    /** The name label. */
    @Inject
    private String name;

    /** The email address label. */
    @Inject
    private String emailAddress;

    /** The email error message. */
    @Inject
    private String emailErrorMsg;

    /** The telephone label. */
    @Inject
    private String telephone;

    /** The phone error message. */
    @Inject
    private String phoneErrorMsg;

    /** The success message. */
    @Inject
    private String successMessage;

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getSelectTypeOfLog() {
        return selectTypeOfLog;
    }

    public String getTechnicalIssues() {
        return technicalIssues;
    }

    public String getProductSupport() {
        return productSupport;
    }

    public String getDropdownPlaceholder() {
        return dropdownPlaceholder;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getUrgencyErrorMsg() {
        return urgencyErrorMsg;
    }

    public String getInputErrorMsg() {
        return inputErrorMsg;
    }

    public String getAffectedSystems() {
        return affectedSystems;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getComments() {
        return comments;
    }

    public String getCommentsErrorMsg() {
        return commentsErrorMsg;
    }

    public String getMarketingConsent() {
        return marketingConsent;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public String getConfirmYourDetailsHeader() {
        return confirmYourDetailsHeader;
    }

    public String getCustomerSiteHeader() {
        return customerSiteHeader;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public String getIdName() {
        return idName;
    }

    public String getSupplierDetailsHeader() {
        return supplierDetailsHeader;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public String getSupplierStreetName() {
        return supplierStreetName;
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public String getSupplierCountry() {
        return supplierCountry;
    }

    public String getContactDetailsHeader() {
        return contactDetailsHeader;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPhoneErrorMsg() {
        return phoneErrorMsg;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    /** The i18n keys. */
    private String i18nKeys;

    /**
     * Gets the i18n keys.
     *
     * @return the i18n keys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_TITLE, getTitle());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUBTITLE, getSubTitle());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SELECT_TYPE_OF_LOG, getSelectTypeOfLog());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_TECHNICAL_ISSUES, getTechnicalIssues());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_PRODUCT_SUPPORT, getProductSupport());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_DROPDOWN_PLACEHOLDER, getDropdownPlaceholder());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_URGENCY, getUrgency());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_URGENCY_ERROR_MSG, getUrgencyErrorMsg());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_INPUT_ERROR_MSG, getInputErrorMsg());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_AFFECTED_SYSTEMS, getAffectedSystems());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SOFTWARE_VERSION, getSoftwareVersion());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_DESCRIPTION, getDescription());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_COMMENTS, getComments());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_COMMENTS_ERROR_MSG, getCommentsErrorMsg());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_MARKETING_CONSENT, getMarketingConsent());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUBMIT_BUTTON_LABEL, getSubmitButtonLabel());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_CONFIRM_YOUR_DETAILS_HEADER, getConfirmYourDetailsHeader());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_CUSTOMER_SITE_HEADER, getCustomerSiteHeader());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_COMPANY_NAME, getCompanyName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_STREET_NAME, getStreetName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_CITY, getCity());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_COUNTRY, getCountry());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_ID_HEADER, getIdHeader());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_ID_NAME, getIdName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUPPLIER_DETAILS_HEADER, getSupplierDetailsHeader());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUPPLIER_COMPANY_NAME, getSupplierCompanyName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUPPLIER_STREET_NAME, getSupplierStreetName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUPPLIER_CITY, getSupplierCity());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUPPLIER_COUNTRY, getSupplierCountry());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_CONTACT_DETAILS_HEADER, getContactDetailsHeader());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_NAME, getName());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_EMAIL_ADDRESS, getEmailAddress());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_EMAIL_ERROR_MSG, getEmailErrorMsg());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_TELEPHONE, getTelephone());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_PHONE_ERROR_MSG, getPhoneErrorMsg());
        i18KeyMap.put(CustomerHubConstants.COTS_SUPPORT_SUCCESS_MESSAGE, getSuccessMessage());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
    }
}
