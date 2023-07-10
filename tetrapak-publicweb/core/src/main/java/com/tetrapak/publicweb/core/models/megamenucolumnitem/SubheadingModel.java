package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SubheadingModel {

    @ValueMapValue
    private String subheading;

    @ValueMapValue
    private String subheadingURL;

    public String getSubheading() {
        return subheading;
    }

    public String getSubheadingURL() {
        return subheadingURL;
    }
}
