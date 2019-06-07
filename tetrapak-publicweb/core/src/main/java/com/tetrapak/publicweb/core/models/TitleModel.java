package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct; 
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TitleModel {

    @Self
    private Resource resource;

    @Inject
    private String titleText;

    @Inject
    private String headingFontSize;

    @Inject
    private String headingType;

    @PostConstruct
    protected void init() {
        if (StringUtils.isBlank(titleText)) {
        	ResourceResolver resolver = resource.getResourceResolver();
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            Page currentPage = pageManager.getContainingPage(resource);
            titleText = currentPage.getTitle();
            headingFontSize = "large";
        }

    }
    public String getTitleText() {
        return titleText;
    }
    public String getHeadingFontSize() {
        return headingFontSize;
    }

    public String getHeadingType() {
        return headingType;
    }
}
