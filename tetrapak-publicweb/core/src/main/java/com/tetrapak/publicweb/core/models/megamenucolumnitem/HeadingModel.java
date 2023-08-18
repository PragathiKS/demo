package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/heading")
public class HeadingModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String headingURL;

    @ValueMapValue
    private String headingStyle;

    public String getHeading() {
        return heading;
    }

    public String getHeadingURL() {
       return LinkUtils.sanitizeLink(headingURL, request);
    }

    public String getHeadingStyle() {
        return headingStyle;
    }
}
