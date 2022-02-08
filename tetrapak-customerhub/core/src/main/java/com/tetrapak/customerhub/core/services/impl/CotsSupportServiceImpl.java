package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.jobs.CotsSupportEmailJob;
import com.tetrapak.customerhub.core.models.CotsSupportModel;
import com.tetrapak.customerhub.core.services.CotsSupportService;
import com.tetrapak.customerhub.core.services.config.CotsSupportServiceConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import com.adobe.acs.commons.email.EmailService;
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
@Designate(ocd = CotsSupportServiceConfig.class)
public class CotsSupportServiceImpl implements CotsSupportService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CotsSupportServiceImpl.class);
    private static final String VALUE = "Value";
    private static final String SENDER_EMAIL_ADDRESS = "senderEmailAddress";
    private static final String SENDER_NAME = "senderName";
    private static final String TEMPLATE_PATH = "templatePath";
    private static final String EMAIL_PARAMS = "emailParams";
    private static final String RECIPIENTS_ARRAY = "receipientsArray";
    private static final String ATTACHMENTS = "attachments";
    
    /** The job mgr. */
    @Reference
    private JobManager jobMgr;
    
    @Reference
    private EmailService emailService;
    
    /** The config. */
    private CotsSupportServiceConfig config;
    
    /**
     * Activate.
     * @param config the config
     */
    @Activate
    public void activate(final CotsSupportServiceConfig config) {
        this.config = config;
    }
    
    @Override
    public boolean sendEmail(List<Map<String, String>> attachments, CotsSupportModel model,
            CotsSupportFormBean cotsSupportFormBean) {
        LOGGER.debug("Inside sendEmail method of CotsSupportServiceImpl");
        boolean isSuccess = false;
        String[] recipientEmailFromOsgiConfig = config.recipientAddresses();
        if (Objects.nonNull(recipientEmailFromOsgiConfig)) {
            Map<String, String> emailParams = new HashMap<>();
            // emailParams.put(SENDER_EMAIL_ADDRESS,config.senderEmailAddress());
            // emailParams.put(SENDER_NAME,config.senderName());
            extractCotsSupportModelProps(emailParams, model);
            Map<String, Object> properties = new HashMap<>();
            properties.put(TEMPLATE_PATH, config.emailTemplatePath());
            properties.put(EMAIL_PARAMS, emailParams);
            String recipientAddressFromDialog = emailParams
                    .get(CotsSupportModel.COTSSupportComponentDialog.RECIPIENT_EMAIL_ADDRESS.i18nJsonKey);
            if (recipientAddressFromDialog == null || recipientAddressFromDialog.equals(StringUtils.EMPTY)) {
                properties.put(RECIPIENTS_ARRAY, recipientEmailFromOsgiConfig);
            } else {
                properties.put(RECIPIENTS_ARRAY, new String[] {
                        recipientAddressFromDialog
                });
            }
            properties.put(ATTACHMENTS, attachments);
            if (jobMgr != null) {
                jobMgr.addJob(CotsSupportEmailJob.JOB_TOPIC_NAME, properties);
                isSuccess = true;
            } else {
                LOGGER.error("JobManager Reference null");
            }
        }
        return isSuccess;
    }
    
    public void extractCotsSupportModelProps(Map<String, String> emailParams, CotsSupportModel model) {
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SALUTATION.i18nJsonKey, model.getSalutation());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.BODY.i18nJsonKey, model.getBody());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CONTACT_DETAILS.i18nJsonKey,
                model.getContactDetails());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SELECT_REQUEST.i18nJsonKey,
                model.getSelectRequest());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.COMPANY.i18nJsonKey, model.getCompany());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CUSTOMER_SITE.i18nJsonKey, model.getCustomerSite());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.AFFECTED_SYSTEMS_LABEL.i18nJsonKey,
                model.getAffectedSystemsLabel());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.PRODUCT_INVOLVED.i18nJsonKey,
                model.getProductInvolvedLabel());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SOFTWARE_VERSION.i18nJsonKey,
                model.getSoftwareVersion());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.ENGINEERING_LICENSE_SERIAL_NUMBER.i18nJsonKey,
                model.getEngineeringLicenseSerialNumber());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.SHORT_DESCRIPTION.i18nJsonKey,
                model.getShortDescription());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.CONTACT_DETAILS.i18nJsonKey,
                model.getContactDetails());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.QUESTION.i18nJsonKey, model.getQuestion());
        emailParams.put(CotsSupportModel.COTSSupportComponentDialog.RECIPIENT_EMAIL_ADDRESS.i18nJsonKey,
                model.getRecipientEmailAddress());
        
    }
    
    public void extractFormData(Map<String, String> emailParams, CotsSupportFormBean cotsSupportFormBean) {
        emailParams.put(CotsSupportFormBean.QUERY_TYPE + VALUE, cotsSupportFormBean.getLogQueryType());
        emailParams.put(CotsSupportFormBean.COMPANY + VALUE, cotsSupportFormBean.getCompany());
        emailParams.put(CotsSupportFormBean.CUSTOMER_SITE + VALUE, cotsSupportFormBean.getCustomerSite());
        emailParams.put(CotsSupportFormBean.AFFECTED_SYSTEMS + VALUE, cotsSupportFormBean.getAffectedSystems());
        emailParams.put(CotsSupportFormBean.PRODUCT_INVOLVED + VALUE, cotsSupportFormBean.getProductInvolved());
        emailParams.put(CotsSupportFormBean.SOFTWARE_VERSION + VALUE, cotsSupportFormBean.getSoftwareVersion());
        emailParams.put(CotsSupportFormBean.LICENSE_NUMBER + VALUE, cotsSupportFormBean.getLicenseNumber());
        emailParams.put(CotsSupportFormBean.DESCRIPTION + VALUE, cotsSupportFormBean.getDescription());
        emailParams.put(CotsSupportFormBean.QUESTIONS + VALUE, cotsSupportFormBean.getQuestions());
        emailParams.put(CotsSupportFormBean.NAME + VALUE, cotsSupportFormBean.getName());
        emailParams.put(CotsSupportFormBean.EMAIL_ADDRESS + VALUE, cotsSupportFormBean.getEmailAddress());
        emailParams.put(CotsSupportFormBean.TELEPHONE + VALUE, cotsSupportFormBean.getTelephone());
    }
}
