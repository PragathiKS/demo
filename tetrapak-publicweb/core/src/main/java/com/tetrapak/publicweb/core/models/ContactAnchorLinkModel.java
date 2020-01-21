package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


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
            hideContactAnchorLink = basePageModel.getPageContent().getHideContactFooterForm();
        }
    }

	public Boolean getHideContactAnchorLink() {
		return hideContactAnchorLink;
	}
    
}
