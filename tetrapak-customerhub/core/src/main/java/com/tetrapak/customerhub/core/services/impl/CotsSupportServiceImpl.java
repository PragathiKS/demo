package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.jobs.MyTetrapakEmailJob;
import com.tetrapak.customerhub.core.models.CotsSupportModel;
import com.tetrapak.customerhub.core.services.CotsSupportService;
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
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The Class CotsSupportService Implementation.
 */
@Component(service = CotsSupportService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = AIPEmailConfiguration.class)
public class CotsSupportServiceImpl implements CotsSupportService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportServiceImpl.class);
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
    public boolean sendEmail(List<Map<String, String>> attachments, CotsSupportFormBean cotsSupportFormBean,
            SlingHttpServletRequest request) {
        LOGGER.debug("Inside sendEmail method of CotsSupportServiceImpl");
        CotsSupportModel model = request.adaptTo(CotsSupportModel.class);
        boolean isSuccess = false;
        boolean isFeatureEnabled = config.isCotsSupportEmailEnabled();
        String[] recipientEmailFromOsgiConfig = config.recipientAddresses();
        if (Objects.nonNull(recipientEmailFromOsgiConfig)) {
            Map<String, String> emailParams = new HashMap<>();
            extractCotsSupportModelProps(emailParams, model, request, I18N_PREFIX);
            extractFormData(emailParams, cotsSupportFormBean);
            Map<String, Object> properties = new HashMap<>();
            properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, config.emailTemplatePath());
            properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
            properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, recipientEmailFromOsgiConfig);
            properties.put(MyTetrapakEmailJob.ATTACHMENTS, attachments);
            if (jobMgr != null && isFeatureEnabled && recipientEmailFromOsgiConfig != null) {
                LOGGER.debug("Email feature enabled");
                jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
                isSuccess = true;
            } else {
                LOGGER.error("Error in setting up pre-requisites for Email job.");
            }
        }
        return isSuccess;
    }
    
    /**
     * Extract properties from CotsSupporModel passed as input
     * @param emailParams
     * @param model
     * @param request
     * @param prefix
     */
    private void extractCotsSupportModelProps(Map<String, String> emailParams, CotsSupportModel model,
            SlingHttpServletRequest request, String prefix) {
        String salutation = StringUtils.isNotEmpty(model.getSalutation()) ?
                getI18nValue(request, prefix, model.getSalutation()) :
                StringUtils.EMPTY;
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SALUTATION.i18nJsonKey,
                getI18nValue(request, prefix, salutation));
        String body = StringUtils.isNotEmpty(model.getBody()) ?
                getI18nValue(request, prefix, model.getBody()) :
                StringUtils.EMPTY;
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.BODY.i18nJsonKey,
                getI18nValue(request, prefix, body));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CONTACT_DETAILS.i18nJsonKey,
                getI18nValue(request, prefix, model.getContactDetails()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SELECT_REQUEST.i18nJsonKey,
                getI18nValue(request, prefix, model.getSelectRequest()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.COMPANY.i18nJsonKey,
                getI18nValue(request, prefix, model.getCompany()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CUSTOMER_SITE.i18nJsonKey,
                getI18nValue(request, prefix, model.getCustomerSite()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.AFFECTED_SYSTEMS_LABEL.i18nJsonKey,
                getI18nValue(request, prefix, model.getAffectedSystemsLabel()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.PRODUCT_INVOLVED.i18nJsonKey,
                getI18nValue(request, prefix, model.getProductInvolvedLabel()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SOFTWARE_VERSION.i18nJsonKey,
                getI18nValue(request, prefix, model.getSoftwareVersionEmailLabel()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.ENGINEERING_LICENSE_SERIAL_NUMBER.i18nJsonKey,
                getI18nValue(request, prefix, model.getLicenseNumberEmailLabel()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SHORT_DESCRIPTION.i18nJsonKey,
                getI18nValue(request, prefix, model.getShortDescription()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CONTACT_DETAILS.i18nJsonKey,
                getI18nValue(request, prefix, model.getContactDetails()));
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.QUESTION.i18nJsonKey,
                getI18nValue(request, prefix, model.getQuestion()));
    }
    
    
    /**
     * Extract properties from CotsSupportFormBean passed as input
     * @param emailParams
     * @param cotsSupportFormBean
     */
    private void extractFormData(Map<String, String> emailParams, CotsSupportFormBean cotsSupportFormBean) {
        emailParams.put(CotsSupportFormBean.QUERY_TYPE_PARAMETER + VALUE, cotsSupportFormBean.getLogQueryType());
        emailParams.put(CotsSupportFormBean.COMPANY_PARAMETER + VALUE, cotsSupportFormBean.getCompany());
        emailParams.put(CotsSupportFormBean.CUSTOMER_SITE_PARAMETER + VALUE, cotsSupportFormBean.getCustomerSite());
        emailParams.put(CotsSupportFormBean.AFFECTED_SYSTEMS_PARAMETER + VALUE,
                cotsSupportFormBean.getAffectedSystems());
        emailParams.put(CotsSupportFormBean.PRODUCT_INVOLVED_PARAMETER + VALUE,
                cotsSupportFormBean.getProductInvolved());
        emailParams.put(CotsSupportFormBean.SOFTWARE_VERSION_PARAMETER + VALUE,
                cotsSupportFormBean.getSoftwareVersion());
        emailParams.put(CotsSupportFormBean.LICENSE_NUMBER_PARAMETER + VALUE, cotsSupportFormBean.getLicenseNumber());
        emailParams.put(CotsSupportFormBean.DESCRIPTION_PARAMETER + VALUE, cotsSupportFormBean.getDescription());
        emailParams.put(CotsSupportFormBean.QUESTIONS_PARAMETER + VALUE, cotsSupportFormBean.getQuestions());
        emailParams.put(CotsSupportFormBean.NAME_PARAMETER + VALUE, cotsSupportFormBean.getName());
        emailParams.put(CotsSupportFormBean.EMAIL_ADDRESS_PARAMETER + VALUE, cotsSupportFormBean.getEmailAddress());
        emailParams.put(CotsSupportFormBean.TELEPHONE_PARAMETER + VALUE, cotsSupportFormBean.getTelephone());
    }
    
    public CotsSupportFormBean createCotsSupportFormBean(SlingHttpServletRequest request) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(request.getParameterMap());
        jsonString = xssAPI.getValidJSON(jsonString, StringUtils.EMPTY);
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        if (jsonObject.has(CotsSupportFormBean.FILES_PARAMETER)) {
            jsonObject.remove(CotsSupportFormBean.FILES_PARAMETER);
        }
        jsonObject = replaceArraysInJsonObject(jsonObject);
        LOGGER.debug(jsonObject.toString());
        return gson.fromJson(jsonObject, CotsSupportFormBean.class);
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
