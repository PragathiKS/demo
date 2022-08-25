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
    
    /** The Constant DOWNLOADABLE_DOCS. */
    private static final String DOWNLOADABLE_DOCS = "(css|js|xls|xlsx|doc|docx|pdf|json|ppt|pptx|xml|txt)$";

    /** The Constant DOWNLOADABLE_ASSETS. */
    private static final String DOWNLOADABLE_ASSETS = "(jpg|gif|png|jpeg|mp4|ico|woff|ttf|svg|eps|tif|icc|acb)$";

    /** The Constant FORWARD_SLASH. */
    private static final String FORWARD_SLASH = "/";

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
        return StringUtils.isNotBlank(path) && path.startsWith("/content") && !path.startsWith("/content/dam/") && !path.endsWith(".html");
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
		&& (FilenameUtils.getExtension(link).matches(DOWNLOADABLE_ASSETS)
			|| FilenameUtils.getExtension(link).matches(DOWNLOADABLE_DOCS))) {
            linkType = CustomerHubConstants.DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link))
		&& (FilenameUtils.getExtension(link).matches(DOWNLOADABLE_ASSETS)
			|| FilenameUtils.getExtension(link).matches(DOWNLOADABLE_DOCS))) {
            linkType = CustomerHubConstants.EXTERNAL_DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isInternalLink(link))) {
            linkType = CustomerHubConstants.INTERNAL_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link))) {
            linkType = CustomerHubConstants.EXTERNAL_LINK;
        } 
        
        return linkType;
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
}
