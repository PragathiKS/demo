package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.utils.LinkUtils;

@Model(adaptables = Resource.class)
public class BreadcrumbModel {

    @Self
    private Resource resource;

    private String homePagePath;

    private static final Logger log = LoggerFactory.getLogger(BreadcrumbModel.class);

    private static final String SUBPAGE_TITLE_I18N = "subpageTitleI18n";
    private static final String SUBPAGE_LINK_PATH = "subpageLinkPath";

    private List<Map<String, String>> breadcrumbSubpages = new ArrayList<Map<String, String>>();
    private String breadcrumbHomeLabelI18n = "Home";

    PageManager pageManager;

    @PostConstruct
    protected void init() {
        pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(resource);
        log.info("Current Page path : " + currentPage.getPath());
        if (currentPage != null) {
            buildBreadcrumbItems(currentPage);
        }
    }

    private void buildBreadcrumbItems(Page currentPage) {
        InheritanceValueMap inheritanceValueMap1 = new HierarchyNodeInheritanceValueMap(resource);
        homePagePath = inheritanceValueMap1.getInherited("homePagePath", String.class);
        Page homePage = pageManager.getPage(homePagePath);

        if (homePage != null) {
            log.info("Home Page path : " + homePage.getPath());
            int pageLevel = homePage.getDepth();
            int currentPageLevel = currentPage.getDepth();
            while (pageLevel < currentPageLevel) {
                Page page = currentPage.getAbsoluteParent((int) pageLevel);
                if (page == null) {
                    break;
                }
                pageLevel++;
                if (!page.isHideInNav()) {
                    Map<String, String> breadcrumbItem = new HashMap<String, String>();
                    String pageNavigationTitle = StringUtils.isNotBlank(page.getNavigationTitle()) ? page.getNavigationTitle() : page.getTitle();
                    breadcrumbItem.put(SUBPAGE_TITLE_I18N, pageNavigationTitle);
                    breadcrumbItem.put(SUBPAGE_LINK_PATH, LinkUtils.sanitizeLink(page.getPath()));

                    breadcrumbSubpages.add(breadcrumbItem);
                }
            }
        }
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
