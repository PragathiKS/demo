package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.beans.LeftNavigationBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class)
public class LeftNavigationModel {

    @Self
    private Resource resource;

    private String navHeading;

    private String closeBtnText;

    private List<LeftNavigationBean> leftNavItems = new ArrayList<>();

    private LeftNavigationBean stickyNavItem = new LeftNavigationBean();

    @PostConstruct
    protected void init() {
        Resource childResource = resource.getResourceResolver().getResource(
                CustomerHubConstants.GLOBAL_PAGE_PATH + "/jcr:content/root/responsivegrid");
        Resource globalConfigResource = getGlobalConfigurationResource(childResource);
        if (null != globalConfigResource) {
            ValueMap map = globalConfigResource.getValueMap();
            navHeading = (String) map.get("navHeadingI18n");
            closeBtnText = (String) map.get("closeBtnText");
            setStickyNavItemBean(map);
        }

        Resource globalResource = resource.getResourceResolver().getResource(CustomerHubConstants.GLOBAL_PAGE_PATH);
        Page globalPage = globalResource.adaptTo(Page.class);
        Iterator<Page> itr = globalPage.listChildren();
        while (itr.hasNext()) {
            Page childPage = itr.next();
            ValueMap valueMap = childPage.getContentResource().getValueMap();
            if (!isHiddenInNavigation(valueMap)) {
                LeftNavigationBean leftNavigationBean = getLeftNavigationBean(childPage, valueMap);
                leftNavItems.add(leftNavigationBean);
            }
        }
    }

    private void setStickyNavItemBean(ValueMap valueMap) {
        stickyNavItem.setPath((String) valueMap.get("stickyHref"));
        stickyNavItem.setIconClass((String) valueMap.get("stickyIconClass"));
        stickyNavItem.setExternalLink(true);
    }

    private LeftNavigationBean getLeftNavigationBean(Page childPage, ValueMap valueMap) {
        LeftNavigationBean bean = new LeftNavigationBean();
        bean.setIconClass((String) valueMap.get("iconClass"));
        bean.setExternalLink(isExternalLink(valueMap));
        bean.setIconLabel(getPageNameI18key(valueMap));
        bean.setPath(getResolvedPagePath(childPage));
        bean.setActive(isCurrentPage(childPage));
        return bean;
    }

    private boolean isCurrentPage(Page childPage) {
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if(null != pageManager) {
            Page currentPage = pageManager.getContainingPage(resource);
            return StringUtils.equalsIgnoreCase(childPage.getPath(), currentPage.getPath());
        }
        return false;
    }

    private String getResolvedPagePath(Page childPage) {
        return resource.getResourceResolver().map(childPage.getPath() + CustomerHubConstants.HTML_EXTENSION);
    }

    private String getPageNameI18key(ValueMap valueMap) {
        if (valueMap.containsKey("iconLabel")) {
            return (String) valueMap.get("iconLabel");
        }
        if (valueMap.containsKey("jcr:title")) {
            return (String) valueMap.get("jcr:title");
        }
        return "";
    }

    private boolean isHiddenInNavigation(ValueMap valueMap) {
        if (valueMap.containsKey(CustomerHubConstants.HIDE_IN_NAV_PROPERTY)) {
            return true;
        }
        return false;
    }

    private boolean isExternalLink(ValueMap valueMap) {
        if (valueMap.containsKey(CustomerHubConstants.CQ_REDIRECT_PROPERTY)) {
            return true;
        }
        return false;
    }

    private Resource getGlobalConfigurationResource(Resource childResource) {
        if (null != childResource) {
            Resource res = childResource.getChild("globalconfiguration");
            if (null != res) {
                return res;
            } else {
                Iterator<Resource> itr = childResource.listChildren();
                while (itr.hasNext()) {
                    Resource nextResource = itr.next();
                    if (nextResource.isResourceType(CustomerHubConstants.GLOBAL_CONFIGURATION_RESOURCE_TYPE)) {
                        return nextResource;
                    }
                }
            }
        }
        return null;
    }

    public List<LeftNavigationBean> getLeftNavItems() {
        return leftNavItems;
    }

    public String getNavHeading() {
        return navHeading;
    }

    public String getCloseBtnText() {
        return closeBtnText;
    }
}
