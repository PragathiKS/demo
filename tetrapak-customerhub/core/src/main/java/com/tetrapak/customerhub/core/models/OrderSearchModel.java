package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.services.APIJEEService;
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

    @OSGiService
    APIJEEService apiJeeService;

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
        return null != apiJeeService ? apiJeeService.getApiJeeServiceUrl() : "/etc/designs/customerhub/jsonData/orderSearchSummary.json";
    }
}
