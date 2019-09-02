package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.beans.LeftNavigationBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.TabNamesMappingService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Model class for left navigation component
 *
 * @author Nitin Kumar
 */
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LeftNavigationModel {

    @SlingObject
    private SlingHttpServletRequest request;

    @OSGiService
    private UserPreferenceService userPreferenceService;

    @OSGiService
    private TabNamesMappingService tabNamesMappingService;

    private String navHeading;

    private String closeBtnText;

    private List<LeftNavigationBean> leftNavItems = new ArrayList<>();

    private static final String IS_EXTERNAL_LINK_PROPERTY = "isExternal";
    private static final String HIDE_IN_NAV_PROPERTY = "hideInNav";

    private String selectedLanguage;

    private Map<String, String> tabsMap = new HashMap<>();

    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        Resource globalConfigResource = GlobalUtil.getGlobalConfigurationResource(request);
        PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
        if (null != globalConfigResource && null != pageManager) {
            ValueMap map = globalConfigResource.getValueMap();
            navHeading = (String) map.get("navHeadingI18n");
            closeBtnText = (String) map.get("closeBtnText");

            Page globalPage = pageManager.getContainingPage(globalConfigResource);
            if (null != globalPage) {
                setMapFromConfiguration();
                Iterator<Page> itr = globalPage.listChildren();
                while (itr.hasNext()) {
                    Page childPage = itr.next();
                    populateLeftNavItems(childPage);
                }
            }
        }
    }

    private void setMapFromConfiguration() {
        if (null == tabNamesMappingService) {
            return;
        }
        String[] tabNamesMapArray = tabNamesMappingService.getTabNamesMap();
        for (String text : tabNamesMapArray) {
            tabsMap.put(StringUtils.substringBefore(text, "="),
                    StringUtils.substringAfter(text, "="));
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
            if (PageUtil.isCurrentPage(subPage, request.getResource()) || isChildPageActive(subPage, request.getResource())) {
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
        bean.setIconLabel(getPageNameI18key(valueMap));
        bean.setHref(getResolvedPagePath(childPage));
        bean.setActive(PageUtil.isCurrentPage(childPage, request.getResource()) || isChildPageActive(childPage, request.getResource()));
        bean.setPageName(getTabNameFromMappingConfiguration(childPage.getPath()));
        return bean;
    }

    private String getTabNameFromMappingConfiguration(String path) {
        if (tabsMap.containsKey(path)) {
            return tabsMap.get(path);
        }
        return StringUtils.EMPTY;
    }

    private String getResolvedPagePath(Page childPage) {
        return request.getResourceResolver().map(childPage.getPath() + CustomerHubConstants.HTML_EXTENSION);
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

    private boolean isNotHiddenInNavigation(ValueMap valueMap) {
        return !valueMap.containsKey(HIDE_IN_NAV_PROPERTY);
    }

    private boolean isExternalLink(ValueMap valueMap) {
        return valueMap.containsKey(IS_EXTERNAL_LINK_PROPERTY);
    }

    public List<LeftNavigationBean> getLeftNavItems() {
        return new ArrayList<>(this.leftNavItems);
    }

    public String getNavHeading() {
        return navHeading;
    }

    public String getCloseBtnText() {
        return closeBtnText;
    }

    public String getLocale() {
        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }
}
