package com.tetrapak.publicweb.core.beans;

public class NavigationLinkBean {

    private String linkTextI18n;

    private String linkTooltipI18n;

    private String linkPath;

    private String linkType;

    private String targetBlank;

    public String getLinkTextI18n() {
        return linkTextI18n;
    }

    public void setLinkTextI18n(String linkTextI18n) {
        this.linkTextI18n = linkTextI18n;
    }

    public String getLinkTooltipI18n() {
        return linkTooltipI18n;
    }

    public void setLinkTooltipI18n(String linkTooltipI18n) {
        this.linkTooltipI18n = linkTooltipI18n;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getTargetBlank() {
        return targetBlank;
    }

    public void setTargetBlank(String targetBlank) {
        this.targetBlank = targetBlank;
    }

}
