package com.tetrapak.customerhub.core.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.utils.LinkUtil;

/**
 * Model class for order search component
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderSearchModel {

    @Self
    private Resource resource;

    @Inject
    private String setDatesBtnI18n;

    @Inject
    private String closeBtnI18n;

    @Inject
    private String selectDatesI18n;

    @Inject
    private String resetButtonTextI18n;

    @Inject
    private String searchButtonTextI18n;

    @Inject
    private String allStatusesTextI18n;

    @Inject
    private String allAddressesTextI18n;

    @Inject
    private String dateRangeLabelI18n;

    @Inject
    private String deliveryAddressLabelI18n;

    @Inject
    private String orderStatusLabelI18n;

    @Inject
    private String searchInputLabelI18n;

    @Inject
    private String searchTermPlaceholderI18n;

    @Inject
    private String orderDetailLink;

    private Set<String> enabledFields;

    private String config;

    @PostConstruct
    protected void init() {
        enabledFields = new LinkedHashSet<>();
        Resource childResource = resource.getChild("enabledFields");
        if (null != childResource) {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                enabledFields.add((String) itr.next().getValueMap().get("enabledField"));
            }
        }

        Map<String, Object> i18KeyMap = new HashMap<>();
        i18KeyMap.put("setDatesBtnI18n", setDatesBtnI18n);
        i18KeyMap.put("closeBtnI18n", closeBtnI18n);
        i18KeyMap.put("selectDatesI18n", selectDatesI18n);
        i18KeyMap.put("resetButtonTextI18n", resetButtonTextI18n);
        i18KeyMap.put("searchButtonTextI18n", searchButtonTextI18n);
        i18KeyMap.put("allStatusesTextI18n", allStatusesTextI18n);
        i18KeyMap.put("allAddressesTextI18n", allAddressesTextI18n);
        i18KeyMap.put("dateRangeLabelI18n", dateRangeLabelI18n);
        i18KeyMap.put("deliveryAddressLabelI18n", deliveryAddressLabelI18n);
        i18KeyMap.put("orderStatusLabelI18n", orderStatusLabelI18n);
        i18KeyMap.put("searchInputLabelI18n", searchInputLabelI18n);
        i18KeyMap.put("searchTermPlaceholderI18n", searchTermPlaceholderI18n);
        i18KeyMap.put("enabledFields", enabledFields);
        i18KeyMap.put("orderDetailLink", getOrderDetailLink());


        Gson gson = new Gson();
        config = gson.toJson(i18KeyMap);
    }

    public String getConfig() {
        return config;
    }

    public String getOrderDetailLink() {
        return LinkUtil.getValidLink(resource, orderDetailLink);
    }

    public Set<String> getEnabledFields() {
        return new LinkedHashSet<>(enabledFields);
    }
}
