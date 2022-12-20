package com.tetrapak.supplierportal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import com.tetrapak.supplierportal.core.utils.PageUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LeftNavigationModel {

    private static final String CONFIGURATION_PATH = "/jcr:content/root/responsivegrid/leftnavigation";

    @SlingObject private SlingHttpServletRequest request;

    //    @OSGiService private UserPreferenceService userPreferenceService;

    //    private String navHeading;
    //
    //    private String closeBtnText;

    private List<LeftNavigationBean> leftNavItems = new ArrayList<>();

        private static final String IS_EXTERNAL_LINK_PROPERTY = "isExternal";
        private static final String HIDE_IN_NAV_PROPERTY = "hideInNav";

    private String selectedLanguage;

    @PostConstruct protected void init() {
        //        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        //        Resource globalConfigResource = GlobalUtil.getGlobalConfigurationResource(request);

        Resource resource = request.getResourceResolver()
                .getResource(SupplierPortalConstants.CONTENT_ROOT + CONFIGURATION_PATH);

        if (resource != null) {
            //            ValueMap map = globalConfigResource.getValueMap();
            //            navHeading = (String) map.get("navHeadingI18n");
            //            closeBtnText = (String) map.get("closeBtnText");

            PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);

            Page globalPage = pageManager.getContainingPage(resource);
            if (null != globalPage) {
                Iterator<Page> itr = globalPage.listChildren();
                while (itr.hasNext()) {
                    Page childPage = itr.next();
                    populateLeftNavItems(childPage);
                }
            }
        }
    }

    private void populateLeftNavItems(Page childPage) {
        if (null != childPage && null != childPage.getContentResource()) {
            ValueMap valueMap = childPage.getContentResource().getValueMap();
            if (isNotHiddenInNavigation(valueMap)) {
                LeftNavigationBean leftNavigationBean = getLeftNavigationBean(childPage, valueMap);
                if (childPage.listChildren(new PageFilter()).hasNext()) {
                    setChildPages(childPage, leftNavigationBean);
                }
                leftNavItems.add(leftNavigationBean);
            }
        }
    }

    private void setChildPages(Page childPage, LeftNavigationBean leftNavigationBean) {
        Iterator<Page> itr = childPage.listChildren(new PageFilter());
        while (itr.hasNext()) {
            Page subPage = itr.next();
            if (PageUtil.isCurrentPage(subPage, request.getResource()) || isChildPageActive(subPage,
                    request.getResource())) {
                leftNavigationBean.setActive(true);
                leftNavigationBean.setExpanded(true);
            }

            ValueMap vMap = subPage.getContentResource().getValueMap();
            if (isNotHiddenInNavigation(vMap)) {
                LeftNavigationBean leftNavigationChildBean = getLeftNavigationBean(subPage, vMap);
                if (null == leftNavigationBean.getSubMenuList()) {
                    leftNavigationBean.setSubMenuList(new ArrayList<LeftNavigationBean>() {
                        private static final long serialVersionUID = -2716037396713449132L;
                    });
                }
                leftNavigationBean.getSubMenuList().add(leftNavigationChildBean);
            }
        }
    }

    private boolean isChildPageActive(Page subPage, Resource resource) {
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

    private LeftNavigationBean getLeftNavigationBean(Page childPage, ValueMap valueMap) {
        LeftNavigationBean bean = new LeftNavigationBean();
        bean.setIconClass((String) valueMap.get("iconClass"));
        bean.setExternalLink(isExternalLink(valueMap));
        bean.setIconLabel(getPageTitle(childPage));
        bean.setHref(getResolvedPagePath(childPage));
        bean.setActive(PageUtil.isCurrentPage(childPage, request.getResource()) || isChildPageActive(childPage,
                request.getResource()));
//        bean.setPageName((String) valueMap.get("tabName"));
//        if (valueMap.containsKey("removeNoOpenerNoReferrer")) {
//            bean.setRemoveNoOpenerNoReferrer(true);
//        }
        return bean;
    }

    public static String getPageTitle(final Page page) {
        final ValueMap valueMap = page.getContentResource().getValueMap();
        String title = getPageNameI18key(valueMap);
        if(org.apache.commons.lang3.StringUtils.isEmpty(title)) {
            title = page.getTitle();
        }
        return title;
    }

    /**
     * Fetch navigation title i18n key of a page. If not present sends page Title as fallback
     *
     * @param valueMap
     *            the valueMap of a page
     * @return the navigation kay or title
     */
    private static String getPageNameI18key(ValueMap valueMap) {
        if (valueMap.containsKey("iconLabel")) {
            return (String) valueMap.get("iconLabel");
        }
        return "";
    }

    private String getResolvedPagePath(Page childPage) {
        return request.getResourceResolver().map(childPage.getPath() + SupplierPortalConstants.HTML_EXTENSION);
    }

    private boolean isNotHiddenInNavigation(ValueMap valueMap) {
        return !valueMap.containsKey(HIDE_IN_NAV_PROPERTY);
    }

    private boolean isExternalLink(ValueMap valueMap) {
        return valueMap.containsKey(IS_EXTERNAL_LINK_PROPERTY);
    }

    public List<LeftNavigationBean> getLeftNavItems() {
        return new ArrayList<>(this.leftNavItems);
    }

//    public String getNavHeading() {
//        return navHeading;
//    }
//
//    public String getCloseBtnText() {
//        return closeBtnText;
//    }

//    public String getLocale() {
//        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
//    }
}
