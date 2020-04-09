package com.tetrapak.publicweb.core.models.multifield;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.LinkUtils;

/**
 * The Class TabBeanModel.
 * @author Sandip Kumar
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabModel {
    
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
    private String youtubeVideoID;
    
    /** The damVideoPath */
    @ValueMapValue
    private String damVideoPath;
    
    /** The thumbnailPath */
    @ValueMapValue
    private String thumbnailPath;
    
    /** The thumbnailAltText */
    @ValueMapValue
    private String thumbnailAltText;
    
    /** The fileReference */
    @ValueMapValue
    private String fileReference;
    
    /** The pw button theme. */
    private String pwButtonTheme;
    
    /** The alt */
    @ValueMapValue
    private String alt;
    
    /** The sling settings service. */
    @OSGiService
    private SlingSettingsService slingSettingsService;

    /** The dynamic media service. */
    @OSGiService
    private DynamicMediaService dynamicMediaService;
    
    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";
    
    /** The Constant YOUTUBE_URL_PREFIX. */
    private static final String YOUTUBE_URL_PREFIX = "https://www.youtube.com/embed/";

    /** The Constant AUTHOR. */
    private static final String AUTHOR = "author";

    /**
     * Gets the sub title.
     *
     * @return sub title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Gets the title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the link Texti18n.
     *
     * @return link Texti18n
     */
    public String getLinkTexti18n() {
        return linkTexti18n;
    }

    /**
     * Gets the link URL.
     *
     * @return link URL
     */
    public String getLinkURL() {
        return LinkUtils.sanitizeLink(linkURL);
    }

    /**
     * Gets the target blank.
     *
     * @return target blank
     */
    public String getTargetBlank() {
        return targetBlank;
    }

    /**
     * Gets the video source.
     *
     * @return video source
     */
    public String getVideoSource() {
        return videoSource;
    }

    /**
     * Gets the youtube Embed URL.
     *
     * @return youtube Embed URL
     */
    public String getYoutubeEmbedURL() {
	String youtubeEmbedURL = StringUtils.EMPTY;
	if (youtubeVideoID != null) {
	    youtubeEmbedURL = YOUTUBE_URL_PREFIX + youtubeVideoID;
	}
	return youtubeEmbedURL;
    }

    /**
     * Gets the damVideoPath.
     *
     * @return damVideoPath
     */
    public String getDamVideoPath() {
	if (!slingSettingsService.getRunModes().contains(AUTHOR) && null != dynamicMediaService) {
	    damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
	}
	return damVideoPath;
    }

    /**
     * Gets the thumbnail Path.
     *
     * @return thumbnail Path
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    /**
     * Gets the thumbnail Alt Text.
     *
     * @return thumbnail Alt Text
     */
    public String getThumbnailAltText() {
        return thumbnailAltText;
    }

    /**
     * Gets the file Refrence.
     *
     * @return file Refrence
     */
    public String getFileReference() {
        return fileReference;
    }

    /**
     * Gets the alt.
     *
     * @return alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * Gets the pw Link Theme.
     *
     * @return pw Link Theme
     */
    public String getPwLinkTheme() {
        return pwLinkTheme;
    }

    /**
     * @param pwLinkTheme
     */
    public void setPwLinkTheme(String pwLinkTheme) {
        this.pwLinkTheme = pwLinkTheme;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param linkTexti18n
     */
    public void setLinkTexti18n(String linkTexti18n) {
        this.linkTexti18n = linkTexti18n;
    }

    /**
     * @param linkURL
     */
    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    /**
     * @param targetBlank
     */
    public void setTargetBlank(String targetBlank) {
        this.targetBlank = targetBlank;
    }

    /**
     * @param fileRefrence
     */
    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    /**
     * @param alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
     * Gets the pw button theme.
     *
     * @return pw button theme
     */
    public String getPwButtonTheme() {
        Resource pageContentRes = resource.getParent().getParent();
        if("manual".equalsIgnoreCase(pageContentRes.getValueMap().get("contentType").toString())) {
            pwButtonTheme = pageContentRes.getValueMap().get("pwButtonTheme").toString();
        }
        return pwButtonTheme;
    }

    /**
     * @param pwButtonTheme
     */
    public void setPwButtonTheme(String pwButtonTheme) {
        this.pwButtonTheme = pwButtonTheme;
    }

    public String getTabType() {
        return tabType;
    }

    /**
     * @param tabType
     */
    public void setTabType(String tabType) {
        this.tabType = tabType;
    }
    
    /**
     * Gets the asset name.
     *
     * @return the asset name
     */
    public String getAssetName() {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(linkURL)) {
            assetName = getSubstringAfterLast(linkURL);
        }
        return assetName;
    }

    /**
     * Gets the video name.
     *
     * @return the video name
     */
    public String getVideoName() {
        String videoName = StringUtils.EMPTY;
        if (StringUtils.isNoneEmpty(damVideoPath)) {
            videoName = getSubstringAfterLast(damVideoPath);
        }
        return videoName;
    }

    /**
     * Gets the substring after last.
     *
     * @param path the path
     * @return the substring after last
     */
    private String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, FORWARD_SLASH);
    }
    

}
