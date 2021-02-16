package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.email.EmailServiceConstants;
import com.tetrapak.publicweb.core.beans.NewsEventBean;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DataEncryptionService;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * The Class SubscriptionMailServiceImpl.
 *
 * @author shusaxen
 */
@Component(
        immediate = true,
        service = SubscriptionMailService.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SubscriptionMailServiceImpl implements SubscriptionMailService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionMailServiceImpl.class);

    /** The job mgr. */
    @Reference
    private JobManager jobMgr;

    /** The media service. */
    @Reference
    private DynamicMediaService mediaService;

    /** The encryption service. */
    @Reference
    private DataEncryptionService encryptionService;

    /** The Constant DEFAULT LOGO BACKGROUND RGB value . */
    private static final String DEFAULT_LOGO_BGC = "2,63,136";

    /**
     * Send subscription email.
     *
     * @param newsEventbean
     *            the news eventbean
     * @param mailAddresses
     *            the mail addresses
     * @param resolver
     *            the resolver
     * @return the string
     */
    @Override
    public String sendSubscriptionEmail(NewsEventBean newsEventbean, List<String> mailAddresses,
            ResourceResolver resolver) {
        LOGGER.debug("Inside sendSubcriptionEmail");
        String status = PWConstants.STATUS_ERROR;
        try {
            if (Objects.nonNull(mailAddresses)) {
                status = PWConstants.STATUS_SUCCESS;
                for (String mailAddress : mailAddresses) {
                    String[] receipientArray = { mailAddress };
                    Map<String, String> emailParams = setEmailParams(newsEventbean, resolver, mailAddress);
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("templatePath", getTemplatePath(newsEventbean.getLanguage()));
                    properties.put("emailParams", emailParams);
                    properties.put("receipientsArray", receipientArray);
                    if (jobMgr != null) {
                        jobMgr.addJob(PWConstants.SEND_EMAIL_JOB_TOPIC, properties);
                    } else {
                        status = PWConstants.STATUS_ERROR;
                        LOGGER.error("JobManager Reference null");
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in SubscriptionMailService {}", e.getMessage());
            status = PWConstants.STATUS_ERROR;
        }
        return status;

    }

    /**
     * Sets the email params.
     *
     * @param newsEventbean
     *            the news eventbean
     * @param resolver
     *            the resolver
     * @param mailAddress
     *            the mail address
     * @return the map
     */
    private Map<String, String> setEmailParams(NewsEventBean newsEventbean, ResourceResolver resolver,
            String mailAddress) {
        Map<String, String> emailParams = new HashMap<>();
        emailParams.put("title", newsEventbean.getTitle());
        emailParams.put("description", newsEventbean.getDescription());
        if (StringUtils.isEmpty(newsEventbean.getHeroImage())) {
            emailParams.put("bannerClass", "banner-hide");
            emailParams.put("bannerImage", "#");
        } else {
            emailParams.put("bannerClass", "banner-show");
            emailParams.put("bannerImage",
                    GlobalUtil.getImageUrlFromScene7(resolver, newsEventbean.getHeroImage(), mediaService));
        }
        emailParams.put("headerLogo",
                GlobalUtil.getImageUrlFromScene7(resolver, newsEventbean.getHeaderLogo(), mediaService));
        emailParams.put("footerLogo",
                GlobalUtil.getImageUrlFromScene7(resolver, newsEventbean.getFooterLogo(), mediaService));
        if (!newsEventbean.getFooterLogoBackground().isEmpty()) {
            emailParams.put("footerLogoBGC", newsEventbean.getFooterLogoBackground());
        } else {
            emailParams.put("footerLogoBGC", DEFAULT_LOGO_BGC);
        }
        emailParams.put("pageLink", newsEventbean.getPageLink());
        emailParams.put("newsRoomLink", newsEventbean.getNewsroomLink());
        emailParams.put("legalInformationLink", newsEventbean.getLegalInformationLink());
        String encryptedMailAddress = encryptionService.encryptText(mailAddress);
        if (!encryptedMailAddress.equalsIgnoreCase(PWConstants.STATUS_ERROR)
                && newsEventbean.getManagePreferenceLink().startsWith(PWConstants.HTTPS_PROTOCOL)) {
            emailParams.put("managePreferenceLink", newsEventbean.getManagePreferenceLink() + "?id="
                    + encryptionService.encryptText(mailAddress));
        } else {
            emailParams.put("managePreferenceLink", "#");
        }
        emailParams.put(EmailServiceConstants.SUBJECT, newsEventbean.getTitle());
        return emailParams;
    }

    /**
     * Gets the template path.
     *
     * @param language
     *            the language
     * @return the template path
     */
    private String getTemplatePath(String language) {
        return PWConstants.SUBSCRIPTION_MAIL_TEMPLATE_ROOT_PATH + PWConstants.SLASH + language + PWConstants.SLASH
                + "subscriptionemail.html";
    }

}
