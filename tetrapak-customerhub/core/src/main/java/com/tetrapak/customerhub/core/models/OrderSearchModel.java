package com.tetrapak.customerhub.core.models;

import com.google.gson.Gson;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OrderSearchModel {

    @Self
    private Resource resource;

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

    @OSGiService
    APIGEEService apigeeService;

    private static final String DEFAULT_JSON = "/etc/designs/customerhub/jsonData/orderSearchSummary.json";

    private String apiURL;

    private String config;

    @PostConstruct
    protected void init() {
        Map<String, String> i18KeyMap = new HashMap();
        i18KeyMap.put("resetButtonTextI18n", resetButtonTextI18n);
        i18KeyMap.put("searchButtonTextI18n", searchButtonTextI18n);
        i18KeyMap.put("allStatusesTextI18n", allStatusesTextI18n);
        i18KeyMap.put("allAddressesTextI18n", allAddressesTextI18n);
        i18KeyMap.put("dateRangeLabelI18n", dateRangeLabelI18n);
        i18KeyMap.put("deliveryAddressLabelI18n", deliveryAddressLabelI18n);
        i18KeyMap.put("orderStatusLabelI18n", orderStatusLabelI18n);
        i18KeyMap.put("searchInputLabelI18n", searchInputLabelI18n);
        i18KeyMap.put("searchTermPlaceholderI18n", searchTermPlaceholderI18n);
        i18KeyMap.put("apiURL", GlobalUtil.getApiURL(apigeeService, DEFAULT_JSON));
        Gson gson = new Gson();
        config = gson.toJson(i18KeyMap);
    }

    public String getConfig() {
        return config;
    }
}
