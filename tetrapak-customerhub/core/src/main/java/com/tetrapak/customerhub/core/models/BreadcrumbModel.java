package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.LinkUtils;
import com.tetrapak.customerhub.core.utils.NavigationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The Class BreadcrumbModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadcrumbModel {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbModel.class);

    /**
     * Hide breacrumbs property.
     */
    private static final String HIDE_BREADCRUMBS_PROPERTY = "hideBreadcrumbs";

    /**
     * Disable click in breadcrumbs property.
     */
    private static final String DISABLE_CLICK_BREADCRUMBS_PROPERTY = "disableClickInBreadcrumbs";

    /**
     * The breadcrumb subpages.
     */
    private final Map<String, String> breadcrumbSubpages = new LinkedHashMap<>();

    /**
     * The request.
     */
    @SlingObject
    private SlingHttpServletRequest request;

    /**
     * The current page.
     */
    @ScriptVariable
    private Page currentPage;
    
    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String selectedLanguage;

    private String homePagePath;

    private String homePageTitle;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        final Map<String, String> breadcrumbPages = new LinkedHashMap<>();
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        initHomePage(rootPath);

        if (Boolean.TRUE != currentPage.getProperties().get(HIDE_BREADCRUMBS_PROPERTY, Boolean.class)) {
            final String path = currentPage.getPath().replace(rootPath + "/", StringUtils.EMPTY);

            Page nextPage = currentPage;
            while (Objects.nonNull(nextPage)
                    && !nextPage.getPath().equalsIgnoreCase(rootPath)
                    && !nextPage.isHideInNav()
                    && nextPage.getContentResource() != null) {
                addToBreadcrumbs(breadcrumbPages, nextPage);
                nextPage = nextPage.getParent();
            }

            final List<String> alKeys = new ArrayList<>(breadcrumbPages.keySet());
            Collections.reverse(alKeys);
            for (final String key : alKeys) {
                breadcrumbSubpages.put(key, breadcrumbPages.get(key));
            }
        }
    }

    /**
     * Init home page path and title
     *
     * @param rootPath
     */
    private void initHomePage(String rootPath) {
        homePagePath = rootPath + "/" + CustomerHubConstants.HOME_PAGE_REL_PATH;
        Optional.ofNullable(request.getResourceResolver())
                .map(r -> r.adaptTo(PageManager.class))
                .map(p -> p.getPage(homePagePath))
                .ifPresent(page -> homePageTitle = NavigationUtil.getPageTitle(page));
    }

    /**
     * Add applicable page to breadcrumbs list
     *
     * @param breadcrumbPages
     * @param nextPage
     */
    private void addToBreadcrumbs(Map<String, String> breadcrumbPages, Page nextPage) {
        if (Boolean.TRUE == nextPage.getContentResource().getValueMap()
                .get(DISABLE_CLICK_BREADCRUMBS_PROPERTY, Boolean.class)) {
            breadcrumbPages.put(NavigationUtil.getPageTitle(nextPage), null);
        } else {
            breadcrumbPages
                    .put(NavigationUtil.getPageTitle(nextPage), LinkUtils.sanitizeLink(nextPage.getPath(), request));
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
     * Gets the  current Page Parent index
     *
     * @return current Page Parent index
     */
    public int getCurrentPageParentIndex() {
        return breadcrumbSubpages.size() - 1;
    }

    public String getLocale() {
        return StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }

    public String getHomePagePath() {
        return LinkUtils.sanitizeLink(homePagePath, request);
    }

    public String getHomePageTitle() {
        return homePageTitle;
    }
}
