package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.day.cq.wcm.api.Page;


@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactAnchorLinkModel {

	@Inject
    private Page currentPage;
	
	private Boolean hideContactAnchorLink;

    @PostConstruct
    protected void init() {
        if (currentPage != null) {
            Resource jcrContentResource = currentPage.getContentResource();
            BasePageModel basePageModel = jcrContentResource.adaptTo(BasePageModel.class);
            hideContactAnchorLink = basePageModel.getPageContent().getHideContactAnchorLink();
        }
    }

	public Boolean getHideContactAnchorLink() {
		return hideContactAnchorLink;
	}
    
}
