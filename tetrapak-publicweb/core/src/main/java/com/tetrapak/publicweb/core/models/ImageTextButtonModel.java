package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import com.tetrapak.publicweb.core.utils.LinkUtils;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTextButtonModel {

    @Inject
    private String title;

    @Inject
    private String subtitle;

    @Inject
    private String description;

    @Inject
    private String ctaLinkTextI18n;

    @Inject
    private String ctaLinkURL;
    
    @Inject
    private Boolean openLinkInNewWindow;

    @Inject
    private String imagePath;

    @Inject
    private String imageAltI18n;

    @Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;

    @PostConstruct
    protected void init() {
        if (StringUtils.isNotEmpty(ctaLinkURL)) {
            ctaLinkURL = LinkUtils.sanitizeLink(ctaLinkURL);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getCtaLinkTextI18n() {
        return ctaLinkTextI18n;
    }

    public String getCtaLinkURL() {
        return ctaLinkURL;
    }
    
    public String getLinkType() {
        return LinkUtils.linkType(ctaLinkURL);
    }
    
    public Boolean getOpenLinkInNewWindow() {
    	return openLinkInNewWindow;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageAltI18n() {
        return imageAltI18n;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

}
