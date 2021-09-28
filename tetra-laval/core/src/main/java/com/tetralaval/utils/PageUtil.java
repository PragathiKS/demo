package com.tetralaval.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetralaval.constants.TLConstants;
import org.apache.sling.api.resource.Resource;

/**
 * The Class PageUtil.
 *
 * @author Sandip kumar
 */
public final class PageUtil {

    /**
     * Instantiates a new page util.
     */
    private PageUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Get the current page.
     *
     * @param resource
     *            the resource
     * @return the current page
     */
    public static Page getCurrentPage(final Resource resource) {
        if (resource != null) {
            final PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
            if (pageManager != null) {
                return pageManager.getContainingPage(resource);
            }
        }
        return null;
    }

    /**
     * Gets the language page.
     *
     * @param resource
     *            the resource
     * @return language page
     */
    public static Page getLanguagePage(final Resource resource) {
        final Page currentPage = getCurrentPage(resource);
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(TLConstants.LANGUAGE_PAGE_LEVEL);
        }
        return null;
    }

    /**
     * Check if path represents an experience fragment
     * @param path
     * @return
     */
    public static boolean isExperienceFragment(final String path) {
        return path.contains(TLConstants.EXPERIENCE_FRAGMENTS_PATH);
    }

    /**
     * Check if path represents a language master
     * @param path
     * @return
     */
    public static boolean isLanguageMaster(final String path) {
        return path.contains(TLConstants.LANGUAGE_MASTERS_PATH);
    }
}
