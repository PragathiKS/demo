package com.tetrapak.supplierportal.core.utils;

import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * This is a Link util class to access link utility methods
 *
 * @author Ruhee sharma
 */
public final class LinkUtil {

    private LinkUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Method to get Valid Link
     *
     * @param resource  Resource
     * @param pathField String
     * @return String path
     */
    public static String getValidLink(Resource resource, String pathField) {
        ResourceResolver resolver = resource.getResourceResolver();
        if (StringUtils.isBlank(pathField) || null == resolver) {
            return StringUtils.EMPTY;
        }
        if (isInternalLink(pathField)) {
            String resolvedPath = resolver.map(pathField);
            return resolvedPath + SupplierPortalConstants.HTML_EXTENSION;
        } else {
            return pathField;
        }
    }

    /**
     * Method to check Internal link starts with "/content" and not blank
     *
     * @param path String
     * @return boolean
     */

    private static boolean isInternalLink(String path) {
        return StringUtils.isNotBlank(path) && path.startsWith(SupplierPortalConstants.CONTENT_PATH)
                && !path.startsWith(SupplierPortalConstants.CONTENT_DAM_PATH) && !path.endsWith(
                SupplierPortalConstants.HTML_EXTENSION);
    }

    /**
     * Checks if is external link.
     *
     * @param link the link
     * @return the boolean
     */
    public static Boolean isExternalLink(final String link) {
        if (StringUtils.isEmpty(link)) {
            return Boolean.FALSE;
        }
        return (!link.startsWith(SupplierPortalConstants.CONTENT_PATH)
                && (link.startsWith(SupplierPortalConstants.HTTP) || link.startsWith(SupplierPortalConstants.HTTPS)
                        || link.startsWith(SupplierPortalConstants.WWW)));
    }

    public static String sanitizeLink(final String link, final SlingHttpServletRequest request) {
        if (StringUtils.isBlank(link)) {
            return SupplierPortalConstants.HASH;
        } else if (Boolean.TRUE.equals(isPreviewURL(request))) {
            return request.getResourceResolver().map(link);
        }
        return sanitizeLink0(link, request);
    }

    private static String sanitizeLink0(final String link, final SlingHttpServletRequest request) {
        if (link.startsWith(SupplierPortalConstants.CONTENT_PATH) && !link.startsWith(
                SupplierPortalConstants.CONTENT_DAM_PATH) && !link.endsWith(SupplierPortalConstants.HTML_EXTENSION)
                && !link.endsWith(SupplierPortalConstants.HTM_EXTENSION)) {
            if (GlobalUtil.isPublish()) {
                return request.getResourceResolver().map(link);
            }
            return link + SupplierPortalConstants.HTML_EXTENSION;
        }
        return link;
    }

    public static Boolean isPreviewURL(HttpServletRequest request) {
        String previewHeader = request.getHeader(SupplierPortalConstants.PREVIEW);
        Boolean isPreviewURL = false;
        if (SupplierPortalConstants.TRUE.equalsIgnoreCase(previewHeader)) {
            isPreviewURL = true;
        }
        return isPreviewURL;
    }
}
