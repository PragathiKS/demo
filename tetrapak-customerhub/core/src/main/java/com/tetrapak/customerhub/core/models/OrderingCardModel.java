package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderingCardModel {

    @Self
    private Resource resource;

    @Inject
    private String titleI18n;

    @Inject
    private String preferencesTitleI18n;

    @Inject
    private String preferencesDescriptionI18n;

    @Inject
    private String allOrdersI18n;

    @Inject
    private String saveSettingsI18n;

    @Inject
    private String closeBtnI18n;

    @Inject
    private String preferencesBtnI18n;

    @Inject
    private String saveErrorI18n;

    @Inject
    private String noDataI18n;

    @Inject
    private String dataErrorI18n;

    @Inject
    private String allOrdersLink;

    @OSGiService
    APIGEEService apigeeService;

    @OSGiService
    UserPreferenceService userPreferenceService;

    private String i18nKeys;

    private Set<String> savedPreferences;

    private Set<String> defaultFields;

    private Set<String> disabledFields;

    private static final String DEFAULT_JSON = "/apps/settings/wcm/designs/customerhub/jsonData/orderingCardData.json";

    private static final String PREFERENCES_JSON = "/apps/settings/wcm/designs/customerhub/jsonData/orderingPreference.json";

    @PostConstruct
    protected void init() {
        defaultFields = new LinkedHashSet<>();
        defaultFields.add("orderNumber");
        defaultFields.add("poNumber");
        defaultFields.add("orderDate");

        disabledFields = new LinkedHashSet<>();
        disabledFields.add("contact");

        savedPreferences = new LinkedHashSet<>();
        if(null != userPreferenceService) {
            savedPreferences = userPreferenceService.getSavedPreferences(resource);
        }

        Map<String, String> i18KeyMap = new HashMap<>();
        i18KeyMap.put("title", titleI18n);
        i18KeyMap.put("preferencesTitle", preferencesTitleI18n);
        i18KeyMap.put("preferencesDescription", preferencesDescriptionI18n);
        i18KeyMap.put("allOrders", allOrdersI18n);
        i18KeyMap.put("saveSettings", saveSettingsI18n);
        i18KeyMap.put("closeBtn", closeBtnI18n);
        i18KeyMap.put("preferencesBtn", preferencesBtnI18n);
        i18KeyMap.put("saveError", saveErrorI18n);
        i18KeyMap.put("noData", noDataI18n);
        i18KeyMap.put("dataError", dataErrorI18n);
        Gson gson = new Gson();
        i18nKeys = gson.toJson(i18KeyMap);
    }


    public String getAllOrdersLink() {
        return allOrdersLink;
    }

    public String getApiURL() {
        return GlobalUtil.getApiURL(apigeeService, DEFAULT_JSON);
    }

    public String getPreferencesURL() {
        return resource.getPath() + ".preference.json";
    }

    public Set<String> getSavedPreferences() {
        return savedPreferences;
    }

    public Set<String> getDefaultFields() {
        return defaultFields;
    }

    public Set<String> getDisabledFields() {
        return disabledFields;
    }

    public String getI18nKeys() {
        return i18nKeys;
    }
}
