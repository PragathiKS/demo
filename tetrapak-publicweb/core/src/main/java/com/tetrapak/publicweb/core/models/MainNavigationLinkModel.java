package com.tetrapak.publicweb.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

/**
 * The Class LinkModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MainNavigationLinkModel {
    
    /** The link text. */
    @ValueMapValue
    private String linkText;

    /** The link path. */
    @ValueMapValue
    private String linkUrl;

    @ValueMapValue
    private String megaMenuPath;

    @Self
    private Resource resource;

    /**
     * Gets the link text.
     *
     * @return the link text
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Gets the link url.
     *
     * @return the link url
     */
    public String getLinkUrl() {
        return linkUrl;
    }
    
    /**
     * Sets the linkUrl.
     *
     * @param linkUrl the new linkUrl
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getMegaMenuPath() {

        return megaMenuPath;
    }

    public void setMegaMenuPath(String megaMenuPath) {
        this.megaMenuPath = megaMenuPath;
    }

    @PostConstruct
    protected void init(){
        if(StringUtils.isNotBlank(megaMenuPath) && resource.getResourceResolver().getResource(megaMenuPath)==null){
            megaMenuPath = StringUtils.EMPTY;
        }
    }
}
