package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

/**
 * Model class for Consent Cookie component.
 *
 * @author Sandip kumar
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CookieConsentModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The cookie consent disabled. */
    private Boolean cookieConsentDisabled;

    /** The cookie consent text. */
    private String cookieConsentText;

    /** The cookie consent button text. */
    private String cookieConsentButtonText;

    /** The selected language. */
    private String selectedLanguage;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        Page languagePage = PageUtil.getLanguagePage(request.getResource());
        if (null != languagePage) {
            selectedLanguage = PageUtil.getLanguageCode(languagePage);
            Resource cookieConfigurationResource = request.getResourceResolver()
                    .getResource(languagePage.getPath() + "/jcr:content/root/responsivegrid/cookieconfiguration");
            if (null != cookieConfigurationResource) {
                ValueMap map = cookieConfigurationResource.getValueMap();
                cookieConsentDisabled = Boolean.valueOf((String) map.get("cookieConsentDisabled"));
                cookieConsentText = (String) map.get("cookieConsentText");
                cookieConsentButtonText = (String) map.get("cookieConsentButtonText");
            }
        }
    }

    /**
     * Checks if is cookie consent disabled.
     *
     * @return the boolean
     */
    public Boolean isCookieConsentDisabled() {
        return cookieConsentDisabled;
    }

    /**
     * Gets the cookie consent text.
     *
     * @return the cookie consent text
     */
    public String getCookieConsentText() {
        return cookieConsentText;
    }

    /**
     * Gets the cookie consent button text.
     *
     * @return the cookie consent button text
     */
    public String getCookieConsentButtonText() {
        return cookieConsentButtonText;
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    public String getLocale() {
        if (StringUtils.isEmpty(selectedLanguage)) {
            selectedLanguage = "en";
        }
        return selectedLanguage;
    }
}
