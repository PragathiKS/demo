package com.tetrapak.customerhub.core.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * The LeftNavigationBean Class
 */
public class LeftNavigationBean {
    private String href;
    private String iconClass;
    private String iconLabel;
    private boolean isExternalLink;
    private boolean isActive;
    private boolean isExpanded;
    private List<LeftNavigationBean> subMenuList;
    private String pageName;
    private boolean removeNoOpenerNoReferrer;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public List<LeftNavigationBean> getSubMenuList() {
        return subMenuList;
    }

    public void setSubMenuList(List<LeftNavigationBean> subMenuList) {
        this.subMenuList = new ArrayList<>(subMenuList);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isRemoveNoOpenerNoReferrer() {
        return removeNoOpenerNoReferrer;
    }

    public void setRemoveNoOpenerNoReferrer(boolean removeNoOpenerNoReferrer) {
        this.removeNoOpenerNoReferrer = removeNoOpenerNoReferrer;
    }
}
