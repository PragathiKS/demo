package com.tetrapak.publicweb.core.models;

import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageTextBannerModel {

    @Self
    private Resource resource;

    @Inject
    private String bannerSubtitleI18n;

    @Inject
    private String titleI18n;

    @Inject
    private String desktopImage;

    @Inject
    private String desktopImageAltI18n;

    @Inject
    private String mobileImage;

    @Inject
    private String mobileCroppingOption;

    @Inject
    private String bannerDescriptionI18n;

    @Inject
    private String contentAlignment;

    @Inject
    private String contrastLayer;

    @Inject
    private String bannerCtaTextI18n;

    @Inject
    private String bannerCtaPath;

    @Inject
    private String targetBlank;

    @Inject
    private String bannerCtaTooltipI18n;

    @Inject
    private String targetSoftConversion;

    @Inject
    private String softConversionTitle;

    @Inject
    private String softConversionDescription;

    @Inject
    private String softConversionHeadline;

    @Inject
    private String softConversionLastStep;

    @Inject
    private String softConversionDocPath;

    private Boolean isHeaderBanner = false;

    private String analyticsLinkSection;

    @PostConstruct
    protected void init() {

        String parentName = resource.getParent() != null ? resource.getParent().getName() : StringUtils.EMPTY;
        if ("header".equalsIgnoreCase(parentName)) {
            isHeaderBanner = true;
            analyticsLinkSection = "heroBanner";
        } else if ("pw-banner".equals(contentAlignment)) {
            analyticsLinkSection = "imageTextBannerLeft";
        } else {
            analyticsLinkSection = "imageTextBannerRight";
        }
    }

    public String getBannerSubtitleI18n() {
        return bannerSubtitleI18n;
    }

    public String getTitleI18n() {
        return titleI18n;
    }

    public String getDesktopImage() {
        return desktopImage;
    }

    public String getDesktopImageAltI18n() {
        return desktopImageAltI18n;
    }

    public String getMobileImage() {
        return mobileImage;
    }

    public String getMobileCroppingOption() {
        return mobileCroppingOption;
    }

    public String getBannerDescriptionI18n() {
        return bannerDescriptionI18n;
    }

    public String getContentAlignment() {
        return contentAlignment;
    }

    public String getAnalyticsLinkSection() {
        return analyticsLinkSection;
    }

    public String getContrastLayer() {
        return contrastLayer;
    }

    public String getBannerCtaTextI18n() {
        return bannerCtaTextI18n;
    }

    public String getBannerCtaPath() {
        return LinkUtils.sanitizeLink(bannerCtaPath);
    }

    public String getLinkType() {
        return LinkUtils.linkType(bannerCtaPath);
    }

    public String getTargetBlank() {
        return targetBlank;
    }

    public String getBannerCtaTooltipI18n() {
        return bannerCtaTooltipI18n;
    }

    public Boolean getIsHeaderBanner() {
        return isHeaderBanner;
    }

    public String getTargetSoftConversion() {
        return targetSoftConversion;
    }

    public String getSoftConversionTitle() {
        return softConversionTitle;
    }

    public String getSoftConversionDescription() {
        return softConversionDescription;
    }

    public String getSoftConversionHeadline() {
        return softConversionHeadline;
    }

    public String getSoftConversionLastStep() {
        return softConversionLastStep;
    }

    public String getSoftConversionDocPath() {
        return softConversionDocPath;
    }

}
