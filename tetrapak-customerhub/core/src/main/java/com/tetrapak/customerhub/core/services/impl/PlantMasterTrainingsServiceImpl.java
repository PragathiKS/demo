package com.tetrapak.customerhub.core.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.PlantMasterTrainingsFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.jobs.MyTetrapakEmailJob;
import com.tetrapak.customerhub.core.models.PlantMasterTrainingsModel;
import com.tetrapak.customerhub.core.services.PlantMasterTrainingsService;
import com.tetrapak.customerhub.core.services.config.PlantMasterEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;

/**
 * The Class CotsSupportService Implementation.
 */
@Component(service = PlantMasterTrainingsService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = PlantMasterEmailConfiguration.class)
public class PlantMasterTrainingsServiceImpl implements PlantMasterTrainingsService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterTrainingsServiceImpl.class);
    
    /** The Constant I18N_PREFIX. */
    private static final String I18N_PREFIX = StringUtils.EMPTY;

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    /** The xss API. */
    @Reference
    private XSSAPI xssAPI;

    /** The config. */
    private PlantMasterEmailConfiguration config;

    /**
     * Activate.
     * 
     * @param config
     *            the config
     */
    @Activate
    public void activate(final PlantMasterEmailConfiguration config) {
        this.config = config;
    }

    /**
     * Send email.
     *
     * @param trainingName
     *            the training name
     * @param plantMasterTrainingsFormBean
     *            the plant master trainings form bean
     * @param request
     *            the request
     * @return true, if successful
     */
    @Override
    public boolean sendEmail(PlantMasterTrainingsFormBean plantMasterTrainingsFormBean,
            SlingHttpServletRequest request) {
        LOGGER.debug("Inside sendEmail method of PlantMasterTrainingsServiceImpl");
        PlantMasterTrainingsModel model = request.adaptTo(PlantMasterTrainingsModel.class);
        boolean isSuccess = false;
        boolean isFeatureEnabled = config.isPlantMasterEmailServiceEnabled();
        String[] recipientEmailFromOsgiConfig = config.plantMasterRecipientAddresses();
        if (Objects.nonNull(recipientEmailFromOsgiConfig)) {
            Map<String, String> emailParams = new HashMap<>();

            extractPlantMasterTrainingsModelProps(emailParams, model, request, I18N_PREFIX);
            extractFormData(emailParams, plantMasterTrainingsFormBean);

            Map<String, Object> properties = new HashMap<>();
            properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, config.plantMasterEmailTemplatePath());
            properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
            properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, recipientEmailFromOsgiConfig);

            if (Objects.nonNull(jobMgr) && isFeatureEnabled) {
                LOGGER.debug("Email feature enabled");
                jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
                isSuccess = Boolean.TRUE;
            } else {
                LOGGER.error("Error in setting up pre-requisites for Email job.");
            }
        }
        LOGGER.debug("Exiting sendEmail method of PlantMasterTrainingsServiceImpl");
        return isSuccess;
    }

    /**
     * Extract properties from PlantMasterTrainingsModel passed as input.
     *
     * @param emailParams
     *            the email params
     * @param model
     *            the model
     * @param request
     *            the request
     * @param prefix
     *            the prefix
     */
    private void extractPlantMasterTrainingsModelProps(Map<String, String> emailParams, PlantMasterTrainingsModel model,
            SlingHttpServletRequest request, String prefix) {

        String salutation = StringUtils.isNotEmpty(model.getSalutation())
                ? getI18nValue(request, prefix, model.getSalutation())
                : StringUtils.EMPTY;
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.SALUTATION.i18nJsonKey,
                getI18nValue(request, prefix, salutation));

        String body = StringUtils.isNotEmpty(model.getBody()) ? getI18nValue(request, prefix, model.getBody())
                : StringUtils.EMPTY;
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.BODY.i18nJsonKey,
                getI18nValue(request, prefix, body));

        String subject = StringUtils.isNotEmpty(model.getSubject()) ? getI18nValue(request, prefix, model.getSubject())
                : StringUtils.EMPTY;
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.SUBJECT.i18nJsonKey,
                getI18nValue(request, prefix, subject));

        emailParams.put(
                PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.NO_OF_PARTICIPANTS_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getNoOfParticipantsLabel()));

        emailParams.put(
                PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.PREFERRED_LOCATION_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getPreferredLocationLabel()));

        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.PREFERRED_DATE_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getPreferredDateLabel()));

        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.COMMENTS_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getCommentsLabel()));
        
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.CONTACT_DETAILS.i18nJsonKey,
                getI18nValue(request, prefix, model.getContactDetails()));
        
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.CONSENT_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getConsentLabel()));
        
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.FORM_TITLE.i18nJsonKey,
                getI18nValue(request, prefix, model.getFormTitle()));
        
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.TRAINING_ID_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getTrainingIdLabel()));
        
        emailParams.put(PlantMasterTrainingsModel.PlantMasterTrainingsComponentDialog.TRAINING_NAME_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getTrainingNameLabel()));
    }

    /**
     * Extract properties from PlantMasterTrainingsFormBean passed as input.
     *
     * @param emailParams
     *            the email params
     * @param plantMasterTrainingsFormBean
     *            the plant master trainings form bean
     */
    private void extractFormData(Map<String, String> emailParams,
            PlantMasterTrainingsFormBean plantMasterTrainingsFormBean) {
        emailParams.put(PlantMasterTrainingsFormBean.TRAINING_ID + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getTrainingId());
        emailParams.put(PlantMasterTrainingsFormBean.TRAINING_NAME + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getTrainingName());
        emailParams.put(PlantMasterTrainingsFormBean.NO_OF_PARTICIPANTS + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getNoOfParticipants());
        emailParams.put(PlantMasterTrainingsFormBean.PREFERRED_LOCATION + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getPreferredLocation());
        emailParams.put(PlantMasterTrainingsFormBean.PREFERRED_DATE + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getPreferredDate());
        emailParams.put(PlantMasterTrainingsFormBean.COMMENTS_PARAMETER + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getComments());
        emailParams.put(PlantMasterTrainingsFormBean.CONSENT_PARAMETER + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getConsent());
        emailParams.put(PlantMasterTrainingsFormBean.NAME_PARAMETER + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getName());
        emailParams.put(PlantMasterTrainingsFormBean.EMAIL_ADDRESS_PARAMETER + CustomerHubConstants.VALUE,
                plantMasterTrainingsFormBean.getEmailAddress());
    }

    /**
     * Creates the plant master trainings form bean.
     *
     * @param request
     *            the request
     * @return the plant master trainings form bean
     */
    public PlantMasterTrainingsFormBean createPlantMasterTrainingsFormBean(SlingHttpServletRequest request) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(request.getParameterMap());
        jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
        JsonObject jsonObject = replaceArraysInJsonObject(gson.fromJson(jsonString, JsonObject.class));
        LOGGER.debug("JSON Object {}", jsonObject);
        return gson.fromJson(jsonObject, PlantMasterTrainingsFormBean.class);
    }
    
    /**
     * Replace arrays in json object.
     *
     * @param jsonObject
     *            the json object
     * @return the json object
     */
    private JsonObject replaceArraysInJsonObject(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            jsonObject.addProperty(key, jsonObject.get(key).getAsString());
        }
        return jsonObject;
    }

    /**
     * Gets the i18n value.
     *
     * @param request
     *            the request
     * @param i18nKey
     *            the i18n key
     * @param prefix
     *            the prefix
     * @return the i18n value
     */
    public String getI18nValue(SlingHttpServletRequest request, String i18nKey, String prefix) {
        return GlobalUtil.getI18nValue(request, prefix, i18nKey);
    }
}
