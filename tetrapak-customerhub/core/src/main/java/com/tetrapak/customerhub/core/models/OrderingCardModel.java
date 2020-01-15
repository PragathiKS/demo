package com.tetrapak.customerhub.core.models;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.LinkUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Model class for ordering card component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderingCardModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderingCardModel.class);
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

    @Inject
    private String orderDetailLink;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String i18nKeys;

    private Set<String> savedPreferences;

    private Set<String> defaultFields;

    private Set<String> enabledFields;

    @PostConstruct
    protected void init() {
        enabledFields = new LinkedHashSet<>();
        defaultFields = new LinkedHashSet<>();

        Resource enabledFieldsResource = resource.getChild("enabledFields");
        if (null != enabledFieldsResource) {
            Iterator<Resource> itr = enabledFieldsResource.listChildren();
            while (itr.hasNext()) {
                enabledFields.add((String) itr.next().getValueMap().get("enabledField"));
            }
        }

        Resource defaultFieldsResource = resource.getChild("defaultFields");
        if (null != defaultFieldsResource) {
            Iterator<Resource> itr = defaultFieldsResource.listChildren();
            while (itr.hasNext()) {
                defaultFields.add((String) itr.next().getValueMap().get("defaultField"));
            }
        }

        savedPreferences = new LinkedHashSet<>();
        savedPreferences.addAll(defaultFields);
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        if (null != session) {
            String userID = session.getUserID();
            getUserPreferences(userID);

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

    /**
     * Fetch the order preferences for the user
     *
     * @param userID
     */
    private void getUserPreferences(String userID) {
        if (null != userPreferenceService) {
            String savedPreferencesStr = userPreferenceService.getSavedPreferences(userID,
                    CustomerHubConstants.ORDER_PREFERENCES);
            if (null != savedPreferencesStr) {
                Type mapType = new TypeToken<String[]>() {
                }.getType();
                Gson gson = new Gson();
                try {
                    String[] prefData = gson.fromJson(savedPreferencesStr, mapType);
                    savedPreferences.addAll(Arrays.asList(prefData));
                } catch (JsonSyntaxException jsonSyntaxException) {
                    LOGGER.error("Some exception occured while parsing type: {} from the JSON {} !!", mapType,
                            savedPreferencesStr, jsonSyntaxException);
                }
            }
        }
    }

    /**
     * @return allOrdersLink
     */
    public String getAllOrdersLink() {
        return LinkUtil.getValidLink(resource, allOrdersLink);
    }

    /**
     * @return orderDetailLink
     */
    public String getOrderDetailLink() {
        return LinkUtil.getValidLink(resource, orderDetailLink);
    }

    /**
     * @return PreferencesURL
     */
    public String getPreferencesURL() {
        return resource.getPath() + ".preference.json";
    }

    /**
     * @return savedPreferences
     */
    public Set<String> getSavedPreferences() {
        return new LinkedHashSet<>(savedPreferences);
    }

    /**
     * @return defaultFields
     */
    public Set<String> getDefaultFields() {
        return new LinkedHashSet<>(defaultFields);
    }

    /**
     * @return enabledFields
     */
    public Set<String> getEnabledFields() {
        return new LinkedHashSet<>(enabledFields);
    }

    /**
     * @return i18nKeys
     */
    public String getI18nKeys() {
        return i18nKeys;
    }
}
