package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RequestEngineeringLicensesModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestEngineeringLicensesModel.class);

    public enum requestEngineeringLicenseComponentDialog {
        USER_DETAILS_SECTION_TITLE("userDetailsSectionTitle"),
        NAME("name"),
        NAME_PLACEHOLDER("namePlaceholder"),
        ACTIVATION_DATE("activationDate"),
        ACTIVATION_DATE_PLACEHOLDER("activationDatePlaceholder"),
        LICENSE_SELECTION_SECTION_TITLE("licenseSelectionSectionTitle"),
        ADD_USER("addUser"),
        COMMENTS("comments"),
        SUBMIT_BUTTON("submitButton"),
        LICENSE_DESCRIPTIONS("licenseDescriptions"),
        DESCRIPTION("description"),
        EMAIL_SUBJECT("subject"),
        EMAIL_SALUTATION("salutation"),
        EMAIL_BODY("body");

        public final String i18nJsonKey;

        requestEngineeringLicenseComponentDialog(String jsonKey) {
            this.i18nJsonKey = jsonKey;
        }

        private String getI18nJsonKey(){
            return i18nJsonKey;
        }

    }

    /** The resource. */
    @SlingObject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String userDetailsSectionTitle;

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String namePlaceholder;

    @ValueMapValue
    private String activationDate;

    @ValueMapValue
    private String activationDatePlaceholder;

    @ValueMapValue
    private String licenseSelectionSectionTitle;

    @ValueMapValue
    private String addUser;

    @ValueMapValue
    private String comments;

    @ValueMapValue
    private String submitButton;

    @ValueMapValue
    private String licenseDescriptions;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String subject;

    @ValueMapValue
    private String salutation;

    @ValueMapValue
    private String body;

    private String i18nKeys;

    private String userName;

    private String userEmailAddress;

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.USER_DETAILS_SECTION_TITLE.getI18nJsonKey(), getUserDetailsSectionTitle());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.NAME.getI18nJsonKey(), getName());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.NAME_PLACEHOLDER.getI18nJsonKey(), getNamePlaceholder());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.ACTIVATION_DATE.getI18nJsonKey(), getActivationDate());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.ACTIVATION_DATE_PLACEHOLDER.getI18nJsonKey(), getActivationDatePlaceholder());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.LICENSE_SELECTION_SECTION_TITLE.getI18nJsonKey(), getLicenseSelectionSectionTitle());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.ADD_USER.getI18nJsonKey(), getAddUser());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.COMMENTS.getI18nJsonKey(), getComments());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButton());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.LICENSE_DESCRIPTIONS.getI18nJsonKey(), getLicenseDescriptions());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.DESCRIPTION.getI18nJsonKey(), getDescription());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.EMAIL_SUBJECT.getI18nJsonKey(), getSubject());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.EMAIL_SALUTATION.getI18nJsonKey(), getSalutation());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.EMAIL_BODY.getI18nJsonKey(), getBody());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.USER_DETAILS_SECTION_TITLE.getI18nJsonKey(), getUserDetailsSectionTitle());
        i18KeyMap.put(requestEngineeringLicenseComponentDialog.USER_DETAILS_SECTION_TITLE.getI18nJsonKey(), getUserDetailsSectionTitle());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}",i18nKeys);

        this.setUserEmailAddress();
        if (request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME) != null) {
            this.userName = request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue();
        }
    }

    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
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

    public String getComments() {
        return comments;
    }

    public String getSubmitButton() {
        return submitButton;
    }

    public String getLicenseDescriptions() {
        return licenseDescriptions;
    }

    public String getDescription() {
        return description;
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

    public String getI18nKeys() {
        return i18nKeys;
    }
}


