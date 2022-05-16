package com.tetrapak.customerhub.core.models;

import com.google.gson.annotations.Expose;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class for Active licenses child node of PlantMaster Licenses component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ActiveLicenseModel {

    public static final String COMMENTS_JSON_KEY = "comments";
    public static final String CONTACT_PERSON = "contactPerson";

    
    @SlingObject
    private Resource resource;
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String title;

    @ValueMapValue
    @Expose(serialize = true)
    private String engLicenseTitle;

    @ValueMapValue
    @Expose(serialize = true)
    private String siteLicenseTitle;

    @ValueMapValue
    @Expose(serialize = true)
    private String name;

    @ValueMapValue
    @Expose(serialize = true)
    private String licenceKey;

    @ValueMapValue
    @Expose(serialize = true)
    private String platform;

    @ValueMapValue
    @Expose(serialize = true)
    private String startDate;

    @ValueMapValue
    @Expose(serialize = true)
    private String siteDate;

    @ValueMapValue
    @Expose(serialize = true)
    private String endDate;

    @ValueMapValue
    @Expose(serialize = true)
    private String withdrawBtn;

    @ValueMapValue
    @Expose(serialize = true)
    private String country;

    @ValueMapValue
    @Expose(serialize = true)
    private String site;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalConfirmTitle;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalComments;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalConfirmText;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalConfirmBtn;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalCancelBtn;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalWithdrawSuccessTitle;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalWithdrawSuccessText;

    @ValueMapValue
    @Expose(serialize = true)
    private String modalWithdrawBack;
    
    @ValueMapValue
    private String subject;
    
    @ValueMapValue
    private String salutation;
    
    @ValueMapValue
    private String body;

    public String getTitle() {
        return title;
    }

    public String getEngLicenseTitle() {
        return engLicenseTitle;
    }

    public String getSiteLicenseTitle() {
        return siteLicenseTitle;
    }

    public String getName() {
        return name;
    }

    public String getLicenceKey() {
        return licenceKey;
    }

    public String getPlatform() {
        return platform;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getWithdrawBtn() {
        return withdrawBtn;
    }

    public String getCountry() {
        return country;
    }

    public String getSite() {
        return site;
    }

    public String getModalConfirmTitle() {
        return modalConfirmTitle;
    }

    public String getModalComments() {
        return modalComments;
    }

    public String getModalConfirmText() {
        return modalConfirmText;
    }

    public String getModalConfirmBtn() {
        return modalConfirmBtn;
    }

    public String getModalCancelBtn() {
        return modalCancelBtn;
    }

    public String getModalWithdrawSuccessTitle() {
        return modalWithdrawSuccessTitle;
    }

    public String getModalWithdrawSuccessText() {
        return modalWithdrawSuccessText;
    }

    public String getModalWithdrawBack() {
        return modalWithdrawBack;
    }

    public String getSubject() {
        return subject;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getBody() {
        return body;
    }

    public String getSiteDate() {
        return siteDate;
    }
}
