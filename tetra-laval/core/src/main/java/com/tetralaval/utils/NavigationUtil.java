package com.tetralaval.utils;

import com.day.cq.wcm.api.Page;

import org.apache.commons.lang3.StringUtils;


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
