package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationLinksModel {

    @Inject
    private List<NavigationLink> navLinks;

    public List<NavigationLink> getNavLinks() {
        return navLinks;
    }
}
