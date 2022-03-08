package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.licenses.EngineeringLicenseFormBean;
import com.tetrapak.customerhub.core.beans.licenses.SiteLicenseFormBean;
import com.tetrapak.customerhub.core.models.EngineeringLicenseModel;
import com.tetrapak.customerhub.core.models.SiteLicenseModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * The Class PlantMasterLicensesService Implementation.
 */
@Component(service = PlantMasterLicensesService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
//@Designate(ocd = AIPEmailConfiguration.class)
public class PlantMasterLicensesServiceImpl implements PlantMasterLicensesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesServiceImpl.class);
    // This is expected to be empty since full key names would be provided in Dialog.
    // However, this is still a provision to allow for prefixes, if authors don't specify keys with prefix
    private static final String I18N_PREFIX = StringUtils.EMPTY;
    private static final String VALUE = "Value";

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    @Reference
    private XSSAPI xssAPI;

    /** The config. */
    private AIPEmailConfiguration config;

    /**
     * Activate.
     * @param config the config
     */
    @Activate
    public void activate(final AIPEmailConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean sendEmail(SlingHttpServletRequest request) {
        LOGGER.debug("Inside sendEmail method of CotsSupportServiceImpl");
        boolean isSuccess = false;
        boolean isFeatureEnabled = config.isCotsSupportEmailEnabled();
        String[] recipients = config.recipientAddresses();
        if(request.getHeader("licenseType").equals("engineering")){
            isSuccess = sendEmailEngineeringLicense(request,isFeatureEnabled,recipients);
        }
        else{
            isSuccess = sendEmailSiteLicense(request,isFeatureEnabled,recipients);
        }
        return isSuccess;
    }

    private Boolean sendEmailEngineeringLicense(SlingHttpServletRequest request,boolean isFeatureEnabled,String[] recipients){
        EngineeringLicenseModel model = request.adaptTo(EngineeringLicenseModel.class);
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            EngineeringLicenseFormBean bean = createEngineeringLicenseFormBean(request);
            Map<String, String> emailParams = new HashMap<>();
            extractEngineeringLicenseModelProps(emailParams, model, request, I18N_PREFIX);
            extractEngineeringLicenseFormData(emailParams,bean);
            isSuccess = addEmailJob(recipients,emailParams);
        }
        return isSuccess;

    }

    private Boolean sendEmailSiteLicense(SlingHttpServletRequest request,boolean isFeatureEnabled,String[] recipients){
        SiteLicenseModel model = request.adaptTo(SiteLicenseModel.class);
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            SiteLicenseFormBean bean = createSiteLicenseFormBean(request);
            Map<String, String> emailParams = new HashMap<>();
            extractSiteLicenseModelProps(emailParams, model, request, I18N_PREFIX);
            extractSiteLicenseFormData(emailParams,bean);
            isSuccess = addEmailJob(recipients,emailParams);
        }
        return isSuccess;
    }

    private void extractEngineeringLicenseModelProps(Map<String, String> emailParams, EngineeringLicenseModel model,
            SlingHttpServletRequest request, String prefix){

        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY,
                getI18nValue(request, prefix, model.getComments()));

    }
    private void extractSiteLicenseModelProps(Map<String, String> emailParams, SiteLicenseModel model,
            SlingHttpServletRequest request, String prefix){
        emailParams.put(SiteLicenseModel.APPLICATION_JSON_KEY,
                getI18nValue(request, prefix, model.getApplication()));
    }
    private void extractEngineeringLicenseFormData(Map<String, String> emailParams, EngineeringLicenseFormBean engineeringLicenseFormBean){
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY + VALUE, engineeringLicenseFormBean.getComments());
    }
    private void extractSiteLicenseFormData(Map<String, String> emailParams, SiteLicenseFormBean siteLicenseFormBean){
        emailParams.put(SiteLicenseModel.APPLICATION_JSON_KEY + VALUE, siteLicenseFormBean.getApplication());
    }
    public EngineeringLicenseFormBean createEngineeringLicenseFormBean(SlingHttpServletRequest request) {
        JsonObject requestParameterJson = convertToJson(request.getParameterMap());
        LOGGER.debug(requestParameterJson.toString());
        Gson gson = new Gson();
        return gson.fromJson(requestParameterJson,EngineeringLicenseFormBean.class );
    }
    public SiteLicenseFormBean createSiteLicenseFormBean(SlingHttpServletRequest request) {
        JsonObject requestParameterJson = convertToJson(request.getParameterMap());
        LOGGER.debug(requestParameterJson.toString());
        Gson gson = new Gson();
        return gson.fromJson(requestParameterJson,SiteLicenseFormBean.class );
    }

    public JsonObject convertToJson(Map<String, String[]> requestParameterMap){
        Gson gson = new Gson();
        String jsonString = gson.toJson(requestParameterMap);
        jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        jsonObject = replaceArraysInJsonObject(jsonObject);
        return jsonObject;
    }
    private boolean addEmailJob( String[] recipients,Map<String, String> emailParams){
        LOGGER.debug("Email job added");
        boolean isSuccess = false;
/*        Map<String, Object> properties = new HashMap<>();
        properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, config.emailTemplatePath());
        properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
        properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, recipients);
        if (jobMgr != null && isFeatureEnabled && recipientEmailFromOsgiConfig != null) {
            LOGGER.debug("Email feature enabled");
            jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
            isSuccess = true;
        } else {
            LOGGER.error("Error in setting up pre-requisites for Email job.");
        }*/
        return isSuccess;
    }

    private JsonObject replaceArraysInJsonObject(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            jsonObject.addProperty(key, jsonObject.get(key).getAsString());
        }
        return jsonObject;
    }

    public String getI18nValue(SlingHttpServletRequest request,String i18nKey,
            String prefix){
        return GlobalUtil.getI18nValue(request, prefix, i18nKey);
    }
}
