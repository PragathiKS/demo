package com.tetrapak.publicweb.core.utils;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.models.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.models.MegaMenuConfigurationModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.Objects;

/**
 * The Class NavigationUtil.
 */
public final class NavigationUtil {

    /**
     * Instantiates a new navigation util.
     */
    private NavigationUtil() {
        // adding a private constructor to hide the public implicit one
    }

    /**
     * Fetch solution page path.
     *
     * @param request
     *            the request
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
     * @param request
     *            the request
     * @param solutionPage
     *            the solution page
     * @return the solution page title
     */
    public static String getSolutionPageTitle(final SlingHttpServletRequest request, final String solutionPage) {
        String solutionPageTitle = StringUtils.EMPTY;
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final String solutionPagePath = resourceResolver.resolve(solutionPage).getPath();
        final String solutionPageJcrContentPath = getSolutionPageWithoutExtension(solutionPagePath) + PWConstants.SLASH
                + JcrConstants.JCR_CONTENT;
        final Resource solutionPageResource = resourceResolver.getResource(solutionPageJcrContentPath);
        if (Objects.nonNull(solutionPageResource)) {
            final ValueMap properties = solutionPageResource.adaptTo(ValueMap.class);
            final String navTitle = properties.get(NameConstants.PN_NAV_TITLE, StringUtils.EMPTY);
            if (StringUtils.isNotBlank(navTitle)) {
                solutionPageTitle = navTitle;
            } else {
                solutionPageTitle = properties.get(JcrConstants.JCR_TITLE, StringUtils.EMPTY);
            }
        }
        return solutionPageTitle;
    }

    /**
     * Gets the solution page without extension.
     *
     * @param path
     *            the path
     * @return the solution page without extension
     */
    public static String getSolutionPageWithoutExtension(final String path) {
        return StringUtils.substringBeforeLast(path, ".");
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

    /**
     * Gets the mega menu configuration model.
     *
     * @param request
     *            the request
     * @return the mega menu configuration model
     */
    public static MegaMenuConfigurationModel getMegaMenuConfigurationModel(final SlingHttpServletRequest request,
            final String path) {
        MegaMenuConfigurationModel megaMenuConfigurationModel = new MegaMenuConfigurationModel();
        final String rootPath = LinkUtils.getRootPath(path);
        final String pagePath = rootPath + "/jcr:content/root/responsivegrid/megamenuconfig";
        final Resource megaMenuConfigResource = request.getResourceResolver().getResource(pagePath);
        if (Objects.nonNull(megaMenuConfigResource)) {
            megaMenuConfigurationModel = megaMenuConfigResource.adaptTo(MegaMenuConfigurationModel.class);
        }
        return megaMenuConfigurationModel;
    }
}
