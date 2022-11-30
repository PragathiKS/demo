package com.tetrapak.supplierportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderConfigurationModel {

    @ValueMapValue private String logoUrl;

    @ValueMapValue private String logoLink;

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getLogoLink() {
        return logoLink;
    }
}

