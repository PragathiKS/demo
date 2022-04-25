package com.tetrapak.customerhub.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Model class for Engineering licenses child node of PlantMaster Licenses component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EngineeringLicenseModel {

    public static final String COMMENTS_JSON_KEY = "comments";
    public static final String USERS_EMAIL_LABEL = "users";
    public static final String LICENSE_TABLE_USER_NAME = "licenseTableUserName";
    public static final String LICENSE_TABLE_ACTIVATION_DATE = "licenseTableActivationDate";
    public static final String LICENSE_TABLE_LIST_OF_LICENSES = "licenseTableListOfLicenses";
    
    @SlingObject
    @Expose(serialize = false)
    private Resource resource;
    
    @SlingObject
    @Expose(serialize = false)
    private SlingHttpServletRequest request;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String title;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String userDetailsSectionTitle;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String name;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String namePlaceholder;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String activationDate;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String activationDatePlaceholder;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String licenseSelectionSectionTitle;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String addUser;

    @ValueMapValue
    @Expose(serialize = true)
    private String removeUser;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(COMMENTS_JSON_KEY)
    private String comments;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String submitButton;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String licenseDescriptions;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String successMessageHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    private String successMessageDescription;

    @ValueMapValue
    @Expose(serialize = true)
    private String description;

    @ValueMapValue
    @Expose(serialize = true)
    private String inputFieldError;

    @ValueMapValue
    private String inputLicensesFormatErrorMsg;

    @ValueMapValue
    @Expose(serialize = true)
    private String checkboxFieldError;

    @ValueMapValue
    @Expose(serialize = true)
    private String correctFormsError;
    
    @ValueMapValue
    private String subject;
    
    @ValueMapValue
    private String salutation;
    
    @ValueMapValue
    private String body;
    
    @ValueMapValue
    private String users;

    @ValueMapValue
    private String licenseTableUserName;

    @ValueMapValue
    private String licenseTableActivationDate;

    @ValueMapValue
    private String licenseTableListOfLicenses;

    public String getTitle() {
        return title;
    }
    
    public String getUserDetailsSectionTitle() {
        return userDetailsSectionTitle;
    }
    
    public String getName() {
        return name;
    }
    
    public String getNamePlaceholder() {
        return namePlaceholder;
    }
    
    public String getActivationDate() {
        return activationDate;
    }
    
    public String getActivationDatePlaceholder() {
        return activationDatePlaceholder;
    }
    
    public String getLicenseSelectionSectionTitle() {
        return licenseSelectionSectionTitle;
    }
    
    public String getAddUser() {
        return addUser;
    }

    public String getRemoveUser() {
        return removeUser;
    }
    
    public String getComments() {
        return comments;
    }
    
    public String getSubmitButton() {
        return submitButton;
    }
    
    public String getLicenseDescriptions() {
        return licenseDescriptions;
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
    
    public String getSuccessMessageHeading() {
        return successMessageHeading;
    }
    
    public String getSuccessMessageDescription() {
        return successMessageDescription;
    }
    
    public String getUsers() {
        return users;
    }

    public String getDescription() {
        return description;
    }

    public String getInputFieldError() {
        return inputFieldError;
    }

    public String getInputLicensesFormatErrorMsg() {
        return inputLicensesFormatErrorMsg;
    }

    public String getCheckboxFieldError() {
        return checkboxFieldError;
    }

    public String getCorrectFormsError() {
        return correctFormsError;
    }

    public String getLicenseTableUserName() {
        return licenseTableUserName;
    }

    public String getLicenseTableActivationDate() {
        return licenseTableActivationDate;
    }

    public String getLicenseTableListOfLicenses() {
        return licenseTableListOfLicenses;
    }
}
