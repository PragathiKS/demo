package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    private String selectedLanguage;
    
    private Map<String, String> listOfLanguages = new HashMap<>();

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getAdditionalSelectedLanguage(request, userPreferenceService);
        
        Resource globalConfigResource = GlobalUtil.getGlobalConfigurationResource(request);
        if (null != globalConfigResource) {
            Resource languageResource = globalConfigResource.getChild("languages");
            if (null == languageResource) {
                return;
            }



            Iterator<Resource> itr = languageResource.listChildren();
            while (itr.hasNext()) {
                ValueMap languageNodeValueMap = itr.next().getValueMap();
                listOfLanguages.put(
                        (String) languageNodeValueMap.get("langCode"), (String) languageNodeValueMap.get("languageDisplayName")
                );
            }
            listOfLanguages = listOfLanguages.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }
    }
    
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public Map<String, String> getListOfLanguages() {
        return listOfLanguages;
    }
}
