package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
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
import javax.jcr.Session;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Model class for language selector component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LanguageSelectorModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String headingI18n;

    private String closeBtnTextI18n;

    private String selectedLanguage;

    private List<String> listOfLanguages = new ArrayList<>();

    @PostConstruct
    protected void init() {
        selectedLanguage = getSelectedLanguageFromCookie();

        Resource globalConfigResource = getGlobalConfigurationResource();
        if (null != globalConfigResource) {
            ValueMap map = globalConfigResource.getValueMap();
            headingI18n = (String) map.get("headingI18n");
            closeBtnTextI18n = (String) map.get("closeBtnText");

            Resource languageResource = globalConfigResource.getChild("languages");
            if (null == languageResource) {
                return;
            }

            Iterator<Resource> itr = languageResource.listChildren();
            while (itr.hasNext()) {
                ValueMap languageNodeValueMap = itr.next().getValueMap();
                listOfLanguages.add((String) languageNodeValueMap.get("langCode"));
            }

            Collections.sort(listOfLanguages);
        }
    }

    private String getSelectedLanguageFromCookie() {
        Cookie languageCookie = request.getCookie("lang-code");
        if (null != languageCookie) {
            return languageCookie.getValue();
        }
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null != session && null != userPreferenceService) {
            String userId = session.getUserID();
            userPreferenceService.getSavedPreferences(userId, CustomerHubConstants.LANGUGAGE_PREFERENCES);
        }
        return null;
    }

    private Resource getGlobalConfigurationResource() {
        Resource childResource = request.getResourceResolver().getResource(
                GlobalUtil.getCustomerhubConfigPagePath(request.getResource()) + "/jcr:content/root/responsivegrid");
        if (null != childResource) {
            return GlobalUtil.getGlobalConfigurationResource(childResource);
        }
        return null;
    }

    public String getHeadingI18n() {
        return headingI18n;
    }

    public String getCloseBtnTextI18n() {
        return closeBtnTextI18n;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public List<String> getListOfLanguages() {
        return listOfLanguages;
    }
}
