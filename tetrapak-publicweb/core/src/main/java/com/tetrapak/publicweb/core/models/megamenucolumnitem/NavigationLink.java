package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationLink {

    @ValueMapValue
    private String linkLabel;

    @ValueMapValue
    private String linkUrl;

    @ValueMapValue
    private String linkDescription;

    public String getLinkLabel() {
        return linkLabel;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkDescription() {
        return linkDescription;
    }
}
