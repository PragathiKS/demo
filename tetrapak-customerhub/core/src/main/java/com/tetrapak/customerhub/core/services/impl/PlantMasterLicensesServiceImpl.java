package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.licenses.EngineeringLicenseFormBean;
import com.tetrapak.customerhub.core.beans.licenses.SiteLicenseFormBean;
import com.tetrapak.customerhub.core.beans.licenses.WithdrawalLicenseFormBean;
import com.tetrapak.customerhub.core.models.ActiveLicenseModel;
import com.tetrapak.customerhub.core.models.EngineeringLicenseModel;
import com.tetrapak.customerhub.core.models.PlantMasterLicensesModel;
import com.tetrapak.customerhub.core.models.SiteLicenseModel;
import com.tetrapak.customerhub.core.services.PlantMasterLicensesService;
import com.tetrapak.customerhub.core.services.config.PlantMasterLicensesEmailConfiguration;
import com.tetrapak.customerhub.core.utils.EmailUtil;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
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
    private static final String LICENSE_TYPE_ACTIVE_WITHDRAWAL = "activeWithdrawal";
    private static final String USERS_HTML_NAME = "<tr><td class='license-list'> NAME </td>";
    private static final String USERS_HTML_DATE = "<td class='license-list'> DATE </td> ";
    private static final String USERS_HTML_LICENSES = "<td class='license-list'> \tLICENSES </td></tr><tr><td colspan='3'>&nbsp;</td></tr>";
    private static final String ENG_LICENSE_EMAIL_TEMPLATE = "/etc/notification/email/customerhub/licenses/engineering-license.html";
    private static final String SITELICENSE_EMAIL_TEMPLATE = "/etc/notification/email/customerhub/licenses/site-license.html";
    private static final String WITHDRAWALLICENSE_EMAIL_TEMPLATE
            = "/etc/notification/email/customerhub/licenses/active-license-withdraw.html";

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
    public boolean sendEmail(ResourceBundle bundle, String licenseTypeHeader,
            String requestData, PlantMasterLicensesModel masterLicensesModel) throws IOException {
        LOGGER.debug("Inside sendEmail method of PlantMasterLicensesServiceImpl");
        boolean isSuccess = false;
        String[] recipients = config.recipientAddresses();
        if(config.isLicensesEmailEnabled()) {
            if (LICENSE_TYPE_ACTIVE_WITHDRAWAL.equals(licenseTypeHeader)) {
                isSuccess = sendEmailWithdrawlLicense(bundle, recipients, requestData, masterLicensesModel);
            } else if (LICENSE_TYPE_ENGINEERING.equals(licenseTypeHeader)) {
                isSuccess = sendEmailEngineeringLicense(bundle, recipients, requestData, masterLicensesModel);
            } else {
                isSuccess = sendEmailSiteLicense(bundle, recipients, requestData, masterLicensesModel);
            }
        }
        return isSuccess;
    }

    /**
     * Send email for active license withdrawal request
     * @param bundle
     * @param recipients
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailWithdrawlLicense(ResourceBundle bundle, String[] recipients,
                                                String requestData, PlantMasterLicensesModel plantMasterLicensesModel) {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            WithdrawalLicenseFormBean bean = createWithdrawalLicenseFormBean(requestData);
            LOGGER.debug("Data Ojbject for withdrawal license : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractWithdrawalLicenseModelProps(emailParams, bundle, I18N_PREFIX, plantMasterLicensesModel);
            extractWithdrawalLicenseFormData(emailParams, bean);
            isSuccess = EmailUtil.addEmailJob(emailParams,recipients, WITHDRAWALLICENSE_EMAIL_TEMPLATE, jobMgr);
        } else {
            LOGGER.debug("Withdrawal email recipients list is missing");
        }
        return isSuccess;

    }

    /**
     * Send email for Engineering license type
     * @param bundle
     * @param recipients
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailEngineeringLicense(ResourceBundle bundle, String[] recipients,
            String requestData, PlantMasterLicensesModel plantMasterLicensesModel) {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            EngineeringLicenseFormBean bean = createEngineeringLicenseFormBean(requestData);
            LOGGER.debug("Submitted form data object : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractEngineeringLicenseModelProps(emailParams, bundle, I18N_PREFIX, plantMasterLicensesModel);
            extractEngineeringLicenseFormData(emailParams, bean);
            isSuccess = EmailUtil.addEmailJob(emailParams, recipients, ENG_LICENSE_EMAIL_TEMPLATE, jobMgr);
        } else {
            LOGGER.debug("Recipient email address missing in configuration!");
        }
        return isSuccess;

    }

    /**
     * Send email for Site license type
     *
     * @param bundle
     * @param recipients
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailSiteLicense(ResourceBundle bundle, String[] recipients, String requestData,
            PlantMasterLicensesModel plantMasterLicensesModel) {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            SiteLicenseFormBean bean = createSiteLicenseFormBean(requestData);
            LOGGER.debug("Submitted form data object : {}", bean.toString());
            Map<String, String> emailParams = new HashMap<>();
            extractSiteLicenseModelProps(emailParams, bundle, I18N_PREFIX, plantMasterLicensesModel);
            extractSiteLicenseFormData(emailParams, bean);
            isSuccess = EmailUtil.addEmailJob(emailParams, recipients, SITELICENSE_EMAIL_TEMPLATE, jobMgr);
        } else {
            LOGGER.debug("Recipient email address missing in configuration!");
        }
        return isSuccess;
    }

    /**
     * Extract labels to be used in email for withdrawal Request
     * @param emailParams
     * @param bundle
     * @param prefix
     */
    private void extractWithdrawalLicenseModelProps(Map<String, String> emailParams, ResourceBundle bundle,
                                                     String prefix, PlantMasterLicensesModel plantMasterLicensesModel) {
        ActiveLicenseModel model = plantMasterLicensesModel.getActiveLicenseModel();
        emailParams.put(PlantMasterLicensesModel.EMAIL_SUBJECT, getI18nValue(bundle, prefix, model.getSubject()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SALUTATION, getI18nValue(bundle, prefix, model.getSalutation()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_SALUTATION,
                getI18nValue(bundle, prefix, model.getSalutation())));
        emailParams.put(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(bundle, prefix, model.getBody()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(bundle, prefix, model.getBody())));
        emailParams.put(ActiveLicenseModel.USERNAME, getI18nValue(bundle, prefix, model.getUsernameText()));
        emailParams.put(ActiveLicenseModel.LICENSE_KEY,getI18nValue(bundle, prefix, model.getLicenceKey()));
        emailParams.put(ActiveLicenseModel.PLATFORM,getI18nValue(bundle, prefix, model.getPlatformText()));
        emailParams.put(ActiveLicenseModel.START_DATE,getI18nValue(bundle, prefix, model.getStartDate()));
        emailParams.put(ActiveLicenseModel.END_DATE,getI18nValue(bundle, prefix, model.getEndDate()));
        emailParams.put(ActiveLicenseModel.COMMENTS_JSON_KEY,getI18nValue(bundle, prefix, model.getEmailCommentText()));
        emailParams.put(ActiveLicenseModel.REQUESTER, getI18nValue(bundle, prefix, model.getRequestorText()));
        emailParams.put(ActiveLicenseModel.REQUESTER + VALUE, getI18nValue(bundle, prefix, plantMasterLicensesModel.getUserNameValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailAddressValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailaddress()));
    }


    /**
     * Extract labels to be used in email for engineering license type
     * @param emailParams
     * @param bundle
     * @param prefix
     */
    private void extractEngineeringLicenseModelProps(Map<String, String> emailParams, ResourceBundle bundle,
            String prefix, PlantMasterLicensesModel plantMasterLicensesModel) {
        EngineeringLicenseModel model = plantMasterLicensesModel.getEngineeringLicenseModel();
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY, getI18nValue(bundle, prefix, model.getComments()));
        emailParams.put(EngineeringLicenseModel.USERS_EMAIL_LABEL, getI18nValue(bundle, prefix, model.getUsers()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_USER_NAME,
                getI18nValue(bundle, prefix, model.getLicenseTableUserName()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_ACTIVATION_DATE,
                getI18nValue(bundle, prefix, model.getLicenseTableActivationDate()));
        emailParams.put(EngineeringLicenseModel.LICENSE_TABLE_LIST_OF_LICENSES,
                getI18nValue(bundle, prefix, model.getLicenseTableListOfLicenses()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getUsername()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME + VALUE,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getUserNameValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailAddressValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailaddress()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SUBJECT, getI18nValue(bundle, prefix, model.getSubject()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SALUTATION,
                getI18nValue(bundle, prefix, model.getSalutation()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(bundle, prefix, model.getBody()));
    }

    /**
     * Extract labels to be used in email for site license type
     * @param emailParams
     * @param bundle
     * @param prefix
     */
    private void extractSiteLicenseModelProps(Map<String, String> emailParams, ResourceBundle bundle,
            String prefix, PlantMasterLicensesModel plantMasterLicensesModel) {
        SiteLicenseModel model = plantMasterLicensesModel.getSiteLicenseModel();
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getUsername()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_USERNAME + VALUE,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getUserNameValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailAddressValue()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_ADDRESS,
                getI18nValue(bundle, prefix, plantMasterLicensesModel.getEmailaddress()));
        emailParams.put(SiteLicenseModel.NAME_OF_SITE_KEY, getI18nValue(bundle, prefix, model.getNameOfSite()));
        emailParams.put(SiteLicenseModel.LOCATION_OF_SITE_KEY,
                getI18nValue(bundle, prefix, model.getLocationOfSite()));
        emailParams.put(SiteLicenseModel.APPLICATION_KEY, getI18nValue(bundle, prefix, model.getApplication()));
        emailParams.put(SiteLicenseModel.PLC_TYPE_KEY, getI18nValue(bundle, prefix, model.getPlcType()));
        emailParams.put(SiteLicenseModel.HMI_TYPE_KEY, getI18nValue(bundle, prefix, model.getHmiType()));
        emailParams.put(SiteLicenseModel.MES_TYPE_KEY, getI18nValue(bundle, prefix, model.getMesType()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_KEY,
                getI18nValue(bundle, prefix, model.getNumberOfBasicUnit()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_KEY,
                getI18nValue(bundle, prefix, model.getNumberOfAdvancedUnit()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SUBJECT, getI18nValue(bundle, prefix, model.getSubject()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_SALUTATION,
                getI18nValue(bundle, prefix, model.getSalutation()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_SALUTATION, model.getSalutation()));
        emailParams.put(PlantMasterLicensesModel.EMAIL_BODY, getI18nValue(bundle, prefix, model.getBody()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(PlantMasterLicensesModel.EMAIL_BODY, model.getBody()));
    }

    /**
     * Extract values from submitted form for withdrawal License type
     * @param emailParams email Parameter
     * @param withdrawalLicenseFormBean withdrawal licence form bean
     */
    private void extractWithdrawalLicenseFormData(Map<String, String> emailParams,
                                                   WithdrawalLicenseFormBean withdrawalLicenseFormBean) {
        emailParams.put(ActiveLicenseModel.USERNAME + VALUE,withdrawalLicenseFormBean.getName());
        emailParams.put(ActiveLicenseModel.LICENSE_KEY + VALUE, withdrawalLicenseFormBean.getLicenseKey());
        emailParams.put(ActiveLicenseModel.PLATFORM + VALUE, withdrawalLicenseFormBean.getPlatform());
        emailParams.put(ActiveLicenseModel.START_DATE + VALUE, withdrawalLicenseFormBean.getStartDate());
        emailParams.put(ActiveLicenseModel.END_DATE + VALUE, withdrawalLicenseFormBean.getEndDate());
        emailParams.put(ActiveLicenseModel.COMMENTS_JSON_KEY + VALUE, withdrawalLicenseFormBean.getComments());
        emailParams.putAll(EmailUtil.cssHideIfEmpty(ActiveLicenseModel.COMMENTS_JSON_KEY + VALUE, withdrawalLicenseFormBean.getComments()));
    }

    /**
     * Extract values from submitted form for Engineering License type
     * @param emailParams
     * @param engineeringLicenseFormBean
     */
    private void extractEngineeringLicenseFormData(Map<String, String> emailParams,
            EngineeringLicenseFormBean engineeringLicenseFormBean) {
        emailParams.put(EngineeringLicenseModel.COMMENTS_JSON_KEY + VALUE, engineeringLicenseFormBean.getComments());
        emailParams.putAll(EmailUtil.cssHideIfEmpty(EngineeringLicenseModel.COMMENTS_JSON_KEY, engineeringLicenseFormBean.getComments()));
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
        emailParams.putAll(EmailUtil.cssHideIfEmpty(SiteLicenseModel.PLC_TYPE_KEY, siteLicenseFormBean.getPlcType()));
        emailParams.put(SiteLicenseModel.HMI_TYPE_KEY + VALUE, siteLicenseFormBean.getHmiType());
        emailParams.putAll(EmailUtil.cssHideIfEmpty(SiteLicenseModel.HMI_TYPE_KEY, siteLicenseFormBean.getHmiType()));
        emailParams.put(SiteLicenseModel.MES_TYPE_KEY + VALUE, siteLicenseFormBean.getMesType());
        emailParams.putAll(EmailUtil.cssHideIfEmpty(SiteLicenseModel.MES_TYPE_KEY, siteLicenseFormBean.getMesType()));
        emailParams.put(SiteLicenseModel.NUMBER_OF_BASIC_UNIT_KEY + VALUE,
                siteLicenseFormBean.getNumberOfBasicUnit());
        emailParams.put(SiteLicenseModel.NUMBER_OF_ADVANCED_UNIT_KEY + VALUE,
                siteLicenseFormBean.getNumberOfAdvancedUnit());
    }


    private WithdrawalLicenseFormBean createWithdrawalLicenseFormBean(String requestData) {
        LOGGER.debug("Withdrawal License Request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, WithdrawalLicenseFormBean.class);
    }

    private EngineeringLicenseFormBean createEngineeringLicenseFormBean(String requestData) {
        LOGGER.debug("Request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, EngineeringLicenseFormBean.class);
    }

    private SiteLicenseFormBean createSiteLicenseFormBean(String requestData) {
        LOGGER.debug("Request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, SiteLicenseFormBean.class);
    }

    public String getI18nValue(ResourceBundle bundle, String i18nKey, String prefix) {
        return GlobalUtil.getI18nValue(bundle, prefix, i18nKey);
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
