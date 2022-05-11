package com.tetrapak.customerhub.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

/**
 * This is a Link util class to access link utility methods
 *
 * @author Ruhee sharma
 */
public final class LinkUtil {

    /** The Constant DOWNLOADABLE_ASSETS. */
    private static final String DOWNLOADABLE_ASSETS = "(jpg|gif|png|css|js|xls|xlsx|doc|docx|pdf|jpeg|mp4|json|css|ico|woff|ttf|svg|eps|png|tif|ppt|pptx|xml)$";

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
            return resolvedPath + CustomerHubConstants.HTML_EXTENSION;
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
        return StringUtils.isNotBlank(path) && path.startsWith("/content");
    }

    /**
     * Checks if is external link.
     *
     * @param link
     *            the link
     * @return the boolean
     */
    public static Boolean isExternalLink(final String link) {
        return (!StringUtils.isEmpty(link) && !link.startsWith(CustomerHubConstants.CONTENT_PATH)
                && (link.startsWith(CustomerHubConstants.HTTP) || link.startsWith(CustomerHubConstants.WWW)));
    }

    /**
     * Check link type.
     *
     * @param link
     *            the link
     * @return the string
     */
    public static String checkLinkType(final String link) {
        String linkType = StringUtils.EMPTY;
        if (StringUtils.isBlank(link)) {
            linkType = "#";
        } else if (link.startsWith(CustomerHubConstants.CONTENT_DAM_PATH)
                && FilenameUtils.getExtension(link).matches(DOWNLOADABLE_ASSETS)) {
            linkType = CustomerHubConstants.DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link))
                && FilenameUtils.getExtension(link).matches(DOWNLOADABLE_ASSETS)) {
            linkType = CustomerHubConstants.EXTERNAL_DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isInternalLink(link))) {
            linkType = CustomerHubConstants.INTERNAL_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link))) {
            linkType = CustomerHubConstants.EXTERNAL_LINK;
        }
        return linkType;
    }
}
