package com.tetrapak.publicweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.tetrapak.publicweb.core.utils.LinkUtils;

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
    private String mobileImageAltI18n;

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

    private Boolean isHeaderBanner = false;

    @PostConstruct
    protected void init() {

        String parentName = resource.getParent().getName();
        if (parentName.equalsIgnoreCase("header")) {
            isHeaderBanner = true;
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

    public String getMobileImageAltI18n() {
        return mobileImageAltI18n;
    }

    public String getBannerDescriptionI18n() {
        return bannerDescriptionI18n;
    }

    public String getContentAlignment() {
        return contentAlignment;
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

}
