package com.tetralaval.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetralaval.constants.PWConstants;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * The Class PageUtil.
 *
 * @author Sandip kumar
 */
public final class PageUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageUtil.class);

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
     * This method returns page locale default is en-US.
     *
     * @param currentPage
     *            current page
     * @return local current locale
     */
    public static Locale getPageLocale(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getLanguage(false);
        }

        return new Locale.Builder().setLanguage(PWConstants.SITE_LANGUAGE_COUNTRY_CODE)
                .setRegion(PWConstants.GLOBAL_ISO_CODE).build();
    }

    /**
     * Gets the language page.
     *
     * @param currentPage
     *            the current page
     * @return language
     */
    public static Page getLanguagePage(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(PWConstants.MARKET_ROOT_PAGE_LEVEL);
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
            return currentPage.getAbsoluteParent(PWConstants.MARKET_ROOT_PAGE_LEVEL);
        }
        return null;
    }
}
