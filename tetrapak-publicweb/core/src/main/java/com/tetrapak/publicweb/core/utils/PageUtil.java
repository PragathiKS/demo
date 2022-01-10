package com.tetrapak.publicweb.core.utils;

import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.PWConstants;

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

        return new Locale.Builder().setLanguage(PWConstants.ENGLISH_LANGUAGE_ISO_CODE)
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
            return currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL);
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
        if (Objects.nonNull(currentPage)) {
            if (currentPage.getPath().startsWith(PWConstants.TETRA_LAVAL_CONTENT_PATH)) {
                return currentPage.getAbsoluteParent(PWConstants.TL_LANGUAGE_PAGE_LEVEL);
            } else {
                return currentPage.getAbsoluteParent(PWConstants.LANGUAGE_PAGE_LEVEL);
            }            
        }
        return null;
    }

    /**
     * Gets the country page.
     *
     * @param currentPage
     *            the current page
     * @return country page
     */
    public static Page getCountryPage(final Page currentPage) {
        return currentPage.getAbsoluteParent(PWConstants.COUNTRY_PAGE_LEVEL);
    }

    /**
     * Gets the language code.
     *
     * @param languagePage
     *            the language page
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

    /**
     * Gets the language code from resource.
     *
     * @param resource
     *            the resource
     * @return the language code from resource
     */
    public static String getLanguageCodeFromResource(final Resource resource) {
        return getLanguageCode(getLanguagePage(resource));
    }

    /**
     * Gets the country code from resource.
     *
     * @param resource
     *            the resource
     * @return the country code from resource
     */
    public static String getCountryCodeFromResource(final Resource resource) {
        return getCountryCode(getLanguagePage(resource));
    }

    /**
     * Gets the country code.
     *
     * @param languagePage
     *            the country page
     * @return the country code
     */
    public static String getCountryCode(final Page languagePage) {
        final String country = PWConstants.GLOBAL_ISO_CODE;
        if (languagePage != null) {
            return languagePage.getParent().getName();
        }
        return country;
    }

    /**
     * This method is used for getting locale in form of language-country say en-gb.
     *
     * @param page
     *            page
     * @return String locale in form of language-country
     */
    public static String getLocaleFromURL(final Page page) {
        return getLanguageCode(page).concat(PWConstants.HYPHEN).concat(getCountryCode(page));
    }

    /**
     * Gets the market code.
     *
     * @param resource
     *            the resource
     * @return the market code
     */
    public static String getMarketCode(Resource resource) {
        String marketCode = PageUtil.getCountryCodeFromResource(resource);
        if (StringUtils.isNotBlank(marketCode)) {
            switch (marketCode) {
                case PWConstants.MAGHREB_COUNTRY_CODE:
                    marketCode = PWConstants.MAGHREB_MARKET_CODE;
                    break;
                case PWConstants.CAC_COUNTRY_CODE:
                    marketCode = PWConstants.CAC_MARKET_CODE;
                    break;
                default:
                    LOGGER.info("Not a valid country code");
            }
        }
        return marketCode;
    }

    public static Boolean isHomePage(final String currentPagePath){
        return currentPagePath.endsWith(PWConstants.HOME_PAGE_REL_PATH);
    }
}
