package com.tetrapak.customerhub.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

/**
 * Utility class for page API related methods
 *
 * @author Nitin Kumar
 */
public final class PageUtil {

    private PageUtil() {
        //adding private constructor
    }

    /**
     * Method to check if given resource is inside the current page
     *
     * @param page     page
     * @param resource current resource
     * @return true if resource is within current page
     */
    public static boolean isCurrentPage(Page page, Resource resource) {
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if (null != pageManager) {
            Page currentPage = pageManager.getContainingPage(resource);
            return StringUtils.equalsIgnoreCase(page.getPath(), currentPage.getPath());
        }
        return false;
    }

    /**
     * Method to get page from the given path
     *
     * @param resource resource
     * @param path     page path
     * @return page
     */
    public static Page getPageFromPath(Resource resource, String path) {
        if (null == resource) {
            return null;
        }
        Resource pageResource = resource.getResourceResolver().getResource(path);
        if (null != pageResource) {
            return pageResource.adaptTo(Page.class);
        }
        return null;
    }
}
