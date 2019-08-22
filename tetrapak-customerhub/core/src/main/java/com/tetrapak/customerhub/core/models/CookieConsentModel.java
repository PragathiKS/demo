package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

/**
 * Model class for Consent Cookie component.
 *
 * @author tustusha
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CookieConsentModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private Boolean cookieConsentDisabled;

    private String cookieConsentText;

    private String cookieConsentButtonText;

    private String selectedLanguage;

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        Page languagePage = GlobalUtil.getLanguagePage(request, userPreferenceService);
        if (null != languagePage) {
            Resource cookieConfigurationResource = request.getResourceResolver().getResource(languagePage.getPath()
                    + "/jcr:content/root/responsivegrid/cookieconfiguration");
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
        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }
}
