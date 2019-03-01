package com.tetrapak.customerhub.core.beans;

public class LeftNavigationBean {
    private String path;
    private boolean isHiddenInNavigation;
    private boolean isIconAvailable;
    private String iconClass;
    private String iconLabel;
    private boolean isExternalLink;
    private boolean isSubMenuAvailable;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHiddenInNavigation() {
        return isHiddenInNavigation;
    }

    public void setHiddenInNavigation(boolean hiddenInNavigation) {
        isHiddenInNavigation = hiddenInNavigation;
    }

    public boolean isIconAvailable() {
        return isIconAvailable;
    }

    public void setIconAvailable(boolean iconAvailable) {
        isIconAvailable = iconAvailable;
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

    public boolean isSubMenuAvailable() {
        return isSubMenuAvailable;
    }

    public void setSubMenuAvailable(boolean subMenuAvailable) {
        isSubMenuAvailable = subMenuAvailable;
    }
}
