package com.tetrapak.publicweb.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.vault.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;

import com.adobe.cq.sightly.WCMUsePojo;
import com.tetrapak.publicweb.core.constants.PWConstants;
import org.apache.sling.api.resource.Resource;

/**
 * The Class LinkUtils.
 */
public class LinkUtils extends WCMUsePojo {

    /** The sanitized link. */
    private String sanitizedLink;

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

    /**
     * Add .html to link if is internal
     *
     * @param link the link
     * @param request the request
     * @return the string
     */
    public static String sanitizeLink(final String link, final SlingHttpServletRequest request) {
        if (StringUtils.isBlank(link)) {
            return "#";
        } else if (Boolean.TRUE.equals(isPreviewURL(request))) {
            return request.getResourceResolver().map(link);
        } else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html")
                && !link.endsWith(".htm")) {
            if (GlobalUtil.isPublish()) {
                return request.getResourceResolver().map(link);
            }
            return link + ".html";
        }
        return link;
    }

    public static String sanitizeLink(final String link, final Resource resource) {
        if (StringUtils.isBlank(link)) {
            return "#";
        } else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html")
                && !link.endsWith(".htm")) {
            if (GlobalUtil.isPublish()) {
                return resource.getResourceResolver().map(link);
            }
            return link + ".html";
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
     * Checks if is preview URL.
     *
     * @param request the request
     * @return the boolean
     */
    public static Boolean isPreviewURL(SlingHttpServletRequest request) {
        String previewHeader = request.getHeader("preview");
        Boolean isPreviewURL = false;
        if ("true".equalsIgnoreCase(previewHeader)) {
            isPreviewURL = true;
        }
        return isPreviewURL;
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
     * Gets the country path.
     *
     * @param pagePath
     *            the page path
     * @return the root path
     */
    public static String getCountryPath(final String pagePath) {
        return Text.getAbsoluteParent(pagePath, PWConstants.COUNTRY_PAGE_LEVEL);
    }

    /**
     * Used for analytics code to determine the link type.
     *
     * @param linkPath the link path
     * @return the string
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

    /**
     * Activate.
     *
     * @throws Exception the exception
     */
    @Override
    public void activate() throws Exception {
        sanitizedLink = get(PWConstants.PARAM_LINK, String.class);

    }

    /**
     * Gets the sanitized link.
     *
     * @return the sanitized link
     */
    public String getSanitizedLink() {
        return LinkUtils.sanitizeLink(sanitizedLink, getRequest());
    }

    /**
     * Gets the asset name.
     *
     * @param path
     *            the asset path.
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
     * @param path
     *            the path
     * @return the substring after last
     */
    private static String getSubstringAfterLast(final String path) {
        return StringUtils.substringAfterLast(path, FORWARD_SLASH);
    }
    
    
    /**
     * Gets the url without protocol.
     *
     * @param path
     *            the path
     * @return the url without protocol
     */
    public static String getUrlWithoutProtocol(final String path) {
        String updatedUrl = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(path)) {
            updatedUrl = path.replaceFirst("^(http[s]?://)", "");
        }
        return updatedUrl;
    }
}
