package com.tetrapak.customerhub.core.beans;

/**
 * Bean class for tabs component's multi field
 *
 * @author Nitin Kumar
 */
public class TabBean {
    private String pageUrl;
    private boolean isActive;
    private String labelI18n;
    private String iconClass;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLabelI18n() {
        return labelI18n;
    }

    public void setLabelI18n(String labelI18n) {
        this.labelI18n = labelI18n;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}
