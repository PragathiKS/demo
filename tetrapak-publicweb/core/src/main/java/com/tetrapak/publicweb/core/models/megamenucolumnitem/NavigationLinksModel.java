package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationLinksModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    @Via("resource")
    private List<NavigationLink> navLinks;

    public List<NavigationLink> getNavLinks() {
       if (navLinks != null && !navLinks.isEmpty()) {
			navLinks.stream()
					.forEach(model -> model.setLinkUrl(LinkUtils.sanitizeLink(model.getLinkUrl(), request)));
		}
    
       return navLinks;
    }
}
