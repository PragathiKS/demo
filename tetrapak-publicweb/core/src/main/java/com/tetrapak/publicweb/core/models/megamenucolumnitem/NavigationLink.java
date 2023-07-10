package com.tetrapak.publicweb.core.models.megamenucolumnitem;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

import static com.tetrapak.publicweb.core.constants.PWConstants.ICON_CSS_CLASS_PREFIX;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationLink {

    @ValueMapValue
    private String linkLabel;

    @ValueMapValue
    private String linkUrl;

    @ValueMapValue
    private String linkDescription;

    @ValueMapValue
    private String linkIcon;

    @Self
    Resource resource;

    private String linkIconClass;

    @PostConstruct
    protected void init(){
        if(linkIcon!=null){
            Resource iconResource = resource.getResourceResolver().getResource(linkIcon);
            String iconFileName = iconResource.getName();
            if(iconFileName.contains(".")){
                linkIconClass = ICON_CSS_CLASS_PREFIX+iconFileName.substring(0,iconFileName.lastIndexOf("."));
            }else {
                linkIconClass = ICON_CSS_CLASS_PREFIX+iconFileName;
            }

        }

    }

    public String getLinkLabel() {
        return linkLabel;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public String getLinkIconClass() {
        return linkIconClass;
    }
}
