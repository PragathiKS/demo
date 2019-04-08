package com.tetrapak.customerhub.core.beans;

/**
 * The GetStartedBean class
 */
public class GetStartedBean {
    private String imagePath;

    private String imageAltI18n;

    private String titleI18n;

    private String descriptionI18n;

    private String linkUrl;

    private String linkTextI18n;

    public String getImagePath() {

        return imagePath;
    }

    public void setImagePath(String imagePath) {

        this.imagePath = imagePath;
    }

    public String getImageAltI18n() {

        return imageAltI18n;
    }

    public void setImageAltI18n(String imageAltI18n) {

        this.imageAltI18n = imageAltI18n;
    }

    public String getTitleI18n() {

        return titleI18n;
    }

    public void setTitleI18n(String titleI18n) {

        this.titleI18n = titleI18n;
    }

    public String getDescriptionI18n() {

        return descriptionI18n;
    }

    public void setDescriptionI18n(String descriptionI18n) {

        this.descriptionI18n = descriptionI18n;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTextI18n() {
        return linkTextI18n;
    }

    public void setLinkTextI18n(String linkTextI18n) {
        this.linkTextI18n = linkTextI18n;
    }
}
