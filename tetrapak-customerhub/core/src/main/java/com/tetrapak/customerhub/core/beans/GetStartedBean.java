package com.tetrapak.customerhub.core.beans;

/**
 * Get Started Bean class
 */
public class GetStartedBean {
    private ImageBean image;

    private String titleI18n;

    private String descriptionI18n;

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
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
}
