package com.tetrapak.publicweb.core.beans;

public class TeaserBean {
    private String title;
    private String description;
    private String imagePath;
    private String altText;
    private String linkText;
    private String linkPath;
    private String targetNew;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    public String getTargetNew() {
        return targetNew;
    }

    public void setTargetNew(String targetNew) {
        this.targetNew = targetNew;
    }
}
