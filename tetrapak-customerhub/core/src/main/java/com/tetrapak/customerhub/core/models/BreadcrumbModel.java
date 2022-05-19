package com.tetrapak.customerhub.core.models;

import com.day.cq.wcm.api.Page;
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

/**
 * The Class BreadcrumbModel.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BreadcrumbModel {

    /** The request. */
    @SlingObject
    private SlingHttpServletRequest request;

    /** The current page. */
    @ScriptVariable
    private Page currentPage;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbModel.class);

    /** The breadcrumb subpages. */
    private final Map<String, String> breadcrumbSubpages = new LinkedHashMap<>();

    @OSGiService
    private UserPreferenceService userPreferenceService;

    private String selectedLanguage;

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        selectedLanguage = GlobalUtil.getSelectedLanguage(request, userPreferenceService);
        final Map<String, String> breadcrumbPages = new LinkedHashMap<>();
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = currentPage.getPath().replace(rootPath + "/", StringUtils.EMPTY);
        final String[] pages = path.split("/");
        final int length = pages.length;

        Page nextPage = currentPage;
        for (int i = 0; i <= length; i++) {
            if (Objects.nonNull(nextPage) && !nextPage.getPath().equalsIgnoreCase(rootPath) && !nextPage.isHideInNav()
                    && nextPage.getContentResource() != null) {

                if (nextPage.getContentResource().getValueMap().containsKey("disableClickInNavigation")) {
                    breadcrumbPages.put(NavigationUtil.getPageTitle(nextPage), null);
                } else {
                    breadcrumbPages.put(NavigationUtil.getPageTitle(nextPage),
                            LinkUtils.sanitizeLink(nextPage.getPath(), request));
                }
                nextPage = nextPage.getParent();
            }
        }
        final List<String> alKeys = new ArrayList<>(breadcrumbPages.keySet());
        Collections.reverse(alKeys);
        for (final String key : alKeys) {
            breadcrumbSubpages.put(key, breadcrumbPages.get(key));
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
     * @return current Page Parent index
     */
    public int getCurrentPageParentIndex() {
        return breadcrumbSubpages.size()-1;
    }

    public String getLocale() {
        return org.apache.commons.lang.StringUtils.isEmpty(selectedLanguage) ? CustomerHubConstants.DEFAULT_LOCALE : selectedLanguage;
    }
}
