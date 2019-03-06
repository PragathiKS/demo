package com.tetrapak.customerhub.core.beans;

public class LeftNavigationBean {
    private String path;
    private String iconClass;
    private String iconLabel;
    private boolean isExternalLink;
    private boolean isActive;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getIconLabel() {
        return iconLabel;
    }

    public void setIconLabel(String iconLabel) {
        this.iconLabel = iconLabel;
    }

    public boolean isExternalLink() {
        return isExternalLink;
    }

    public void setExternalLink(boolean externalLink) {
        isExternalLink = externalLink;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
