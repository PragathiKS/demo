package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
    private String allOrdersLink;

    @OSGiService
    APIGEEService apigeeService;

    private Set<String> savedPreferences;

    private Set<String> defaultFields;

    private Set<String> disabledFields;

    private static final String DEFAULT_JSON = "/apps/settings/wcm/designs/customerhub/jsonData/orderingCardData.json";

    private static final String PREFERENCES_JSON = "/apps/settings/wcm/designs/customerhub/jsonData/orderingPreference.json";

    @PostConstruct
    protected void init() {
        defaultFields.add("orderNumber");
        defaultFields.add("poNumber");
        defaultFields.add("orderDate");

        disabledFields.add("contact");

        savedPreferences.add("orderNumber");
        savedPreferences.add("poNumber");
        savedPreferences.add("orderDate");
        savedPreferences.add("status");
        savedPreferences.add("contact");
    }

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getPreferencesTitleI18n() {
        return preferencesTitleI18n;
    }

    public String getPreferencesDescriptionI18n() {
        return preferencesDescriptionI18n;
    }

    public String getAllOrdersI18n() {
        return allOrdersI18n;
    }

    public String getSaveSettingsI18n() {
        return saveSettingsI18n;
    }

    public String getCloseBtnI18n() {
        return closeBtnI18n;
    }

    public String getPreferencesBtnI18n() {
        return preferencesBtnI18n;
    }

    public String getSaveErrorI18n() {
        return saveErrorI18n;
    }

    public String getAllOrdersLink() {
        return allOrdersLink;
    }

    public String getApiURL() {
        return GlobalUtil.getApiURL(apigeeService, DEFAULT_JSON);
    }

    public String getPreferencesURL() {
        return GlobalUtil.getPreferencesURL(apigeeService, PREFERENCES_JSON);
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
}
