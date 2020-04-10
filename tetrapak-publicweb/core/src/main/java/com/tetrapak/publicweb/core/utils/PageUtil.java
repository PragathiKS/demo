package com.tetrapak.publicweb.core.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * The Class PageUtil.
 * 
 * @author Sandip kumar
 */
public final class PageUtil {

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

        return new Locale.Builder().setLanguage(PWConstants.ENGLISH_LANGUAGE_ISO_CODE)
                .setRegion(PWConstants.GB_ISO_CODE).build();
    }

    /**
     * @param currentPage
     * @return language
     */
    public static Page getLanguagePage(final Page currentPage) {
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL);
        }
        return null;
    }

    /**
     * @param resource
     *            the resource
     * @return language page
     */
    public static Page getLanguagePage(final Resource resource) {
        Page currentPage = getCurrentPage(resource);
        if (currentPage != null) {
            return currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL);
        }
        return null;
    }

    /**
     * @param currentPage
     * @return country page
     */
    public static Page getCountryPage(final Page currentPage) {
        return currentPage.getAbsoluteParent(PWConstants.COUNTRY_PAGE_LEVEL);
    }

    /**
     * @param languagePage
     * @return current language
     */
    public static String getLanguageCode(final Page languagePage) {
        String langauge = PWConstants.ENGLISH_LANGUAGE_ISO_CODE;
        if (languagePage != null) {
            langauge = languagePage.getLanguage().getLanguage();
        }
        if (StringUtils.isEmpty(langauge)) {
            langauge = PWConstants.ENGLISH_LANGUAGE_ISO_CODE;
        }
        return langauge;
    }

}
