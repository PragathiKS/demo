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
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.List;

import static com.tetrapak.publicweb.core.constants.PWConstants.LANG_MASTERS;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,resourceType = "publicweb/components/structure/megamenucolumnitems/navigationlinks")
@Exporter(name = "jackson", extensions = "json")
public class NavigationLinksModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @ChildResource
    private List<NavigationLink> navLinks;

    @ScriptVariable
    private Page currentPage;

    public List<NavigationLink> getNavLinks() {
       if (navLinks != null && !navLinks.isEmpty() && request!=null && currentPage!=null) {
			navLinks.stream()
					.forEach(model -> {
                        String url = model.getLinkUrl();
                        url = LinkUtils.getMarketLinkInXF(currentPage.getPath(),request,url);
                        model.setLinkUrl(LinkUtils.sanitizeLink(url, request));
                    });
		} else if (navLinks != null && !navLinks.isEmpty() && resource!=null){
           navLinks.stream()
                   .forEach(model -> {
                       model.setLinkUrl(LinkUtils.sanitizeLink(model.getLinkUrl(), resource));
                   });
       }
    
       return navLinks;
    }
}
