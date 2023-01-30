package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.PreferredLanguagesService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
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
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdditionalLanguageSelectorModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;
    
    @OSGiService
    private PreferredLanguagesService preferredLanguagesService;

    private String selectedLanguage;
    
    private Map<String, String> listOfLanguages = new HashMap<>();
    
    private String saveAdditionalPrefLangServletUrl;
    
    /** The resource. */
    @SlingObject
    private Resource resource;
	
    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getAdditionalSelectedLanguage(request, userPreferenceService);
        listOfLanguages = preferredLanguagesService.getPreferredLanguages(request.getResourceResolver());
        saveAdditionalPrefLangServletUrl = resource.getPath() + CustomerHubConstants.DOT + CustomerHubConstants.LANGUGAGE_PREFERENCES
        		+ CustomerHubConstants.DOT + CustomerHubConstants.JSON_SERVLET_EXTENSION;
    }
    
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public Map<String, String> getListOfLanguages() {
        return listOfLanguages;
    }

	public String getSaveAdditionalPrefLangServletUrl() {
		return saveAdditionalPrefLangServletUrl;
	}
}
