package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.servlets.PlantMasterTrainingsEmailServlet;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The Class PlantMasterTrainingsModel.
 */
@Model(
        adaptables = { Resource.class, SlingHttpServletRequest.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlantMasterTrainingsModel {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterTrainingsModel.class);
    public static final String GROUP_SERVLET_URL_POSTFIX= ".plantmaster.json";
    private static final String LEARNING_HISTORY_CHILD_RESOURCE_NAME = "learningHistory";

    /**
     * The Enum PlantMasterTrainingsComponentDialog.
     */
    public enum PlantMasterTrainingsComponentDialog {

        /** The title. */
        TITLE("title"),
        
        /** The subTitle. */
        SUBTITLE("subTitle"),

        /** The available trainings. */
        AVAILABLE_TRAININGS("availableTrainings"),

        /** The learning history text. */
        LEARNING_HISTORY_TEXT("learninghistorytext"),

        /** The diploma. */
        DIPLOMA_TEXT("diplomatext"),

        /** The accredited. */
        ACCREDITED_TEXT("accreditedtext"),

        /** The authenticated. */
        AUTHENTICATED_TEXT("authenticatedtext"),

        /** The user text. */
        USER_TEXT("usertext"),

        /** The item text. */
        ITEM_TEXT("itemtext"),

        /** The completion date text. */
        COMPLETION_DATE_TEXT("completiondatetext"),

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
        
        /** The hours. */
        HOURS("hours"),

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

        /** The preferred date. */
        PREFERRED_DATE_PLACEHOLDER("preferredDatePlaceholder"),

        /** The comments. */
        COMMENTS_LABEL("commentsLabel"),

        /** The confirmation text. */
        CONFIRMATION_TEXT("confirmationText"),

        /** The input error message. */
        INPUT_ERROR_MESSAGE("inputErrorMsg"),

        /** The input format error message. */
        INPUT_FORMAT_ERROR_MESSAGE("inputFormatErrorMsg"),

        /** The number field error message. */
        NUMBER_FIELD_ERROR_MESSAGE("numberFieldErrorMsg"),

        /** The confirmation error message. */
        CONFIRMATION_ERROR_MESSAGE("confirmationErrorMsg"),

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

    /** The is publish environment. */
    private boolean isPublishEnvironment = Boolean.FALSE;

    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The training material handouts. */
    @ValueMapValue
    private String trainingMaterialHandouts;

    /** The training details api. */
    @ValueMapValue
    private String trainingDetailsApi;
    
    /** The subtitle */
    @ValueMapValue
    private String subTitle;

    /** The apigee service. */
    @OSGiService
    private APIGEEService apigeeService;

    /** The aip category service. */
    @OSGiService
    private AIPCategoryService aipCategoryService;

    @ChildResource(name = LEARNING_HISTORY_CHILD_RESOURCE_NAME)
    private PlantMasterLearningHistoryModel plantMasterLearningHistoryModel;

    @Inject
    @Self
    private PlantMasterEngineeringTrainingsModel engineeringTrainingsModel;

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

    /** The learning history api. */
    private String learningHistoryApi;

    /**
     * Checks if is publish environment.
     *
     * @return true, if is publish environment
     */
    public boolean isPublishEnvironment() {
        return isPublishEnvironment;
    }

    /**
     * init method.
     */
    @PostConstruct
    protected void init() {

        Map<String, Object> i18KeyMap = getPlantMasterTrainingI18KeyMap();
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

        if (slingSettingsService.getRunModes().contains("publish")) {
            isPublishEnvironment = Boolean.TRUE;
        }

        final String productDetailsApiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                CustomerHubConstants.AIP_PRODUCT_DETAILS_API);
        final String licenseHistoryApiMapping = GlobalUtil.getSelectedApiMapping(apigeeService,
                CustomerHubConstants.AIP_LEARNING_HISTORY_API);
        trainingDetailsApi = GlobalUtil.getAIPEndpointURL(apigeeService.getApigeeServiceUrl(), productDetailsApiMapping,
                aipCategoryService.getAutomationTrainingsId());
        LOGGER.debug("Engineering Trainings API : {}", trainingDetailsApi);
        learningHistoryApi = GlobalUtil.getAPIEndpointURL(apigeeService.getApigeeServiceUrl(),licenseHistoryApiMapping);
        LOGGER.debug("Learning history API : {}", learningHistoryApi);
    }

    private Map<String, Object> getPlantMasterTrainingI18KeyMap(){
        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TITLE.getI18nJsonKey(), engineeringTrainingsModel.getTitle());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.AVAILABLE_TRAININGS.getI18nJsonKey(),
                engineeringTrainingsModel.getAvailableTrainings());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_MATERIAL_HANDOUTS.getI18nJsonKey(),
                getTrainingMaterialHandouts());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.COURSE_DESCRIPTION.getI18nJsonKey(),
                engineeringTrainingsModel.getCourseDescription());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PRINCIPLE_OBJECTIVES.getI18nJsonKey(),
                engineeringTrainingsModel.getPrincipleObjectives());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TARGET_GROUPS.getI18nJsonKey(), engineeringTrainingsModel.getTargetGroups());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.DURATION.getI18nJsonKey(), engineeringTrainingsModel.getDuration());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.HOURS.getI18nJsonKey(), engineeringTrainingsModel.getHours());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.MAX_PARTICIPANTS.getI18nJsonKey(),
                engineeringTrainingsModel.getMaxParticipants());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.KNOWLEDGE_REQUIREMENTS.getI18nJsonKey(),
                engineeringTrainingsModel.getKnowledgeRequirements());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.NO_OF_PARTICIPANTS_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getNoOfParticipantsLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_LOCATION_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getPreferredLocationLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_DATE_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getPreferredDateLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.PREFERRED_DATE_PLACEHOLDER.getI18nJsonKey(),
                engineeringTrainingsModel.getPreferredDatePlaceholder());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.COMMENTS_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getCommentsLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONFIRMATION_TEXT.getI18nJsonKey(),
                engineeringTrainingsModel.getConfirmationText());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(),
                engineeringTrainingsModel.getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.INPUT_ERROR_MESSAGE.getI18nJsonKey(),
                engineeringTrainingsModel.getInputErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.INPUT_FORMAT_ERROR_MESSAGE.getI18nJsonKey(),
                engineeringTrainingsModel.getInputFormatErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.NUMBER_FIELD_ERROR_MESSAGE.getI18nJsonKey(),
                engineeringTrainingsModel.getNumberFieldErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONFIRMATION_ERROR_MESSAGE.getI18nJsonKey(),
                engineeringTrainingsModel.getConfirmationErrorMsg());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUCCESS_MESSAGE.getI18nJsonKey(),
                engineeringTrainingsModel.getSuccessMessage());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBMIT_BUTTON.getI18nJsonKey(),
                engineeringTrainingsModel.getSubmitButtonLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SUBJECT.getI18nJsonKey(),
                engineeringTrainingsModel.getSubject());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.SALUTATION.getI18nJsonKey(),
                engineeringTrainingsModel.getSalutation());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.BODY.getI18nJsonKey(),
                engineeringTrainingsModel.getBody());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONTACT_DETAILS.getI18nJsonKey(),
                engineeringTrainingsModel.getContactDetails());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_ID_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getTrainingIdLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.TRAINING_NAME_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getTrainingNameLabel());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.FORM_TITLE.getI18nJsonKey(),
                engineeringTrainingsModel.getFormTitle());
        i18KeyMap.put(PlantMasterTrainingsComponentDialog.CONSENT_LABEL.getI18nJsonKey(),
                engineeringTrainingsModel.getConsentLabel());
        i18KeyMap.put(LEARNING_HISTORY_CHILD_RESOURCE_NAME,
                plantMasterLearningHistoryModel);
        return i18KeyMap;
    }

    /**
     * Sets the user email address.
     */
    public void setUserEmailAddress() {
        this.userEmailAddress = GlobalUtil.getCustomerEmailAddress(this.request);
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
     * Gets the training details api.
     *
     * @return the training details api
     */
    public String getTrainingDetailsApi() {
        return trainingDetailsApi;
    }

    /**
     * Gets the learning history api.
     *
     * @return the learning history api
     */
    public String getLearningHistoryApi() {
        return learningHistoryApi;
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

    /**
     * Gets the groups servlet path to read permissions for user
     *
     * @return the component path extension
     */
    public String getGroupServletUrl() {
        return resource.getPath() + GROUP_SERVLET_URL_POSTFIX;
    }
    
    public String getSubTitle() {
        return subTitle;
    }
    
}
