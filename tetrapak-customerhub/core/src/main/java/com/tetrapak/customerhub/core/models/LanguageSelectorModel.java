package com.tetrapak.customerhub.core.models;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<String, String> listOfLanguages = new HashMap<>();

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);

        Resource globalConfigResource = GlobalUtil.getGlobalConfigurationResource(request);
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
                listOfLanguages.put(
                        (String) languageNodeValueMap.get("langCode"), (String) languageNodeValueMap.get("languageDisplayName")
                );
            }
            listOfLanguages = listOfLanguages.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }
    }

    public String getHeadingI18n() {
        return headingI18n;
    }

    public String getCloseBtnTextI18n() {
        return closeBtnTextI18n;
    }

    public String getLocale() {
        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public Map<String, String> getListOfLanguages() {
        return listOfLanguages;
    }
}
