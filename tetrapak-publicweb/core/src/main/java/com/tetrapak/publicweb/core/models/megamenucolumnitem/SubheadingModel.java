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
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

import static com.tetrapak.publicweb.core.constants.PWConstants.LANG_MASTERS;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/subheading")
@Exporter(name = "jackson", extensions = "json")
public class SubheadingModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @ValueMapValue
    private String subheading;

    @ValueMapValue
    private String subheadingURL;

    @ValueMapValue
    private String subheadingOverviewText;

    @ScriptVariable
    private Page currentPage;

    public String getSubheading() {
        return subheading;
    }

    public String getSubheadingURL() {

        if(request!=null && currentPage!=null){
            subheadingURL = LinkUtils.getMarketLinkInXF(currentPage.getPath(),request,subheadingURL);

            return LinkUtils.sanitizeLink(subheadingURL, request);
        }else{
            return LinkUtils.sanitizeLink(subheadingURL, resource);
        }
    }

    public String getSubheadingOverviewText() {
        return subheadingOverviewText;
    }
}
