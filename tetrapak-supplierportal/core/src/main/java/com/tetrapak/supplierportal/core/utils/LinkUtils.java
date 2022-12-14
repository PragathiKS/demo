package com.tetrapak.supplierportal.core.utils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.vault.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * The Class LinkUtils.
 */
public class LinkUtils extends WCMUsePojo {

    /**
     * The sanitized link.
     */
    private String sanitizedLink;

    /**
     * The Constant FORWARD_SLASH.
     */
    private static final String FORWARD_SLASH = "/";

    /**
     * Add .html to link if is internal
     *
     * @param link    the link
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
                return request.getResourceResolver().map(link) + ".html";
            }
            return link + ".html";
        }
        return link;
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
     * Activate.
     *
     * @throws Exception the exception
     */
    @Override public void activate() throws Exception {
        sanitizedLink = get(SupplierPortalConstants.PARAM_LINK, String.class);

    }

}
