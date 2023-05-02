package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeadingModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String headingURL;

    public String getHeading() {
        return heading;
    }

    public String getHeadingURL() {
        return headingURL;
    }
}
