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
    public static final String REQUESTER = "requester";
    public static final String USERNAME = "username";
    public static final String LICENSE_KEY = "licenseKey";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String PLATFORM = "platform";

    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;
    
    @ValueMapValue
    @Expose()
    private String title;

    @ValueMapValue
    @Expose()
    private String engLicenseTitle;

    @ValueMapValue
    @Expose()
    private String siteLicenseTitle;

    @ValueMapValue
    @Expose()
    private String name;

    @ValueMapValue
    @Expose()
    private String licenceKey;

    @ValueMapValue
    @Expose()
    private String platformText;

    @ValueMapValue
    @Expose()
    private String startDate;

    @ValueMapValue
    @Expose()
    private String siteDate;

    @ValueMapValue
    @Expose()
    private String endDate;

    @ValueMapValue
    @Expose()
    private String withdrawBtn;

    @ValueMapValue
    @Expose()
    private String country;

    @ValueMapValue
    @Expose()
    private String site;

    @ValueMapValue
    @Expose()
    private String modalConfirmTitle;

    @ValueMapValue
    @Expose()
    private String modalComments;

    @ValueMapValue
    @Expose()
    private String modalConfirmText;

    @ValueMapValue
    @Expose()
    private String modalConfirmBtn;

    @ValueMapValue
    @Expose()
    private String modalCancelBtn;

    @ValueMapValue
    @Expose()
    private String modalWithdrawSuccessTitle;

    @ValueMapValue
    @Expose()
    private String modalWithdrawSuccessText;

    @ValueMapValue
    @Expose()
    private String modalWithdrawBack;
    
    @ValueMapValue
    @Expose()
    private String subject;
    
    @ValueMapValue
    @Expose()
    private String salutation;
    
    @ValueMapValue
    @Expose()
    private String body;

    @ValueMapValue
    private String requestorText;

    @ValueMapValue
    @Expose()
    private String usernameText;

    @ValueMapValue
    @Expose()
    private String emailCommentText;

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

    public String getPlatformText() {
        return platformText;
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

    public String getRequestorText() {
        return requestorText;
    }

    public String getUsernameText() {
        return usernameText;
    }

    public String getEmailCommentText() {
        return emailCommentText;
    }
}
