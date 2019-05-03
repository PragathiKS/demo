package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import com.tetrapak.publicweb.core.utils.LinkUtils;


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
        return cityInputLabel;
    }

    public String getCityInputLabel() {
        return title;
    }

    public String getFiltersLabel() {
        return filtersLabel;
    }
}
