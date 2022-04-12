package com.tetrapak.customerhub.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Model class for Engineering licenses child node of PlantMaster Licenses component.
 */
@Model(adaptables = {
        Resource.class, SlingHttpServletRequest.class
}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EngineeringLicenseModel {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineeringLicenseModel.class);
    
    public static final String TITLE_JSON_KEY = "title";
    public static final String DESCRIPTION_JSON_KEY = "description";
    public static final String USER_DETAILS_SECTION_TITLE_JSON_KEY = "userDetailsSectionTitle";
    public static final String NAME_JSON_KEY = "name";
    public static final String NAME_PLACEHOLDER_JSON_KEY = "namePlaceholder";
    public static final String ACTIVATION_DATE_JSON_KEY = "activationDate";
    public static final String ACTIVATION_DATE_PLACEHOLDER_JSON_KEY = "activationDatePlaceholder";
    public static final String LICENSE_SELECTION_SECTION_TITLE_JSON_KEY = "licenseSelectionSectionTitle";
    public static final String ADD_USER_JSON_KEY = "addUser";
    public static final String REMOVE_USER_JSON_KEY = "removeUser";
    public static final String COMMENTS_JSON_KEY = "comments";
    public static final String SUBMIT_BUTTON_JSON_KEY = "submitButton";
    public static final String LICENSE_DESCRIPTIONS_JSON_KEY = "licenseDescriptions";
    public static final String SUCCESS_MESSAGE_HEADING_JSON_KEY = "successMessageHeading";
    public static final String SUCCESS_MESSAGE_DESCRIPTION_JSON_KEY = "successMessageDescription";
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
    @SerializedName(TITLE_JSON_KEY)
    private String title;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(USER_DETAILS_SECTION_TITLE_JSON_KEY)
    private String userDetailsSectionTitle;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(NAME_JSON_KEY)
    private String name;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(NAME_PLACEHOLDER_JSON_KEY)
    private String namePlaceholder;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(ACTIVATION_DATE_JSON_KEY)
    private String activationDate;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(ACTIVATION_DATE_PLACEHOLDER_JSON_KEY)
    private String activationDatePlaceholder;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(LICENSE_SELECTION_SECTION_TITLE_JSON_KEY)
    private String licenseSelectionSectionTitle;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(ADD_USER_JSON_KEY)
    private String addUser;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(REMOVE_USER_JSON_KEY)
    private String removeUser;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(COMMENTS_JSON_KEY)
    private String comments;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SUBMIT_BUTTON_JSON_KEY)
    private String submitButton;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(LICENSE_DESCRIPTIONS_JSON_KEY)
    private String licenseDescriptions;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SUCCESS_MESSAGE_HEADING_JSON_KEY)
    private String successMessageHeading;
    
    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(SUCCESS_MESSAGE_DESCRIPTION_JSON_KEY)
    private String successMessageDescription;

    @ValueMapValue
    @Expose(serialize = true)
    @SerializedName(DESCRIPTION_JSON_KEY)
    private String description;

    @ValueMapValue
    @Expose(serialize = true)
    private String inputFieldError;

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
    
    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Engineering License Model created");
    }
    
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
