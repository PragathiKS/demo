package com.tetrapak.publicweb.core.utils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.tetrapak.publicweb.core.constants.PWConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.vault.util.Text;
import org.apache.sling.api.resource.ResourceResolver;

public class LinkUtils extends WCMUsePojo {

    private static final String PARAM_LINK = "linkPath";
    private String sanitizedLink;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

    /**
     * Add .html to link if is internal
     *
     * @param link
     */
    public static String sanitizeLink(final String link, final ResourceResolver resolver) {
        if (StringUtils.isBlank(link)) {
            return "#";
        } else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html")
                && !link.endsWith(".htm")) {
            if (GlobalUtil.isPublish()) {
                return resolver.map(link);
            }
            return resolver.map(link + ".html");
        }
        return link;
    }

    /**
     * Gets the root path.
     *
     * @param pagePath
     *            the page path
     * @return the root path
     */
    public static String getRootPath(final String pagePath) {
        return Text.getAbsoluteParent(pagePath, PWConstants.LANGUAGE_PAGE_LEVEL);
    }

    /**
     * Gets the root path.
     *
     * @param pagePath
     *            the page path
     * @return the root path
     */
    public static String getMarketsRootPath(final String pagePath) {
        return Text.getAbsoluteParent(pagePath, PWConstants.MARKET_ROOT_PAGE_LEVEL);
    }

    /**
     * Used for analytics code to determine the link type
     */
    public static String linkType(final String linkPath) {
        if (StringUtils.startsWith(linkPath, "/content/dam/") || StringUtils.endsWith(linkPath, ".pdf")) {
            return "download";
        } else if (StringUtils.startsWith(linkPath, "/content/")) {
            return "internal";
        } else {
            return "external";
        }
    }

    @Override
    public void activate() throws Exception {
        sanitizedLink = get(PARAM_LINK, String.class);
    }

    public String getSanitizedLink() {
        return LinkUtils.sanitizeLink(sanitizedLink, getResourceResolver());
    }

    /**
     * Gets the asset name.
     *
     * @param path the asset path.
     * @return the asset name.
     */
    public static String getAssetName(final String path) {
        String assetName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(path)) {
            assetName = getSubstringAfterLast(path);
        }
        return assetName;
    }

    /**
     * Gets the substring after last.
     *
     * @param path the path
     * @return the substring after last
     */
    private static String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, FORWARD_SLASH);
    }
}
