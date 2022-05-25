package com.tetrapak.customerhub.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.settings.SlingSettingsService;

import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtil;

/**
 * Model class for Text Video component.
 *
 * @author tustusha
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextVideoModel {

    @Self
    private Resource resource;

    @OSGiService
    private DynamicMediaService dynamicMediaService;

    @OSGiService
    private SlingSettingsService slingSettingsService;

    @Inject
    private String title;

    @Inject
    private String description;

    @Inject
    private String linkTextI18n;

    @Inject
    private String linkURL;

    private String linkType;

    @Inject
    private String videoSource;

    @Inject
    private String youtubeVideoID;

    @Inject
    private String damVideoPath;

    @Inject
    private String thumbnailPath;

    private String youtubeEmbedURL;

    @Inject
    private String textAlignment;
    
    @Inject
    private Boolean packageDesign;
    
    @Inject
    private String subTitle;
    
    @Inject
    private String pwTheme;

    @Inject
    private String pwButtonTheme;
    
    @Inject
    private String anchorId;

    @Inject
    private String anchorTitle;

    @PostConstruct
    protected void init() {
        linkURL = LinkUtil.getValidLink(resource, linkURL);
        if (youtubeVideoID != null) {
            youtubeEmbedURL = "https://www.youtube.com/embed/" + youtubeVideoID;
        }
        if (!slingSettingsService.getRunModes().contains("author") && null != dynamicMediaService) {
            damVideoPath = GlobalUtil.getVideoUrlFromScene7(damVideoPath, dynamicMediaService);
        }
        if(StringUtils.isNotBlank(linkURL)) {
            linkType = LinkUtil.checkLinkType(linkURL);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkTextI18n() {
        return linkTextI18n;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public String getYoutubeVideoID() {
        return youtubeVideoID;
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

    public String getTextAlignment() {
        return textAlignment;
    }
    
    public Boolean isPackageDesign() {
        return packageDesign;
    }
    
    public String getSubTitle() {
        return subTitle;
    }

    public String getPwTheme() {
        return pwTheme;
    }

    public String getPwButtonTheme() {
        return pwButtonTheme;
    }
    
    public String getAnchorId() {
        return anchorId;
    }

    public String getAnchorTitle() {
        return anchorTitle;
    }
}
