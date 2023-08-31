package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;
import java.util.List;

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

    public List<NavigationLink> getNavLinks() {
       if (navLinks != null && !navLinks.isEmpty() && request!=null) {
			navLinks.stream()
					.forEach(model -> model.setLinkUrl(LinkUtils.sanitizeLink(model.getLinkUrl(), request)));
		} else if (navLinks != null && !navLinks.isEmpty() && resource!=null){
           navLinks.stream()
                   .forEach(model -> model.setLinkUrl(LinkUtils.sanitizeLink(model.getLinkUrl(), resource)));
       }
    
       return navLinks;
    }
}
