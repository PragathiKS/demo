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
