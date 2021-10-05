package com.tetrapak.publicweb.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.utils.LinkUtils;
import com.tetrapak.publicweb.core.utils.NavigationUtil;

/**
 * The Class BreadcrumbModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadcrumbModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The home page path. */
    private String homePagePath;
    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbModel.class);

    /** The breadcrumb subpages. */
    private final Map<String, String> breadcrumbSubpages = new LinkedHashMap<>();

    /** The Current Page Parent Index. */
    private int currentPageParentIndex;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Inside init method");
        final Map<String, String> breadcrumbPages = new LinkedHashMap<>();
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        homePagePath = LinkUtils.sanitizeLink(rootPath + PWConstants.SLASH + PWConstants.HOME_PAGE_REL_PATH, request);
        final String path = currentPage.getPath().replace(rootPath + "/", StringUtils.EMPTY);
        final String[] pages = path.split("/");
        final int length = pages.length - 1;
        Page parent = currentPage.getParent();
        final String title = NavigationUtil.getNavigationTitle(currentPage);
        int activePageHierarchyIndex = 0;

        breadcrumbPages.put(title, currentPage.getPath());
        for (int i = 0; i <= length; i++) {
            if (Objects.nonNull(parent) && !parent.getPath().equalsIgnoreCase(rootPath) && !parent.isHideInNav() && parent.getContentResource()!=null) {

                if (parent.getContentResource().getValueMap().containsKey("disableClickInNavigation")) {
                    breadcrumbPages.put(NavigationUtil.getNavigationTitle(parent), null);
                } else {
                    activePageHierarchyIndex++;
                    checkAndSetCurrentParentPageIndex(activePageHierarchyIndex,i);
                    breadcrumbPages.put(NavigationUtil.getNavigationTitle(parent),
                            LinkUtils.sanitizeLink(parent.getPath(), request));
                }

                parent = parent.getParent();
            }
        }
        final List<String> alKeys = new ArrayList<String>(breadcrumbPages.keySet());
        Collections.reverse(alKeys);
        for (final String key : alKeys) {
            breadcrumbSubpages.put(key, breadcrumbPages.get(key));
        }
    }

    private void checkAndSetCurrentParentPageIndex(int activePageHierarchyIndex, int i) {
        if(activePageHierarchyIndex == 1) {
            currentPageParentIndex = i + 1;
        }
    }

    /**
     * Gets the breadcrumb subpages.
     *
     * @return the breadcrumb subpages
     */
    public Map<String, String> getBreadcrumbSubpages() {
        return breadcrumbSubpages;
    }

    /**
     * Gets the home page path.
     *
     * @return the home page path
     */
    public String getHomePagePath() {
        return LinkUtils.sanitizeLink(homePagePath, request);
    }

    /**
     * Gets the  current Page Parent index
     * @return current Page Parent index
     */
    public int getCurrentPageParentIndex() {
        return (breadcrumbSubpages.size()-currentPageParentIndex);
    }
}
