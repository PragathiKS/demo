package com.tetrapak.publicweb.core.models;

import java.util.HashMap;
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
import com.tetrapak.publicweb.core.utils.LinkUtils;

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
    private Map<String, String> breadcrumbSubpages = new HashMap<>();

    /** The home label. */
    private String homeLabel = "Home";

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        LOGGER.debug("Inside init method");
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        homePagePath = LinkUtils.sanitizeLink(rootPath + "/home");
        final String path = currentPage.getPath().replace(rootPath + "/", StringUtils.EMPTY);
        final String[] pages = path.split("/");
        final int length = pages.length - 1;
        Page parent = currentPage.getParent();
        breadcrumbSubpages.put(currentPage.getTitle(), currentPage.getPath());
        for (int i = 0; i <= length; i++) {
            if (Objects.nonNull(parent) && !parent.getPath().equalsIgnoreCase(rootPath) && !parent.isHideInNav()) {
                breadcrumbSubpages.put(parent.getTitle(), LinkUtils.sanitizeLink(parent.getPath()));
                parent = parent.getParent();
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
        return LinkUtils.sanitizeLink(homePagePath);
    }

    /**
     * Gets the home label.
     *
     * @return the home label
     */
    public String getHomeLabel() {
        return homeLabel;
    }

}
