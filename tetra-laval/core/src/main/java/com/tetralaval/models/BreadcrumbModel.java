package com.tetralaval.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.day.cq.wcm.api.WCMMode;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.utils.LinkUtils;
import com.tetralaval.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.tetralaval.utils.NavigationUtil;

/**
 * The Class BreadcrumbModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadcrumbModel {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbModel.class);

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The home page path. */
    private String homePagePath;

    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The breadcrumb subpages. */
    private final Map<String, String> breadcrumbSubpages = new LinkedHashMap<>();

    private boolean exist;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Inside init method");
        Resource resource = request.getResource();
        ResourceResolver resourceResolver = resource.getResourceResolver();

        final Map<String, String> breadcrumbPages = new LinkedHashMap<>();
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo()).replace(TLConstants.HTML_EXTENSION, StringUtils.EMPTY);
        homePagePath = LinkUtils.sanitizeLink(rootPath, request);
        final String path = currentPage.getPath().replace(rootPath, StringUtils.EMPTY);
        final List<String> pages = Arrays.stream(path.split(TLConstants.SLASH))
                .filter(s -> !StringUtils.EMPTY.equals(s)).collect(Collectors.toList());

        Page languagePage = PageUtil.getLanguagePage(resourceResolver.resolve(rootPath));
        if (languagePage != null) {
            breadcrumbPages.put(NavigationUtil.getNavigationTitle(languagePage), languagePage.getPath());

            String currentPagePath = rootPath;
            for (int i = 0; i < pages.size(); i++) {
                currentPagePath = new StringBuilder(currentPagePath).append(TLConstants.SLASH).append(pages.get(i)).toString();
                Page page = resourceResolver.resolve(currentPagePath).adaptTo(Page.class);
                breadcrumbPages.put(NavigationUtil.getNavigationTitle(page), page.getPath());
            }

            final List<String> alKeys = new ArrayList<>(breadcrumbPages.keySet());
            for (final String key : alKeys) {
                breadcrumbSubpages.put(key, LinkUtils.sanitizeLink(breadcrumbPages.get(key), request));
            }
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

    public boolean isExist() {
        WCMMode currentMode = WCMMode.fromRequest(request);
        return breadcrumbSubpages != null && (WCMMode.EDIT == currentMode ||
                (WCMMode.EDIT != currentMode && breadcrumbSubpages.size() > 1));
    }
}
