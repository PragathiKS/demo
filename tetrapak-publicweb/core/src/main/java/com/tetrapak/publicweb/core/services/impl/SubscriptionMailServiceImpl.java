package com.tetrapak.publicweb.core.services.impl;

import java.util.*;

import com.adobe.agl.util.ULocale;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.PageUtil;
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
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SubscriptionMailService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

import com.adobe.acs.commons.i18n.I18nProvider;

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

    @Reference
    private I18nProvider i18nProvider;


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

                PageManager pageManager = resolver.adaptTo(PageManager.class);
                String rootPath = newsEventbean.getRootPath();
                Page page = pageManager.getPage(LinkUtils.getRootPath(rootPath));
                final Locale locale = PageUtil.getPageLocale(page);
                for (String mailAddress : mailAddresses) {
                    String[] receipientArray = { mailAddress };
                    Map<String, String> emailParams = setEmailParams(newsEventbean, resolver,locale);
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("templatePath", getTemplatePath(newsEventbean.getImagePath()));
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
    private Map<String, String> setEmailParams(NewsEventBean newsEventbean, ResourceResolver resolver, Locale locale) {
        Map<String, String> emailParams = new HashMap<>();
        emailParams.put("title", newsEventbean.getTitle());
        emailParams.put("description", newsEventbean.getDescription());
        if (StringUtils.isNotEmpty(newsEventbean.getImagePath())) {
        emailParams.put("imagePath",
                    GlobalUtil.getImageUrlFromScene7(resolver, newsEventbean.getImagePath(), mediaService));
        }
        else{
            emailParams.put("imagePath","#");
        }
        if(StringUtils.contains(newsEventbean.getTemplateType(), "press-release")){
            emailParams.put("templateType",i18nProvider.translate(PWConstants.SUBSCRIPTION_PRESS_TEMPLATE, locale));
        }
        else {
            emailParams.put("templateType",i18nProvider.translate(PWConstants.SUBSCRIPTION_NEWS_ARTICLE_TEMAPLTE, locale));
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
        emailParams.put("rootPageLink",newsEventbean.getRootPageLink());
        emailParams.put("legalInformationLink", newsEventbean.getLegalInformationLink());
        emailParams.put("contactUsLink", newsEventbean.getContactUsLink());
        emailParams.put("newsRoomLink", newsEventbean.getNewsroomLink());
        emailParams.put("ctaText", i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_CTA_TEXT, locale));
        emailParams.put("kindRegardsText",i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_KIND_REGARDS_TEXT, locale));
        emailParams.put("privacyPolicyText",i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_PRIVACY_POLICY_TEXT,locale));
        emailParams.put("genericLinkText", i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_GENERIC_LINK_TEXT,locale));
        emailParams.put("unsubscribeText", i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_UNSUBSCRIBE_TEXT,locale));
        emailParams.put("contactText", i18nProvider.translate(PWConstants.SUBSCRIPTION_EMAIL_CONTACT_TEXT,locale));
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
    private String getTemplatePath(String imagePath) {
        String emailTemplate = null;
        if (StringUtils.isNotEmpty(imagePath)){
            emailTemplate = "subscriptionemail.html";
        }
        else {
            emailTemplate = "subscriptionemailnoimage.html";
        }
            return PWConstants.SUBSCRIPTION_MAIL_TEMPLATE_ROOT_PATH + PWConstants.SLASH
                    + emailTemplate;
        }
}
