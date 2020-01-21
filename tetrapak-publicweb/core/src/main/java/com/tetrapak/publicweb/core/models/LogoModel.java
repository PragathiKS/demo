package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class LogoModel {

    @Self
    private Resource resource;

    private String imagePath;
    private String imageAltI18n;
    private String imageLink;
    
    private String cType = StringUtils.EMPTY;

    @PostConstruct
    protected void init() {
        InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
        imagePath = inheritanceValueMap1.getInherited("imagePath", String.class);
        imageAltI18n = inheritanceValueMap1.getInherited("imageAltI18n", String.class);
        imageLink = inheritanceValueMap1.getInherited("imageLink", String.class);
        if (resource.getPath().contains("/header/")) {
        	cType = "header";
        } else if (resource.getPath().contains("/footer/")) {
        	cType = "footer";
        }
    }

    public Resource getResource() {
        return resource;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageAltI18n() {
        return imageAltI18n;
    }

    public String getImageLink() {
        return LinkUtils.sanitizeLink(imageLink);
    }
    
    public String getLinkType() {
        return LinkUtils.linkType(imageLink);
    }
    
    public String getCType() {
        return cType;
    }
}
