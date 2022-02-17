package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
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
        NO_OF_PARTICIPANTS("noOfParticipants"),

        /** The preferred location. */
        PREFERRED_LOCATION("preferredLocation"),

        /** The preferred date. */
        PREFERRED_DATE("preferredDate"),

        /** The comments. */
        COMMENTS("comments"),

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
        SUBMIT_BUTTON("submitButtonLabel");

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
    @Inject
    private String title;

    /** The available trainings. */
    @Inject
    private String availableTrainings;

    /** The learning history. */
    @Inject
    private String learningHistory;

    /** The training material handouts. */
    @Inject
    private String trainingMaterialHandouts;

    /** The course description. */
    @Inject
    private String courseDescription;

    /** The principle objectives. */
    @Inject
    private String principleObjectives;

    /** The target groups. */
    @Inject
    private String targetGroups;

    /** The duration. */
    @Inject
    private String duration;

    /** The max participants. */
    @Inject
    private String maxParticipants;

    /** The knowledge requirements. */
    @Inject
    private String knowledgeRequirements;

    /** The no of participants. */
    @Inject
    private String noOfParticipants;

    /** The preferred location. */
    @Inject
    private String preferredLocation;

    /** The preferred date. */
    @Inject
    private String preferredDate;

    /** The comments. */
    @Inject
    private String comments;

    /** The confirmation text. */
    @Inject
    private String confirmationText;

    /** The submit button label. */
    @Inject
    private String submitButtonLabel;

    /** The input error message label. */
    @Inject
    private String inputErrorMsg;

    /** The success message. */
    @Inject
    private String successMessage;

    /** The subject. */
    @Inject
    private String subject;

    /** The salutation text in email. */
    @Inject
    private String salutation;

    /** The body text in email. */
    @Inject
    private String body;

    /** The training details api. */
    @Inject
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
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.NO_OF_PARTICIPANTS.getI18nJsonKey(), getNoOfParticipants());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_LOCATION.getI18nJsonKey(), getPreferredLocation());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_DATE.getI18nJsonKey(), getPreferredDate());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.COMMENTS.getI18nJsonKey(), getComments());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONFIRMATION_TEXT.getI18nJsonKey(), getConfirmationText());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.INPUT_ERROR_MESSAGE.getI18nJsonKey(), getInputErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUCCESS_MESSAGE.getI18nJsonKey(), getSuccessMessage());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(), getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBJECT.getI18nJsonKey(), getSubject());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SALUTATION.getI18nJsonKey(), getSalutation());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.BODY.getI18nJsonKey(), getBody());

        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
        LOGGER.debug("i18nKeys : {}", i18nKeys);

        this.setUserEmailAddress();
        if (request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME) != null) {
            this.userName = request.getCookie(CustomerHubConstants.CUSTOMER_COOKIE_NAME).getValue();
        }
        String apiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                CustomerHubConstants.AIP_PRODUCT_DETAILS_API);
        trainingDetailsApi = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), apiMapping,
                aipCategoryService.getAutomationTrainingsId());
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
     * Gets the no of participants.
     *
     * @return the no of participants
     */
    public String getNoOfParticipants() {
        return noOfParticipants;
    }

    /**
     * Gets the preferred location.
     *
     * @return the preferred location
     */
    public String getPreferredLocation() {
        return preferredLocation;
    }

    /**
     * Gets the preferred date.
     *
     * @return the preferred date
     */
    public String getPreferredDate() {
        return preferredDate;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
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

}
