package com.tetrapak.publicweb.core.models.multifield;

import java.util.Objects;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * The Class TabBeanModel.
 * 
 * @author Sandip Kumar
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TabModel {

    /** The resource. */
    @Self
    private Resource resource;

    /** The tabType. */
    @ValueMapValue
    private String tabType;

    /** The subTitle. */
    @ValueMapValue
    private String subTitle;

    /** The title. */
    @ValueMapValue
    private String title;

    /** The description. */
    @ValueMapValue
    private String description;

    /** The linkText. */
    @ValueMapValue
    private String linkText;

    /** The linkURL. */
    @ValueMapValue
    private String linkURL;

    /** The videoSource. */
    @ValueMapValue
    private String videoSource;

    /** The youtubeEmbedURL. */
    @Named(value = "youtubeVideoID")
    @ValueMapValue
    private String youtubeVideoID;

    /** The damVideoPath. */
    @ValueMapValue
    private String damVideoPath;

    /** The thumbnailPath. */
    @ValueMapValue
    private String thumbnailPath;

    /** The thumbnailAltText. */
    @ValueMapValue
    private String thumbnailAltText;

    /** The fileReference. */
    @ValueMapValue
    private String fileReference;

    /** The pw button theme. */
    private String pwButtonTheme;

    /** The article date. */
     @ValueMapValue
    private String articleDate;

    /** The alt. */
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
     * Gets the link Text.
     *
     * @return link Text
     */
    public String getLinkText() {
        return linkText;
    }

    /**
     * Gets the link URL.
     *
     * @return link URL
     */
    public String getLinkURL() {
        return linkURL;
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
     * Gets the article date.
     *
     * @return the article date
     */
    public String getArticleDate() {
        return articleDate;
    }    

     /**
     * Sets the articleDate.
     *
     * @param articleDate
     *            the articleDate
     */
    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    /**
     * Gets the youtube Embed URL.
     *
     * @return youtube Embed URL
     */
    public String getYoutubeEmbedURL() {
        String youtubeEmbedURL = StringUtils.EMPTY;
        if (youtubeVideoID != null) {
            youtubeEmbedURL = YOUTUBE_URL_PREFIX
                    + (youtubeVideoID.contains("?") ? (youtubeVideoID + "&" + PWConstants.ENABLE_JS_API)
                            : (youtubeVideoID + "?" + PWConstants.ENABLE_JS_API));
        }
        return youtubeEmbedURL;
    }

    /**
     * Gets the damVideoPath.
     *
     * @return damVideoPath
     */
    public String getDamVideoPath() {
        if (Objects.nonNull(slingSettingsService) && Objects.nonNull(dynamicMediaService)
                && !slingSettingsService.getRunModes().contains(AUTHOR)) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(resource.getResourceResolver(), damVideoPath,
                    dynamicMediaService);
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
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the link text.
     *
     * @param linkText
     *            the new link text
     */
    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    /**
     * Sets the link URL.
     *
     * @param linkURL
     *            the new link URL
     */
    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    /**
     * Sets the file reference.
     *
     * @param fileReference
     *            the new file reference
     */
    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    /**
     * Sets the alt.
     *
     * @param alt
     *            the new alt
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
        if (resource != null) {
            Resource pageContentRes = resource.getParent().getParent();
            if ("manual".equalsIgnoreCase(pageContentRes.getValueMap().get("contentType").toString())) {
                pwButtonTheme = pageContentRes.getValueMap().get("pwButtonTheme").toString();
            }
        }
        return pwButtonTheme;
    }

    /**
     * Sets the pw button theme.
     *
     * @param pwButtonTheme
     *            the new pw button theme
     */
    public void setPwButtonTheme(String pwButtonTheme) {
        this.pwButtonTheme = pwButtonTheme;
    }

    /**
     * Gets the tab type.
     *
     * @return the tab type
     */
    public String getTabType() {
        return tabType;
    }

    /**
     * Sets the tab type.
     *
     * @param tabType
     *            the new tab type
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
     * @param path
     *            the path
     * @return the substring after last
     */
    private String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, FORWARD_SLASH);
    }

    /**
     * Sets the sub title.
     *
     * @param subTitle
     *            the new sub title
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * Sets the video source.
     *
     * @param videoSource
     *            the new video source
     */
    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    /**
     * Sets the dam video path.
     *
     * @param damVideoPath
     *            the new dam video path
     */
    public void setDamVideoPath(String damVideoPath) {
        this.damVideoPath = damVideoPath;
    }

    /**
     * Sets the thumbnail path.
     *
     * @param thumbnailPath
     *            the new thumbnail path
     */
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    /**
     * Sets the thumbnail alt text.
     *
     * @param thumbnailAltText
     *            the new thumbnail alt text
     */
    public void setThumbnailAltText(String thumbnailAltText) {
        this.thumbnailAltText = thumbnailAltText;
    }

}
