package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OfficeLocatorModel {

    @Self
    private Resource resource;

    @Inject
    private String title;

    @Inject
    private String cityInputLabel;

    @Inject
    private String filtersLabel;

    @PostConstruct
    protected void init() {

    }

    public String getTitle() {
        return title;
    }

    public String getCityInputLabel() {
        return cityInputLabel;
    }

    public String getFiltersLabel() {
        return filtersLabel;
    }
}
