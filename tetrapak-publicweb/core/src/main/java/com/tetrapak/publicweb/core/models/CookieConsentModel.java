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

    @SlingObject
    private SlingHttpServletRequest request;

    private Boolean cookieConsentDisabled;

    private String cookieConsentText;

    private String cookieConsentButtonText;

    private String selectedLanguage;

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

    public Boolean isCookieConsentDisabled() {
        return cookieConsentDisabled;
    }

    public String getCookieConsentText() {
        return cookieConsentText;
    }

    public String getCookieConsentButtonText() {
        return cookieConsentButtonText;
    }

    public String getLocale() {
        if (StringUtils.isEmpty(selectedLanguage)) {
            selectedLanguage = "en";
        }
        return selectedLanguage;
    }
}
