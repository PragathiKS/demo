package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.licenses.EngineeringLicenseFormBean;
import com.tetrapak.customerhub.core.beans.licenses.SiteLicenseFormBean;
import com.tetrapak.customerhub.core.jobs.MyTetrapakEmailJob;
import com.tetrapak.customerhub.core.models.EngineeringLicenseModel;
import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.models.SiteLicenseModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import com.tetrapak.customerhub.core.services.config.PlantMasterLicensesEmailConfiguration;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.event.jobs.JobManager;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The Class PlantMasterLicensesService Implementation.
 */
@Component(service = PlantMasterLicensesService.class, immediate = true,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = PlantMasterLicensesEmailConfiguration.class)
public class PlantMasterLicensesServiceImpl implements PlantMasterLicensesService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantMasterLicensesServiceImpl.class);
    // This is expected to be empty since full key names would be provided in Dialog.
    // However, this is still a provision to allow for prefixes, if authors don't specify keys with prefix
    private static final String I18N_PREFIX = StringUtils.EMPTY;
    private static final String VALUE = "Value";
    private static final String LICENSE_TYPE_ENGINEERING = "engineering";
    private static final String LICENSE_TYPE_REQUEST_PARAMETER = "licenseType";
    private static final String HIDE_SUFFIX = "HideClass";
    private static final String HIDE_CSS_CLASS = "hide";
    private static final String USERS_HTML_NAME = "<tr><td class='license-list'> NAME </td>";
    private static final String USERS_HTML_DATE = "<td class='license-list'> DATE </td> ";
    private static final String USERS_HTML_LICENSES = "<td class='license-list'> \tLICENSES </td></tr><tr><td colspan='3'>&nbsp;</td></tr>";
    
    /** The job mgr. */
    @Reference
    private JobManager jobMgr;
    
    /** The config. */
    private PlantMasterLicensesEmailConfiguration config;
    
    /**
     * Activate.
     * @param config the config
     */
    @Activate
    public void activate(final PlantMasterLicensesEmailConfiguration config) {
        this.config = config;
    }
    
    @Override
    public boolean sendEmail(SlingHttpServletRequest request) throws IOException {
        LOGGER.debug("Inside sendEmail method of PlantMasterLicensesServiceImpl");
        boolean isSuccess = false;
        String[] recipients = config.recipientAddresses();
        if (LICENSE_TYPE_ENGINEERING.equals(request.getHeader(LICENSE_TYPE_REQUEST_PARAMETER))) {
            isSuccess = sendEmailEngineeringLicense(request, recipients);
        } else {
            isSuccess = sendEmailSiteLicense(request, recipients);
        }
        return isSuccess;
    }
    
    /**
     * Send email for Engineering license type
     * @param request
     * @param recipients
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailEngineeringLicense(SlingHttpServletRequest request, String[] recipients)
            throws IOException {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            EngineeringLicenseFormBean bean = createEngineeringLicenseFormBean(request);
            LOGGER.debug("Submitted form data object : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractEngineeringLicenseModelProps(emailParams, request, I18N_PREFIX);
            extractEngineeringLicenseFormData(emailParams, bean);
            isSuccess = addEmailJob(emailParams, config.engineeringLicenseEmailTemplatePath());
        } else {
            LOGGER.debug("Recipient email address missing in configuration!");
        }
        return isSuccess;
        
    }
    
    /**
     * Send email for Site license type
     * @param request
     * @param recipients
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailSiteLicense(SlingHttpServletRequest request, String[] recipients) throws IOException {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            SiteLicenseFormBean bean = createSiteLicenseFormBean(request);
            LOGGER.debug("Submitted form data object : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractSiteLicenseModelProps(emailParams, request, I18N_PREFIX);
            extractSiteLicenseFormData(emailParams, bean);
            isSuccess = addEmailJob(emailParams, config.siteLicenseEmailTemplatePath());
        }else {
            LOGGER.debug("Recipient email address missing in configuration!");
        }
        return isSuccess;
    }
    
    /**
     * Extract labels to be used in email for engineering license type
     * @param emailParams
     * @param request
     * @param prefix
     */
    private void extractEngineeringLicenseModelProps(Map<String, String> emailParams, SlingHttpServletRequest request,
            String prefix) {
        PlantMasterLicensesModel plantMasterLicensesModel = request.adaptTo(PlantMasterLicensesModel.class);
        EngineeringLicenseModel model = plantMasterLicensesModel.getEngineeringLicenseModel();
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY, getI18nValue(request, prefix, model.getComments()));
        emailParams.put(EngineeringLicenseModel.USERS_EMAIL_LABEL, getI18nValue(request, prefix, model.getUsers()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_USER_NAME,
                getI18nValue(request, prefix, model.getLicenseTableUserName()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_ACTIVATION_DATE,
                getI18nValue(request, prefix, model.getLicenseTableActivationDate()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_LIST_OF_LICENSES,
                getI18nValue(request, prefix, model.getLicenseTableListOfLicenses()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME,
                getI18nValue(request, prefix, plantMasterLicensesModel.getUsername()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME + VALUE,
                getI18nValue(request, prefix, plantMasterLicensesModel.getUserNameValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(request, prefix, plantMasterLicensesModel.getEmailAddressValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS,
                getI18nValue(request, prefix, plantMasterLicensesModel.getEmailaddress()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SUBJECT, getI18nValue(request, prefix, model.getSubject()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SALUTATION,
                getI18nValue(request, prefix, model.getSalutation()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(request, prefix, model.getBody()));
    }
    
    /**
     * Extract labels to be used in email for site license type
     * @param emailParams
     * @param request
     * @param prefix
     */
    private void extractSiteLicenseModelProps(Map<String, String> emailParams, SlingHttpServletRequest request,
            String prefix) {
        PlantMasterLicensesModel plantMasterLicensesModel = request.adaptTo(PlantMasterLicensesModel.class);
        SiteLicenseModel model = plantMasterLicensesModel.getSiteLicenseModel();
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME,
                getI18nValue(request, prefix, plantMasterLicensesModel.getUsername()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME + VALUE,
                getI18nValue(request, prefix, plantMasterLicensesModel.getUserNameValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(request, prefix, plantMasterLicensesModel.getEmailAddressValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS,
                getI18nValue(request, prefix, plantMasterLicensesModel.getEmailaddress()));
        emailParams.put(SiteLicenseModel.NAME_OF_SITE_KEY, getI18nValue(request, prefix, model.getNameOfSite()));
        emailParams.put(SiteLicenseModel.LOCATION_OF_SITE_KEY,
                getI18nValue(request, prefix, model.getLocationOfSite()));
        emailParams.put(SiteLicenseModel.APPLICATION_KEY, getI18nValue(request, prefix, model.getApplication()));
        emailParams.put(SiteLicenseModel.PLC_TYPE_KEY, getI18nValue(request, prefix, model.getPlcType()));
        emailParams.put(SiteLicenseModel.HMI_TYPE_KEY, getI18nValue(request, prefix, model.getHmiType()));
        emailParams.put(SiteLicenseModel.MES_TYPE_KEY, getI18nValue(request, prefix, model.getMesType()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_KEY,
                getI18nValue(request, prefix, model.getNumberOfBasicUnit()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_KEY,
                getI18nValue(request, prefix, model.getNumberOfAdvancedUnit()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SUBJECT, getI18nValue(request, prefix, model.getSubject()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SALUTATION,
                getI18nValue(request, prefix, model.getSalutation()));
        emailParams.putAll(cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_SALUTATION, model.getSalutation()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(request, prefix, model.getBody()));
        emailParams.putAll(cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_BODY, model.getBody()));
    }
    
    /**
     * Extract values from submitted form for Engineering License type
     * @param emailParams
     * @param engineeringLicenseFormBean
     */
    private void extractEngineeringLicenseFormData(Map<String, String> emailParams,
            EngineeringLicenseFormBean engineeringLicenseFormBean) {
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY + VALUE, engineeringLicenseFormBean.getComments());
        emailParams.putAll(cssHideIfEmpty(EngineeringLicenseModel.COMMENTS_JSON_KEY, engineeringLicenseFormBean.getComments()));
        emailParams.put(EngineeringLicenseModel.USERS_EMAIL_LABEL + VALUE, getUsersForEmail(engineeringLicenseFormBean.getUsers()));
    }
    
    /**
     * Extract values from submitted form for Site License type
     * @param emailParams
     * @param siteLicenseFormBean
     */
    private void extractSiteLicenseFormData(Map<String, String> emailParams, SiteLicenseFormBean siteLicenseFormBean) {
        emailParams.put(SiteLicenseModel.NAME_OF_SITE_KEY + VALUE, siteLicenseFormBean.getNameOfSite());
        emailParams.put(SiteLicenseModel.LOCATION_OF_SITE_KEY + VALUE, siteLicenseFormBean.getLocationOfSite());
        emailParams.put(SiteLicenseModel.APPLICATION_KEY + VALUE, siteLicenseFormBean.getApplication());
        emailParams.put(SiteLicenseModel.PLC_TYPE_KEY + VALUE, siteLicenseFormBean.getPlcType());
        emailParams.putAll(cssHideIfEmpty(SiteLicenseModel.PLC_TYPE_KEY, siteLicenseFormBean.getPlcType()));
        emailParams.put(SiteLicenseModel.HMI_TYPE_KEY + VALUE, siteLicenseFormBean.getHmiType());
        emailParams.putAll(cssHideIfEmpty(SiteLicenseModel.HMI_TYPE_KEY, siteLicenseFormBean.getHmiType()));
        emailParams.put(SiteLicenseModel.MES_TYPE_KEY + VALUE, siteLicenseFormBean.getMesType());
        emailParams.putAll(cssHideIfEmpty(SiteLicenseModel.MES_TYPE_KEY, siteLicenseFormBean.getMesType()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_KEY + VALUE,
                siteLicenseFormBean.getNumberOfBasicUnit());
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_KEY + VALUE,
                siteLicenseFormBean.getNumberOfAdvancedUnit());
    }

    private Map<String, String> cssHideIfEmpty(final String key, final String value) {
        Map<String, String> entry;
        if (StringUtils.isBlank(value)) {
            entry = Map.of(key + HIDE_SUFFIX, HIDE_CSS_CLASS);
        } else {
            entry = Map.of(key + HIDE_SUFFIX, StringUtils.EMPTY);
        }
        return entry;
    }
    
    private EngineeringLicenseFormBean createEngineeringLicenseFormBean(ServletRequest request) throws IOException {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        LOGGER.debug("Request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, EngineeringLicenseFormBean.class);
    }
    
    private SiteLicenseFormBean createSiteLicenseFormBean(ServletRequest request) throws IOException {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        LOGGER.debug("Request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, SiteLicenseFormBean.class);
    }
    
    /**
     * Create sling job to send email.
     * @param recipients
     * @param emailParams
     * @param templatePath
     * @return
     */
    private boolean addEmailJob(Map<String, String> emailParams, String templatePath) {
        boolean isSuccess = false;
        String[] recipients = config.recipientAddresses();
        Map<String, Object> properties = new HashMap<>();
        properties.put(MyTetrapakEmailJob.TEMPLATE_PATH, templatePath);
        properties.put(MyTetrapakEmailJob.EMAIL_PARAMS, emailParams);
        properties.put(MyTetrapakEmailJob.RECIPIENTS_ARRAY, recipients);
        LOGGER.debug(" Email properties : Template path = {}, Recipients = {}, Is Email Enabled = {}", templatePath,
                ArrayUtils.toString(config.recipientAddresses()), config.isLicensesEmailEnabled());
        for (Map.Entry<String, String> entry : emailParams.entrySet()) {
            LOGGER.debug("Email Parameters '{}' = {}", entry.getKey(), entry.getValue());
        }
        if (jobMgr != null && config.isLicensesEmailEnabled() && recipients != null) {
            jobMgr.addJob(MyTetrapakEmailJob.JOB_TOPIC_NAME, properties);
            isSuccess = true;
            LOGGER.debug("Email job added");
        } else {
            LOGGER.error("Error in setting up pre-requisites for Email job.");
        }
        return isSuccess;
    }
    
    public String getI18nValue(SlingHttpServletRequest request, String i18nKey, String prefix) {
        return GlobalUtil.getI18nValue(request, prefix, i18nKey);
    }
    
    /**
     * Generate HTML output for displaying user license request in email
     * @param users
     * @return
     */
    private String getUsersForEmail(List<EngineeringLicenseFormBean.Users> users) {
        StringBuilder outputHtml = new StringBuilder();
        String usersTableHtml = USERS_HTML_NAME + USERS_HTML_DATE + USERS_HTML_LICENSES;
        for (EngineeringLicenseFormBean.Users user : users) {
            String substitutedHML = usersTableHtml.replace("NAME", user.getLicenseHolderName());
            substitutedHML = substitutedHML.replace("DATE", user.getActivationDate());
            substitutedHML = substitutedHML.replace("LICENSES", getLicensesForUser(user) + "<br/>");
            outputHtml.append(substitutedHML);
        }
        return outputHtml.toString();
    }

    private String getLicensesForUser(EngineeringLicenseFormBean.Users user) {
        return user.getLicenses().stream().collect(Collectors.joining("<br/>"));
    }
}
