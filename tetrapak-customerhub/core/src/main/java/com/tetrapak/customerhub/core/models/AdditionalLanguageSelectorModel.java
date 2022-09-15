package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.PreferredLanguagesService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

/**
 * Model class for language selector component
 *
 * @author Aalekh Mathur
 */
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdditionalLanguageSelectorModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;
    
    @OSGiService
    private PreferredLanguagesService preferredLanguagesService;

    private String selectedLanguage;
    
    private Map<String, String> listOfLanguages = new HashMap<>();

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getAdditionalSelectedLanguage(request, userPreferenceService);
        listOfLanguages = preferredLanguagesService.getPreferredLanguages(request.getResourceResolver());
    }
    
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public Map<String, String> getListOfLanguages() {
        return listOfLanguages;
    }
}
