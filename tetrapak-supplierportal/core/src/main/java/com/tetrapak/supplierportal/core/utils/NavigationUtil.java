package com.tetrapak.supplierportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.models.LeftNavigationBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Class NavigationUtil.
 */
public final class NavigationUtil {

    private static final String ICON_LABEL_PROPERTY = "iconLabel";
    private static final String IS_EXTERNAL_LINK_PROPERTY = "isExternal";
    private static final String HIDE_IN_NAV_PROPERTY = "hideInNav";
    private static final String ICON_CLASS_PROPERTY = "iconClass";
    private static final String TAB_NAME_PROPERTY = "tabName";
    private static final String REMOVE_NO_OPENER_NO_REFERRER_PROPERTY = "removeNoOpenerNoReferrer";

    /**
     * Instantiates a new navigation util.
     */
    private NavigationUtil() {
        // adding a private constructor to hide the public implicit one
    }

    /**
     * Fetch page base on availability in below order:
     * - i18n key if exist for the page
     * - navigation title if exist
     * - page title if all other are empy
     *
     * @param page the page
     * @return the navigation title
     */
    public static String getPageTitle(final Page page) {
        final ValueMap valueMap = page.getContentResource().getValueMap();
        String title = getPageNameI18key(valueMap);
        if (StringUtils.isEmpty(title)) {
            title = page.getTitle();
        }
        return title;
    }

    /**
     * Fetch navigation title i18n key of a page. If not present sends page Title as fallback
     *
     * @param valueMap the valueMap of a page
     * @return the navigation kay or title
     */
    private static String getPageNameI18key(ValueMap valueMap) {
        if (valueMap.containsKey(ICON_LABEL_PROPERTY)) {
            return (String) valueMap.get(ICON_LABEL_PROPERTY);
        }
        return "";
    }

    public static void populateLeftNavItems(ResourceResolver resolver, Resource resource,
            List<LeftNavigationBean> leftNavItems, Page childPage) {
        if (null != childPage && null != childPage.getContentResource()) {
            ValueMap valueMap = childPage.getContentResource().getValueMap();
            if (isNotHiddenInNavigation(valueMap)) {
                LeftNavigationBean leftNavigationBean = getLeftNavigationBean(resolver, resource, childPage, valueMap);
                if (childPage.listChildren(new PageFilter()).hasNext()) {
                    setChildPages(resolver, resource, childPage, leftNavigationBean);
                }
                leftNavItems.add(leftNavigationBean);
            }
        }
    }

    private static void setChildPages(ResourceResolver resolver, Resource resource, Page childPage,
            LeftNavigationBean leftNavigationBean) {
        Iterator<Page> itr = childPage.listChildren(new PageFilter());
        while (itr.hasNext()) {
            Page subPage = itr.next();
            if (PageUtil.isCurrentPage(subPage, resource) || isChildPageActive(subPage, resource)) {
                leftNavigationBean.setActive(true);
                leftNavigationBean.setExpanded(true);
            }

            ValueMap vMap = subPage.getContentResource().getValueMap();
            if (isNotHiddenInNavigation(vMap)) {
                LeftNavigationBean leftNavigationChildBean = getLeftNavigationBean(resolver, resource, subPage, vMap);
                if (null == leftNavigationBean.getSubMenuList()) {
                    leftNavigationBean.setSubMenuList(new ArrayList<>());
                }
                leftNavigationBean.getSubMenuList().add(leftNavigationChildBean);
            }
        }
    }

    private static boolean isChildPageActive(Page subPage, Resource resource) {
        boolean flag = false;

        if (subPage.listChildren(new PageFilter()).hasNext()) {
            Iterator<Page> itr = subPage.listChildren(new PageFilter());
            while (itr.hasNext()) {
                Page childPage = itr.next();
                if (PageUtil.isCurrentPage(childPage, resource)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    private static LeftNavigationBean getLeftNavigationBean(ResourceResolver resolver, Resource resource,
            Page childPage, ValueMap valueMap) {
        LeftNavigationBean bean = new LeftNavigationBean();
        bean.setIconClass((String) valueMap.get(ICON_CLASS_PROPERTY));
        bean.setExternalLink(isExternalLink(valueMap));
        bean.setIconLabel(NavigationUtil.getPageTitle(childPage));
        bean.setHref(getResolvedPagePath(resolver, childPage));
        bean.setActive(PageUtil.isCurrentPage(childPage, resource) || isChildPageActive(childPage, resource));
        bean.setPageName((String) valueMap.get(TAB_NAME_PROPERTY));
        if (valueMap.containsKey(REMOVE_NO_OPENER_NO_REFERRER_PROPERTY)) {
            bean.setRemoveNoOpenerNoReferrer(true);
        }
        return bean;
    }

    private static String getResolvedPagePath(ResourceResolver resolver, Page childPage) {
        return resolver.map(childPage.getPath() + SupplierPortalConstants.HTML_EXTENSION);
    }

    private static boolean isNotHiddenInNavigation(ValueMap valueMap) {
        return !valueMap.containsKey(HIDE_IN_NAV_PROPERTY);
    }

    private static boolean isExternalLink(ValueMap valueMap) {
        return valueMap.containsKey(IS_EXTERNAL_LINK_PROPERTY);
    }
}
