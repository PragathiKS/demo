package com.tetrapak.publicweb.core.models;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class)
public class BreadcrumbModel {

    @Self
    private Resource resource;

    private String homePagePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbModel.class);

    private static final String SUBPAGE_TITLE_I18N = "subpageTitleI18n";
    private static final String SUBPAGE_LINK_PATH = "subpageLinkPath";

    private List<Map<String, String>> breadcrumbSubpages = new ArrayList<>();
    private String breadcrumbHomeLabelI18n = "Home";

    private PageManager pageManager;

    @PostConstruct
    protected void init() {
        pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(resource);
        LOGGER.info("Current Page path : {}", currentPage.getPath());
        if (currentPage != null) {
            buildBreadcrumbItems(currentPage);
        }
    }

    private void buildBreadcrumbItems(Page currentPage) {
        InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
        homePagePath = inheritanceValueMap1.getInherited("homePagePath", String.class);
        Page homePage = pageManager.getPage(homePagePath);

        if (homePage != null) {
            LOGGER.info("Home Page path : {}", homePage.getPath());
            int pageLevel = homePage.getDepth();
            int currentPageLevel = currentPage.getDepth();
            while (pageLevel < currentPageLevel) {
                Page page = currentPage.getAbsoluteParent(pageLevel);
                if (page == null) {
                    break;
                }
                pageLevel++;
                if (!page.isHideInNav()) {
                    addBreadCrumbItem(page);
                }
            }
        }
    }

    private void addBreadCrumbItem(Page page) {
        Map<String, String> breadcrumbItem = new HashMap<>();
        String pageNavigationTitle;
        if (StringUtils.isNotBlank(page.getNavigationTitle())) {
            pageNavigationTitle = page.getNavigationTitle();
        } else {
            pageNavigationTitle = page.getTitle();
        }
        breadcrumbItem.put(SUBPAGE_TITLE_I18N, pageNavigationTitle);
        breadcrumbItem.put(SUBPAGE_LINK_PATH, LinkUtils.sanitizeLink(page.getPath()));

        breadcrumbSubpages.add(breadcrumbItem);
    }

    public List<Map<String, String>> getBreadcrumbSubpages() {
        return breadcrumbSubpages;
    }

    public String getBreadcrumbHomeLabelI18n() {
        return breadcrumbHomeLabelI18n;
    }

    public String getBreadcrumbHomePath() {
        return LinkUtils.sanitizeLink(homePagePath);
    }

}
