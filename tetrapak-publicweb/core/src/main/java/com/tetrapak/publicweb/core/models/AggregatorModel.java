package com.tetrapak.publicweb.core.models;

import javax.inject.Named;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class AggregatorModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AggregatorModel {
    
    /** The title */
    @Named(value = "jcr:title")
    @ValueMapValue
    private String title;
    
    /** The tags */
    @ValueMapValue
    @Named(value = "cq:tags")
    private String[] tags;
    
    /** The description */
    @ValueMapValue
    @Named(value = "jcr:description")
    private String description;
    
    /** The imagePath */
    @ValueMapValue
    private String imagePath;
    
    /** The altText */
    @ValueMapValue
    private String altText;
    
    /** The linkText */
    @ValueMapValue
    private String linkText;
    
    /** The linkPath */
    @ValueMapValue
    private String linkPath;
  
    /** The pwButtonTheme */
    @ValueMapValue
    private String pwButtonTheme;
    
    /** The pwLinkTheme */
    @ValueMapValue
    private String pwLinkTheme;
    
    /** The linkTarget */
    @ValueMapValue
    private String linkTarget;

    public String getTitle() {
        return title;
    }

    public String[] getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAltText() {
        return altText;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }

    public String getPwLinkTheme() {
        return pwLinkTheme;
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    

}
