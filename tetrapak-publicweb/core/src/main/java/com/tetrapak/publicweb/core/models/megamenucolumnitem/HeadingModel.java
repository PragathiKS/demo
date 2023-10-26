package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.tetrapak.publicweb.core.constants.PWConstants.LANG_MASTERS;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/heading")
@Exporter(name = "jackson", extensions = "json")
public class HeadingModel {

    /** The request. */
    @Inject
    private SlingHttpServletRequest request;

    @Inject
    private Resource resource;

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String headingURL;

    @ValueMapValue
    private String headingStyle;

    @ScriptVariable
    private Page currentPage;

    public String getHeading() {
        return heading;
    }

    public String getHeadingURL() {

        if(request!=null && currentPage!=null){
            headingURL = LinkUtils.getMarketLinkInXF(currentPage.getPath(),request,headingURL);

            return LinkUtils.sanitizeLink(headingURL, request);
        }else{
            return LinkUtils.sanitizeLink(headingURL, resource);
        }

    }

    public String getHeadingStyle() {
        return headingStyle;
    }
}
