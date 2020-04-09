package com.tetrapak.publicweb.core.models.multifield;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * The Class TabBeanModel.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabBeanModel {
    
    /** The resource */
    @Self
    Resource resource;
    
    /** The tabType */
    @ValueMapValue
    private String tabType;
    
    /** The subTitle */
    @ValueMapValue
    private String subTitle;
    
    /** The title */
    @ValueMapValue
    private String title;
    
    /** The description */
    @ValueMapValue
    private String description;
    
    /** The linkTexti18n */
    @ValueMapValue
    private String linkTexti18n;
    
    /** The linkURL */
    @ValueMapValue
    private String linkURL;
    
    /** The pwLinkTheme */
    @ValueMapValue
    private String pwLinkTheme;
    
    /** The targetBlank */
    @ValueMapValue
    private String targetBlank;
    
    /** The videoSource */
    @ValueMapValue
    private String videoSource;
  
    /** The youtubeEmbedURL */
    @Named(value = "youtubeVideoID")
    @ValueMapValue
    private String youtubeEmbedURL;
    
    /** The damVideoPath */
    @ValueMapValue
    private String damVideoPath;
    
    /** The thumbnailPath */
    @ValueMapValue
    private String thumbnailPath;
    
    /** The thumbnailAltText */
    @ValueMapValue
    private String thumbnailAltText;
    
    /** The fileRefrence */
    @ValueMapValue
    private String fileRefrence;
    
    /** The pw button theme. */
    private String pwButtonTheme;
    
    /** The alt */
    @ValueMapValue
    private String alt;

    public String getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkTexti18n() {
        return linkTexti18n;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public String getTargetBlank() {
        return targetBlank;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public String getYoutubeEmbedURL() {
        return youtubeEmbedURL;
    }

    public String getDamVideoPath() {
        return damVideoPath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getThumbnailAltText() {
        return thumbnailAltText;
    }

    public String getFileRefrence() {
        return fileRefrence;
    }

    public String getAlt() {
        return alt;
    }

    public String getPwLinkTheme() {
        return pwLinkTheme;
    }

    public void setPwLinkTheme(String pwLinkTheme) {
        this.pwLinkTheme = pwLinkTheme;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLinkTexti18n(String linkTexti18n) {
        this.linkTexti18n = linkTexti18n;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public void setTargetBlank(String targetBlank) {
        this.targetBlank = targetBlank;
    }

    public void setFileRefrence(String fileRefrence) {
        this.fileRefrence = fileRefrence;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getPwButtonTheme() {
	Resource pageContentRes = resource.getParent().getParent();
        if("manual".equalsIgnoreCase(pageContentRes.getValueMap().get("contentType").toString())) {
            pwButtonTheme = pageContentRes.getValueMap().get("pwButtonTheme").toString();
        }
        return pwButtonTheme;
    }

    public void setPwButtonTheme(String pwButtonTheme) {
        this.pwButtonTheme = pwButtonTheme;
    }

    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }
    

}
