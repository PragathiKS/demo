package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.APIJEEService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;

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
    APIJEEService apiJeeService;

    private static final String DEFAULT_JSON = "/etc/designs/customerhub/jsonData/orderSearchSummary.json";

    private String apiURL;

    public String getResetButtonTextI18n() {
        return resetButtonTextI18n;
    }

    public String getSearchButtonTextI18n() {
        return searchButtonTextI18n;
    }

    public String getAllStatusesTextI18n() {
        return allStatusesTextI18n;
    }

    public String getAllAddressesTextI18n() {
        return allAddressesTextI18n;
    }

    public String getApiURL() {
        return GlobalUtil.getApiURL(apiJeeService, DEFAULT_JSON);
    }

    public String getDateRangeLabelI18n() {
        return dateRangeLabelI18n;
    }

    public String getDeliveryAddressLabelI18n() {
        return deliveryAddressLabelI18n;
    }

    public String getOrderStatusLabelI18n() {
        return orderStatusLabelI18n;
    }

    public String getSearchInputLabelI18n() {
        return searchInputLabelI18n;
    }

    public String getSearchTermPlaceholderI18n() {
        return searchTermPlaceholderI18n;
    }

}
