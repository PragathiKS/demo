package com.tetrapak.supplierportal.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LeftNavigationBean {

    private String href;

    @ValueMapValue
    private String iconLabel;

    @ValueMapValue
    private String iconClass;

    @ValueMapValue
    private boolean isExternalLink;

    private boolean isActive;

    private boolean isExpanded;

    private List<LeftNavigationBean> subMenuList;

    public String getIconLabel() {
        return iconLabel;
    }

    public String getIconClass() {
        return iconClass;
    }

    public boolean isExternalLink() {
        return isExternalLink;
    }

    public void setSubMenuList(List<LeftNavigationBean> subMenuList) {
        this.subMenuList = subMenuList;
    }

    public List<LeftNavigationBean> getSubMenuList() {
        return subMenuList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setIconLabel(String iconLabel) {
        this.iconLabel = iconLabel;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public void setExternalLink(boolean externalLink) {
        isExternalLink = externalLink;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
