package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
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

    @PostConstruct
    protected void init() {
        Page languagePage = GlobalUtil.getLanguagePage(request, userPreferenceService);
        if (null != languagePage) {
            Resource globalConfigurationResource = request.getResourceResolver().getResource(languagePage.getPath()
                    + "/jcr:content/root/responsivegrid/globalconfiguration");
            if (null != globalConfigurationResource) {
                ValueMap map = globalConfigurationResource.getValueMap();
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
}
