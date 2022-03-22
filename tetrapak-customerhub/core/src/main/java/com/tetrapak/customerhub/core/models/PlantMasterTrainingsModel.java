package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.servlets.PlantMasterTrainingsEmailServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * The Class PlantMasterTrainingsModel.
 */
@Model(
        adaptables = { Resource.class, SlingHttpServletRequest.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterTrainingsModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterTrainingsModel.class);

    /**
     * The Enum PlantMasterTrainingsComponentDialog.
     */
    public enum PlantMasterTrainingsComponentDialog {

        /** The title. */
        TITLE("title"),

        /** The available trainings. */
        AVAILABLE_TRAININGS("availableTrainings"),

        /** The learning history. */
        LEARNING_HISTORY("learningHistory"),

        /** The training material handouts. */
        TRAINING_MATERIAL_HANDOUTS("trainingMaterialHandouts"),

        /** The course description. */
        COURSE_DESCRIPTION("courseDescription"),

        /** The principle objectives. */
        PRINCIPLE_OBJECTIVES("principleObjectives"),

        /** The target groups. */
        TARGET_GROUPS("targetGroups"),

        /** The duration. */
        DURATION("duration"),

        /** The max participants. */
        MAX_PARTICIPANTS("maxParticipants"),

        /** The knowledge requirements. */
        KNOWLEDGE_REQUIREMENTS("knowledgeRequirements"),

        /** The no of participants. */
        NO_OF_PARTICIPANTS_LABEL("noOfParticipantsLabel"),

        /** The preferred location. */
        PREFERRED_LOCATION_LABEL("preferredLocationLabel"),

        /** The preferred date. */
        PREFERRED_DATE_LABEL("preferredDateLabel"),

        /** The comments. */
        COMMENTS_LABEL("commentsLabel"),

        /** The confirmation text. */
        CONFIRMATION_TEXT("confirmationText"),

        /** The input error message. */
        INPUT_ERROR_MESSAGE("inputErrorMsg"),

        /** The success message. */
        SUCCESS_MESSAGE("successMessage"),

        /** The subject. */
        SUBJECT("subject"),

        /** The salutation. */
        SALUTATION("salutation"),

        /** The body. */
        BODY("body"),

        /** The submit button. */
        SUBMIT_BUTTON("submitButtonLabel"),

        /** The consent label. */
        CONSENT_LABEL("consentLabel"),

        /** The contact details. */
        CONTACT_DETAILS("contactDetails"),

        /** The training id. */
        TRAINING_ID_LABEL("trainingIdLabel"),

        /** The training name. */
        TRAINING_NAME_LABEL("trainingNameLabel"),

        /** The form title. */
        FORM_TITLE("formTitle");

        /** The i18n json key. */
        public final String i18nJsonKey;

        /**
         * Instantiates a new plant master trainings component dialog.
         *
         * @param jsonKey
         *            the json key
         */
        PlantMasterTrainingsComponentDialog(String jsonKey) {
            this.i18nJsonKey = jsonKey;
        }

        /**
         * Gets the i18n json key.
         *
         * @return the i18n json key
         */
        private String getI18nJsonKey() {
            return i18nJsonKey;
        }
    }

    /** The resource. */
    @SlingObject
    private Resource resource;

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The available trainings. */
    @ValueMapValue
    private String availableTrainings;

    /** The learning history. */
    @ValueMapValue
    private String learningHistory;

    /** The training material handouts. */
    @ValueMapValue
    private String trainingMaterialHandouts;

    /** The course description. */
    @ValueMapValue
    private String courseDescription;

    /** The principle objectives. */
    @ValueMapValue
    private String principleObjectives;

    /** The target groups. */
    @ValueMapValue
    private String targetGroups;

    /** The duration. */
    @ValueMapValue
    private String duration;

    /** The max participants. */
    @ValueMapValue
    private String maxParticipants;

    /** The knowledge requirements. */
    @ValueMapValue
    private String knowledgeRequirements;

    /** The no of participants label. */
    @ValueMapValue
    private String noOfParticipantsLabel;

    /** The preferred location label. */
    @ValueMapValue
    private String preferredLocationLabel;

    /** The preferred date label. */
    @ValueMapValue
    private String preferredDateLabel;

    /** The comments label. */
    @ValueMapValue
    private String commentsLabel;

    /** The confirmation text. */
    @ValueMapValue
    private String confirmationText;

    /** The submit button label. */
    @ValueMapValue
    private String submitButtonLabel;

    /** The input error message label. */
    @ValueMapValue
    private String inputErrorMsg;

    /** The success message. */
    @ValueMapValue
    private String successMessage;

    /** The subject. */
    @ValueMapValue
    private String subject;

    /** The salutation text in email. */
    @ValueMapValue
    private String salutation;

    /** The body text in email. */
    @ValueMapValue
    private String body;

    /** The consent label. */
    @ValueMapValue
    private String consentLabel;

    /** The contact details. */
    @ValueMapValue
    private String contactDetails;

    /** The training id label. */
    @ValueMapValue
    private String trainingIdLabel;

    /** The training name label. */
    @ValueMapValue
    private String trainingNameLabel;

    /** The form title. */
    @ValueMapValue
    private String formTitle;

    /** The training details api. */
    @ValueMapValue
    private String trainingDetailsApi;

    /** The apigee service. */
    @OSGiService
    private APIGEEService apigeeService;

    /** The aip category service. */
    @OSGiService
    private AIPCategoryService aipCategoryService;

    /** The i18n keys. */
    private String i18nKeys;

    /** The user name. */
    private String userName;

    /** The user email address. */
    private String userEmailAddress;

    /** The component path. */
    private String componentPath;

    /** The component path extension. */
    private String componentPathExtension;

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TITLE.getI18nJsonKey(), getTitle());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.AVAILABLE_TRAININGS.getI18nJsonKey(),
                getAvailableTrainings());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.LEARNING_HISTORY.getI18nJsonKey(), getLearningHistory());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_MATERIAL_HANDOUTS.getI18nJsonKey(),
                getTrainingMaterialHandouts());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.COURSE_DESCRIPTION.getI18nJsonKey(), getCourseDescription());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PRINCIPLE_OBJECTIVES.getI18nJsonKey(),
                getPrincipleObjectives());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TARGET_GROUPS.getI18nJsonKey(), getTargetGroups());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.DURATION.getI18nJsonKey(), getDuration());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.MAX_PARTICIPANTS.getI18nJsonKey(), getMaxParticipants());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.KNOWLEDGE_REQUIREMENTS.getI18nJsonKey(),
                getKnowledgeRequirements());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.NO_OF_PARTICIPANTS_LABEL.getI18nJsonKey(),
                getNoOfParticipantsLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_LOCATION_LABEL.getI18nJsonKey(),
                getPreferredLocationLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_DATE_LABEL.getI18nJsonKey(),
                getPreferredDateLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.COMMENTS_LABEL.getI18nJsonKey(), getCommentsLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONFIRMATION_TEXT.getI18nJsonKey(), getConfirmationText());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.INPUT_ERROR_MESSAGE.getI18nJsonKey(), getInputErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUCCESS_MESSAGE.getI18nJsonKey(), getSuccessMessage());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBJECT.getI18nJsonKey(), getSubject());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SALUTATION.getI18nJsonKey(), getSalutation());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.BODY.getI18nJsonKey(), getBody());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONTACT_DETAILS.getI18nJsonKey(), getContactDetails());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_ID_LABEL.getI18nJsonKey(), getTrainingIdLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_NAME_LABEL.getI18nJsonKey(), getTrainingNameLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.FORM_TITLE.getI18nJsonKey(), getFormTitle());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONSENT_LABEL.getI18nJsonKey(), getConsentLabel());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}", i18nKeys);

        this.componentPath = resource.getResourceResolver().map(this.resource.getPath());
        LOGGER.debug("Resource mapped url : {}", this.componentPath);

        this.componentPathExtension = CustomerHubConstants.DOT + PlantMasterTrainingsEmailServlet.SLING_SERVLET_SELECTOR
                + CustomerHubConstants.DOT + PlantMasterTrainingsEmailServlet.SLING_SERVLET_EXTENSION;

        this.setUserEmailAddress();

        if (Objects.nonNull(request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME))) {
            this.userName = request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue();
        }
        String apiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                CustomerHubConstants.AIP_PRODUCT_DETAILS_API);
        trainingDetailsApi = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), apiMapping,
                aipCategoryService.getAutomationTrainingsId());
        LOGGER.debug("Engineering Trainings API : {}", trainingDetailsApi);
    }

    /**
     * Sets the user email address.
     */
    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the available trainings.
     *
     * @return the available trainings
     */
    public String getAvailableTrainings() {
        return availableTrainings;
    }

    /**
     * Gets the learning history.
     *
     * @return the learning history
     */
    public String getLearningHistory() {
        return learningHistory;
    }

    /**
     * Gets the training material handouts.
     *
     * @return the training material handouts
     */
    public String getTrainingMaterialHandouts() {
        return trainingMaterialHandouts;
    }

    /**
     * Gets the course description.
     *
     * @return the course description
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Gets the principle objectives.
     *
     * @return the principle objectives
     */
    public String getPrincipleObjectives() {
        return principleObjectives;
    }

    /**
     * Gets the target groups.
     *
     * @return the target groups
     */
    public String getTargetGroups() {
        return targetGroups;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Gets the max participants.
     *
     * @return the max participants
     */
    public String getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Gets the knowledge requirements.
     *
     * @return the knowledge requirements
     */
    public String getKnowledgeRequirements() {
        return knowledgeRequirements;
    }

    /**
     * Gets the no of participants label.
     *
     * @return the no of participants label
     */
    public String getNoOfParticipantsLabel() {
        return noOfParticipantsLabel;
    }

    /**
     * Gets the preferred location label.
     *
     * @return the preferred location label
     */
    public String getPreferredLocationLabel() {
        return preferredLocationLabel;
    }

    /**
     * Gets the preferred date label.
     *
     * @return the preferred date label
     */
    public String getPreferredDateLabel() {
        return preferredDateLabel;
    }

    /**
     * Gets the comments label.
     *
     * @return the comments label
     */
    public String getCommentsLabel() {
        return commentsLabel;
    }

    /**
     * Gets the confirmation text.
     *
     * @return the confirmation text
     */
    public String getConfirmationText() {
        return confirmationText;
    }

    /**
     * Gets the submit button label.
     *
     * @return the submit button label
     */
    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    /**
     * Gets the input error msg.
     *
     * @return the input error msg
     */
    public String getInputErrorMsg() {
        return inputErrorMsg;
    }

    /**
     * Gets the success message.
     *
     * @return the success message
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Gets the salutation.
     *
     * @return the salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Gets the consent label.
     *
     * @return the consent label
     */
    public String getConsentLabel() {
        return consentLabel;
    }

    /**
     * Gets the contact details.
     *
     * @return the contact details
     */
    public String getContactDetails() {
        return contactDetails;
    }

    /**
     * Gets the training id label.
     *
     * @return the training id label
     */
    public String getTrainingIdLabel() {
        return trainingIdLabel;
    }

    /**
     * Gets the training name label.
     *
     * @return the training name label
     */
    public String getTrainingNameLabel() {
        return trainingNameLabel;
    }

    /**
     * Gets the form title.
     *
     * @return the form title
     */
    public String getFormTitle() {
        return formTitle;
    }

    /**
     * Gets the training details api.
     *
     * @return the training details api
     */
    public String getTrainingDetailsApi() {
        return trainingDetailsApi;
    }

    /**
     * Gets the i18n keys.
     *
     * @return the i18n keys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the user email address.
     *
     * @return the user email address
     */
    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    /**
     * Gets the component path.
     *
     * @return the component path
     */
    public String getComponentPath() {
        return componentPath;
    }

    /**
     * Gets the component path extension.
     *
     * @return the component path extension
     */
    public String getComponentPathExtension() {
        return componentPathExtension;
    }

}
