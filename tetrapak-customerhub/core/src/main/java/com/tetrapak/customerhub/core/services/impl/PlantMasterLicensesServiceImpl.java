package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.licenses.EngineeringLicenseFormBean;
import com.tetrapak.customerhub.core.beans.licenses.SiteLicenseFormBean;
import com.tetrapak.customerhub.core.jobs.MyTetrapakEmailJob;
import com.tetrapak.customerhub.core.models.EngineeringLicenseModel;
import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.models.SiteLicenseModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.ArrayUtils;
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

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Class PlantMasterLicensesService Implementation.
 */
@Component(service = PlantMasterLicensesService.class, immediate = true,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = AIPEmailConfiguration.class)
public class PlantMasterLicensesServiceImpl implements PlantMasterLicensesService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesServiceImpl.class);
    // This is expected to be empty since full key names would be provided in Dialog.
    // However, this is still a provision to allow for prefixes, if authors don't specify keys with prefix
    private static final String I18N_PREFIX = StringUtils.EMPTY;
    private static final String VALUE = "Value";
    private static final String LICENSE_TYPE_ENGINEERING = "engineering";
    private static final String LICENSE_TYPE_REQUEST_PARAMETER = "licenseType";
    
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
    public boolean sendEmail(SlingHttpServletRequest request) throws IOException {
        LOGGER.debug("Inside sendEmail method of PlantMasterLicensesServiceImpl");
        boolean isSuccess = false;
        String[] recipients = config.recipientAddresses();
        if (request.getHeader(LICENSE_TYPE_REQUEST_PARAMETER) != null
                && request.getHeader(LICENSE_TYPE_REQUEST_PARAMETER).equals(LICENSE_TYPE_ENGINEERING)) {
            isSuccess = sendEmailEngineeringLicense(request, recipients);
        } else {
            isSuccess = sendEmailSiteLicense(request, recipients);
        }
        return isSuccess;
    }
    
    private Boolean sendEmailEngineeringLicense(SlingHttpServletRequest request, String[] recipients)
            throws IOException {
        EngineeringLicenseModel model = request.adaptTo(EngineeringLicenseModel.class);
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            EngineeringLicenseFormBean bean = createEngineeringLicenseFormBean(request);
            LOGGER.debug("form object : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractEngineeringLicenseModelProps(emailParams, model, request, I18N_PREFIX);
            extractEngineeringLicenseFormData(emailParams, bean);
            isSuccess = addEmailJob(recipients, emailParams, config.engineeringLicenseEmailTemplatePath());
        }
        return isSuccess;
        
    }
    
    private Boolean sendEmailSiteLicense(SlingHttpServletRequest request, String[] recipients) throws IOException {
        SiteLicenseModel model = request.adaptTo(SiteLicenseModel.class);
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            SiteLicenseFormBean bean = createSiteLicenseFormBean(request);
            Map<String, String> emailParams = new HashMap<>();
            extractSiteLicenseModelProps(emailParams, model, request, I18N_PREFIX);
            extractSiteLicenseFormData(emailParams, bean);
            isSuccess = addEmailJob(recipients, emailParams, config.siteLicenseEmailTemplatePath());
        }
        return isSuccess;
    }
    
    private void extractEngineeringLicenseModelProps(Map<String, String> emailParams, EngineeringLicenseModel model,
            SlingHttpServletRequest request, String prefix) {
        
        PlantMasterLicensesModel plantMasterLicensesModel = request.adaptTo(PlantMasterLicensesModel.class);
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY, getI18nValue(request, prefix, model.getComments()));
        emailParams.put("users", getI18nValue(request, prefix, model.getUsers()));
        emailParams.put("name", getI18nValue(request, prefix, plantMasterLicensesModel.getUserName()));
        emailParams.put("email", getI18nValue(request, prefix, plantMasterLicensesModel.getUserEmailAddress()));
        emailParams.put("subject", getI18nValue(request, prefix, model.getSubject()));
        emailParams.put("salutation", getI18nValue(request, prefix, model.getSalutation()));
        emailParams.put("body", getI18nValue(request, prefix, model.getBody()));
        
    }
    
    private void extractSiteLicenseModelProps(Map<String, String> emailParams, SiteLicenseModel model,
            SlingHttpServletRequest request, String prefix) {
        PlantMasterLicensesModel plantMasterLicensesModel = request.adaptTo(PlantMasterLicensesModel.class);
        emailParams.put("name", getI18nValue(request, prefix, plantMasterLicensesModel.getUserName()));
        emailParams.put("email", getI18nValue(request, prefix, plantMasterLicensesModel.getUserEmailAddress()));
        emailParams.put(SiteLicenseModel.NAME_OF_SITE_JSON_KEY, getI18nValue(request, prefix, model.getNameOfSite()));
        emailParams.put(SiteLicenseModel.LOCATION_OF_SITE_JSON_KEY,
                getI18nValue(request, prefix, model.getLocationOfSite()));
        emailParams.put(SiteLicenseModel.APPLICATION_JSON_KEY, getI18nValue(request, prefix, model.getApplication()));
        emailParams.put(SiteLicenseModel.PLC_TYPE_JSON_KEY, getI18nValue(request, prefix, model.getPlcType()));
        emailParams.put(SiteLicenseModel.HMI_TYPE_JSON_KEY, getI18nValue(request, prefix, model.getHmiType()));
        emailParams.put(SiteLicenseModel.MES_TYPE_JSON_KEY, getI18nValue(request, prefix, model.getMesType()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_JSON_KEY,
                getI18nValue(request, prefix, model.getNumberOfBasicUnit()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_JSON_KEY,
                getI18nValue(request, prefix, model.getNumberOfAdvancedUnit()));
        emailParams.put("subject", getI18nValue(request, prefix, model.getSubject()));
        emailParams.put("salutation", getI18nValue(request, prefix, model.getSalutation()));
        emailParams.put("body", getI18nValue(request, prefix, model.getBody()));
    }
    
    private void extractEngineeringLicenseFormData(Map<String, String> emailParams,
            EngineeringLicenseFormBean engineeringLicenseFormBean) {
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY + VALUE, engineeringLicenseFormBean.getComments());
        emailParams.put("users", getUsersForEmail());
    }
    
    private void extractSiteLicenseFormData(Map<String, String> emailParams, SiteLicenseFormBean siteLicenseFormBean) {
        emailParams.put(SiteLicenseModel.NAME_OF_SITE_JSON_KEY + VALUE, siteLicenseFormBean.getNameOfSite());
        emailParams.put(SiteLicenseModel.LOCATION_OF_SITE_JSON_KEY + VALUE, siteLicenseFormBean.getLocationOfSite());
        emailParams.put(SiteLicenseModel.APPLICATION_JSON_KEY + VALUE, siteLicenseFormBean.getApplication());
        emailParams.put(SiteLicenseModel.PLC_TYPE_JSON_KEY + VALUE, siteLicenseFormBean.getPlcType());
        emailParams.put(SiteLicenseModel.HMI_TYPE_JSON_KEY + VALUE, siteLicenseFormBean.getHmiType());
        emailParams.put(SiteLicenseModel.MES_TYPE_JSON_KEY + VALUE, siteLicenseFormBean.getMesType());
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_JSON_KEY + VALUE,
                siteLicenseFormBean.getNumberOfBasicUnit());
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_JSON_KEY + VALUE,
                siteLicenseFormBean.getNumberOfAdvancedUnit());
    }
    /*
     * public EngineeringLicenseFormBean createEngineeringLicenseFormBean(SlingHttpServletRequest request) { JsonObject
     * requestParameterJson = convertToJson(request.getParameterMap()); LOGGER.debug(requestParameterJson.toString());
     * Gson gson = new Gson(); return gson.fromJson(requestParameterJson,EngineeringLicenseFormBean.class ); }
     */
    
    public EngineeringLicenseFormBean createEngineeringLicenseFormBean(ServletRequest request) throws IOException {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        LOGGER.debug(requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, EngineeringLicenseFormBean.class);
    }
    
    public SiteLicenseFormBean createSiteLicenseFormBean(ServletRequest request) throws IOException {
        // JsonObject requestParameterJson = convertToJson(request.getParameterMap());
        String requestParameterJson = request.getReader().lines().collect(Collectors.joining());
        LOGGER.debug("2 " + requestParameterJson);
        Gson gson = new Gson();
        return gson.fromJson(requestParameterJson, SiteLicenseFormBean.class);
    }
    
    public JsonObject convertToJson(Map<String, String[]> requestParameterMap) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(requestParameterMap);
        LOGGER.debug("1. " + jsonString);
        jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        jsonObject = replaceArraysInJsonObject(jsonObject);
        return jsonObject;
    }
    
    private boolean addEmailJob(String[] recipients, Map<String, String> emailParams, String templatePath) {
        LOGGER.debug("Email job added");
        for (String param : emailParams.keySet()) {
            LOGGER.debug(param + " === " + emailParams.get(param));
        }
        LOGGER.debug("recipients : " + ArrayUtils.toString(config.recipientAddresses()));
        boolean isSuccess = false;
        Map<String, Object> properties = new HashMap<>();
        properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, templatePath);
        properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
        properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, recipients);
        if (jobMgr != null && config.isLicensesEmailEnabled() && config.recipientAddresses() != null) {
            LOGGER.debug("Email feature enabled");
            jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
            isSuccess = true;
        } else {
            LOGGER.error("Error in setting up pre-requisites for Email job.");
        }
        return isSuccess;
    }
    
    private JsonObject replaceArraysInJsonObject(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            jsonObject.addProperty(key, jsonObject.get(key).getAsString());
        }
        return jsonObject;
    }
    
    public String getI18nValue(SlingHttpServletRequest request, String i18nKey, String prefix) {
        return GlobalUtil.getI18nValue(request, prefix, i18nKey);
    }
    
    private String getUsersForEmail() {
        
        return "<h1>TODO</h1>";
    }
}
