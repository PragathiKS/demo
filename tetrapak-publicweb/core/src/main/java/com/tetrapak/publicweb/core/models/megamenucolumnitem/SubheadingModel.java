package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/subheading")
@Exporter(name = "jackson", extensions = "json")
public class SubheadingModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String subheading;

    @ValueMapValue
    private String subheadingURL;

    public String getSubheading() {
        return subheading;
    }

    public String getSubheadingURL() {
        return LinkUtils.sanitizeLink(subheadingURL, request);
    }
}
