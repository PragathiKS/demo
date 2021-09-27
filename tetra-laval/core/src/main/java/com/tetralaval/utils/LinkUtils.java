package com.tetralaval.utils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.tetralaval.constants.TLConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.vault.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * LinkUtils
 */
public class LinkUtils extends WCMUsePojo {

    /**
     * Add .html to link if is internal
     *
     * @param link
     */
    public static String sanitizeLink(final String link, final SlingHttpServletRequest request) {
        if (StringUtils.isBlank(link)) {
            return "#";
        } else if (Boolean.TRUE.equals(isPreviewURL(request))) {
            return request.getResourceResolver().map(link);
        } else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(TLConstants.HTML_EXTENSION)
                && !link.endsWith(".htm")) {
            if (GlobalUtil.isPublish()) {
                return request.getResourceResolver().map(link);
            }
            return link + TLConstants.HTML_EXTENSION;
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
        return Text.getAbsoluteParent(pagePath, TLConstants.LANGUAGE_PAGE_LEVEL);
    }

    /**
     * Check if url follows to preview
     * @param request
     * @return
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
        // This is override method
    }
}
