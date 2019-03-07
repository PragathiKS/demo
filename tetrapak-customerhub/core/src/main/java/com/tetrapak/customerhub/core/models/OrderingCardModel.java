package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

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
    
    @Inject
    private String orderDetailLink;

    @OSGiService
    APIGEEService apigeeService;

    private static final String DEFAULT_JSON = "/etc/designs/customerhub/jsonData/orderSearchSummary.json";
    
    private static final String PREFERENCES_JSON = "/apps/settings/wcm/designs/customerhub/jsonData/orderingPreference.json";
    
    
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

    public String getOrderDetailLink() {
        return orderDetailLink;
    }

    public String getApiURL() {
        return GlobalUtil.getApiURL(apigeeService, DEFAULT_JSON);
    }

    public String getPreferencesURL() {
        return GlobalUtil.getPreferencesURL(apigeeService, PREFERENCES_JSON);
    }
}
