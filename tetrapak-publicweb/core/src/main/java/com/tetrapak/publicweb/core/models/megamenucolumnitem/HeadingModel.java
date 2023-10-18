package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/heading")
@Exporter(name = "jackson", extensions = "json")
public class HeadingModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

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
        if(request!=null){
            return LinkUtils.sanitizeLink(headingURL, request);
        }else{
            return LinkUtils.sanitizeLink(headingURL, resource);
        }

    }

    public String getHeadingStyle() {
        return headingStyle;
    }
}
