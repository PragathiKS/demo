package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKitsEmailFormBean;
import com.tetrapak.customerhub.core.models.*;
import com.tetrapak.customerhub.core.services.RebuildingKitsEmailService;
import com.tetrapak.customerhub.core.services.config.RebuildingKitsEmailConfiguration;
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
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * The Class RebuildingKitsEmailService Implementation.
 */
@Component(service = RebuildingKitsEmailService.class, immediate = true,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = RebuildingKitsEmailConfiguration.class)
public class RebuildingKitsEmailServiceImpl implements RebuildingKitsEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebuildingKitsEmailServiceImpl.class);
    // This is expected to be empty since full key names would be provided in Dialog.
    // However, this is still a provision to allow for prefixes, if authors don't specify keys with prefix
    private static final String I18N_PREFIX = StringUtils.EMPTY;
    private static final String VALUE = "Value";
    private static final String RK_TRANSLATION_REQUEST_EMAIL_TEMPLATE = "/etc/notification/email/customerhub/rebuildingkits/rktranslationrequestemail.html";

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    /** The config. */
    private RebuildingKitsEmailConfiguration config;

    /**
     * Activate.
     * @param config the config
     */
    @Activate
    public void activate(final RebuildingKitsEmailConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean sendEmail(ResourceBundle resourceBundle, String requestData,
                             RebuildingKitDetailsModel rebuildingKitDetailsModel) {
        LOGGER.debug("Inside sendEmail method of RebuildingKitsEmailServiceImpl");
        boolean isSuccess = false;
        String[] recipients = config.rebuildingKitsRecipientAddresses();
        if(config.isRebuildingKitsEmailServiceEnabled()) {
                isSuccess = sendEmailForCTICreation(resourceBundle, recipients, requestData, rebuildingKitDetailsModel);
        }
        return isSuccess;
    }

    /**
     * Send email for cti creation request
     * @param bundle
     * @param recipients
     * @param requestData
     * @param rebuildingKitDetailsModel
     * @return operation result
     * @throws IOException
     */
    private Boolean sendEmailForCTICreation(ResourceBundle bundle, String[] recipients,
                                                String requestData, RebuildingKitDetailsModel rebuildingKitDetailsModel) {
        boolean isSuccess = false;
        if (Objects.nonNull(recipients)) {
            RebuildingKitsEmailFormBean bean = createRebuildingKitsEmailFormBean(requestData);
            Map<String, String> emailParams = new HashMap<>();
            extractRebuildingKitDetailsModelProps(emailParams, bundle, I18N_PREFIX, rebuildingKitDetailsModel);
            extractRebuildingKitsEmailFormData(emailParams, bean);
            isSuccess = EmailUtil.addEmailJob(emailParams,recipients, RK_TRANSLATION_REQUEST_EMAIL_TEMPLATE, jobMgr);
        } else {
            LOGGER.debug("RebuildingKits cti creation recipients list is missing");
        }
        return isSuccess;

    }

    private RebuildingKitsEmailFormBean createRebuildingKitsEmailFormBean(String requestData) {
        LOGGER.debug("RebuildingKits CTI creation request data json : {}", requestData);
        Gson gson = new Gson();
        return gson.fromJson(requestData, RebuildingKitsEmailFormBean.class);
    }

    /**
     * Extract labels to be used in email for cti creation request for rebuildingKits
     * @param emailParams
     * @param bundle
     * @param prefix
     * @param rkDetailsModel
     */
    private void extractRebuildingKitDetailsModelProps(Map<String, String> emailParams, ResourceBundle bundle,
                                                    String prefix, RebuildingKitDetailsModel rkDetailsModel) {
        emailParams.put(RebuildingKitDetailsModel.EMAIL_SUBJECT, getI18nValue(bundle, prefix, rkDetailsModel.getRkctisubjecttext()));
        emailParams.put(RebuildingKitDetailsModel.EMAIL_SALUTATION, getI18nValue(bundle, prefix, rkDetailsModel.getRkctisalutationtext()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(RebuildingKitDetailsModel.EMAIL_SALUTATION,
                getI18nValue(bundle, prefix, rkDetailsModel.getRkctisalutationtext())));
        emailParams.put(RebuildingKitDetailsModel.EMAIL_BODY, getI18nValue(bundle, prefix, rkDetailsModel.getRkctibodytext()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(RebuildingKitDetailsModel.EMAIL_BODY, getI18nValue(bundle, prefix, rkDetailsModel.getRkctibodytext())));
        emailParams.put(RebuildingKitDetailsModel.USERNAME, getI18nValue(bundle, prefix, rkDetailsModel.getRkctiUsernameText()));
        emailParams.put(RebuildingKitDetailsModel.RK_TB_NUMBER,getI18nValue(bundle, prefix, rkDetailsModel.getRktbnumbertext()));
        emailParams.put(RebuildingKitDetailsModel.MCON,getI18nValue(bundle, prefix, rkDetailsModel.getRkctimcontext()));
        emailParams.put(RebuildingKitDetailsModel.FUNCTIONAL_LOCATION,getI18nValue(bundle, prefix, rkDetailsModel.getRkctifunctionalLocationtext()));
        emailParams.put(RebuildingKitDetailsModel.REQUESTED_CTI_LANGUAGE,getI18nValue(bundle, prefix, rkDetailsModel.getRkctirequestedlanguage()));
        emailParams.put(RebuildingKitDetailsModel.COMMENTS_JSON_KEY,getI18nValue(bundle, prefix, rkDetailsModel.getRkcticommenttext()));
        emailParams.putAll(EmailUtil.cssHideIfEmpty(RebuildingKitDetailsModel.COMMENTS_JSON_KEY,
                getI18nValue(bundle, prefix, rkDetailsModel.getRkcticommenttext())));
        emailParams.put(RebuildingKitDetailsModel.USERNAME + VALUE, getI18nValue(bundle, prefix, rkDetailsModel.getUserNameValue()));
        emailParams.put(RebuildingKitDetailsModel.EMAIL_ADDRESS + VALUE,
                getI18nValue(bundle, prefix, rkDetailsModel.getEmailAddressValue()));
        emailParams.put(RebuildingKitDetailsModel.EMAIL_ADDRESS,
                getI18nValue(bundle, prefix, rkDetailsModel.getRkctiemailaddress()));
    }

    /**
     * Extract values from submitted form for cti creation for rebuildingkits
     * @param emailParams email Parameter
     * @param rebuildingKitsEmailFormBean withdrawal licence form bean
     */
    private void extractRebuildingKitsEmailFormData(Map<String, String> emailParams,
                                                    RebuildingKitsEmailFormBean rebuildingKitsEmailFormBean) {
        emailParams.put(RebuildingKitDetailsModel.RK_TB_NUMBER + VALUE,rebuildingKitsEmailFormBean.getRkTbNumber());
        emailParams.put(RebuildingKitDetailsModel.MCON + VALUE, rebuildingKitsEmailFormBean.getMcon());
        emailParams.put(RebuildingKitDetailsModel.FUNCTIONAL_LOCATION + VALUE, rebuildingKitsEmailFormBean.getFunctionalLocation());
        emailParams.put(RebuildingKitDetailsModel.REQUESTED_CTI_LANGUAGE + VALUE, rebuildingKitsEmailFormBean.getRequestedCTILanguage());
        emailParams.putAll(EmailUtil.cssHideIfEmpty(RebuildingKitDetailsModel.COMMENTS_JSON_KEY + VALUE, rebuildingKitsEmailFormBean.getUserComment()));
    }

    public String getI18nValue(ResourceBundle bundle, String i18nKey, String prefix) {
        return GlobalUtil.getI18nValue(bundle, prefix, i18nKey);
    }

}
