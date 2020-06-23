package com.tetrapak.publicweb.core.utils;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.HeaderConfigurationModel;

/**
 * The Class NavigationUtil.
 */
public final class NavigationUtil {

    private NavigationUtil() {
        // adding a private constructor to hide the public implicit one
    }

    /**
     * Fetch solution page path.
     *
     * @param request the request
     * @return the string
     */
    public static String fetchSolutionPagePath(final SlingHttpServletRequest request) {
        String solutionPagePath = StringUtils.EMPTY;
        final String rootPath = LinkUtils.getRootPath(request.getPathInfo());
        final String path = rootPath + "/jcr:content/root/responsivegrid/headerconfiguration";
        final Resource headerConfigurationResource = request.getResourceResolver().getResource(path);
        if (Objects.nonNull(headerConfigurationResource)) {
            final HeaderConfigurationModel configurationModel = headerConfigurationResource
                    .adaptTo(HeaderConfigurationModel.class);
            if (Objects.nonNull(configurationModel)) {
                solutionPagePath = configurationModel.getSolutionPage();
            }
        }
        return solutionPagePath;
    }

    /**
     * Gets the solution page title.
     *
     * @param request the request
     * @param solutionPage the solution page
     * @return the solution page title
     */
    public static String getSolutionPageTitle(final SlingHttpServletRequest request, final String solutionPage) {
        String solutionPageTitle = StringUtils.EMPTY;
        final String solutionPageJcrContentPath = getSolutionPageWithoutExtension(solutionPage) + PWConstants.SLASH
                + JcrConstants.JCR_CONTENT;
        final Resource solutionPageResource = request.getResourceResolver().getResource(solutionPageJcrContentPath);
        if (Objects.nonNull(solutionPageResource)) {
            final ValueMap properties = solutionPageResource.adaptTo(ValueMap.class);
            solutionPageTitle = properties.get(JcrConstants.JCR_TITLE, StringUtils.EMPTY);
        }
        return solutionPageTitle;
    }

    /**
     * Gets the solution page without extension.
     *
     * @return the solution page without extension
     */
    public static String getSolutionPageWithoutExtension(final String path) {
        return StringUtils.substringBefore(path, ".");
    }


    /**
     * Fetch navigation title of page, if not present sends page Title as fallback
     *
     * @param page
     *            the page
     * @return the navigation title
     */
    public static String getNavigationTitle(final Page page) {
        String title = page.getNavigationTitle();
        if (StringUtils.isEmpty(title)) {
            title = page.getTitle();
        }
        return title;
    }

}
