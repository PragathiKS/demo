package com.tetrapak.customerhub.core.utils;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;

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
     * Fetch page base on availability in below order:
     * - i18n key if exist for the page
     * - navigation title if exist
     * - page title if all other are empy
     *
     * @param page
     *            the page
     * @return the navigation title
     */
    public static String getPageTitle(final Page page) {
        final ValueMap valueMap = page.getContentResource().getValueMap();
        String title = getPageNameI18key(valueMap);
        if(StringUtils.isEmpty(title)) {
            title = page.getTitle();
        }
        return title;
    }

    /**
     * Fetch navigation title i18n key of a page. If not present sends page Title as fallback
     *
     * @param valueMap
     *            the valueMap of a page
     * @return the navigation kay or title
     */
    private static String getPageNameI18key(ValueMap valueMap) {
        if (valueMap.containsKey("iconLabel")) {
            return (String) valueMap.get("iconLabel");
        }
        return "";
    }
}
