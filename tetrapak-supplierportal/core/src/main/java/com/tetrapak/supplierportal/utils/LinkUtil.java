package com.tetrapak.supplierportal.utils;

import com.tetrapak.supplierportal.constants.SupplierPortalConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * This is a Link util class to access link utility methods
 *
 * @author Ruhee sharma
 */
public final class LinkUtil {

    /**
     * The Constant DOWNLOADABLE_ASSETS.
     */
    private static final String DOWNLOADABLE_ASSETS = "(jpg|gif|png|css|js|xls|xlsx|doc|docx|pdf|jpeg|mp4|json|css|ico|woff|ttf|svg|eps|png|tif|ppt|pptx|xml|icc|acb)$";

    /**
     * The Constant FORWARD_SLASH.
     */
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
        return StringUtils.isNotBlank(path) && path.startsWith("/content") && !path.startsWith("/content/dam/")
                && !path.endsWith(".html");
    }

    /**
     * Checks if is external link.
     *
     * @param link the link
     * @return the boolean
     */
    public static Boolean isExternalLink(final String link) {
        return (!StringUtils.isEmpty(link) && !link.startsWith(SupplierPortalConstants.CONTENT_PATH) && (
                link.startsWith(SupplierPortalConstants.HTTP) || link.startsWith(SupplierPortalConstants.WWW)));
    }

    /**
     * Check link type.
     *
     * @param link the link
     * @return the string
     */
    public static String checkLinkType(final String link) {
        String linkType = StringUtils.EMPTY;
        if (StringUtils.isBlank(link)) {
            linkType = "#";
        } else if (link.startsWith(SupplierPortalConstants.CONTENT_DAM_PATH) && FilenameUtils.getExtension(link)
                .matches(DOWNLOADABLE_ASSETS)) {
            linkType = SupplierPortalConstants.DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link)) && FilenameUtils.getExtension(link)
                .matches(DOWNLOADABLE_ASSETS)) {
            linkType = SupplierPortalConstants.EXTERNAL_DOWNLOAD_LINK;
        } else if (Boolean.TRUE.equals(isInternalLink(link))) {
            linkType = SupplierPortalConstants.INTERNAL_LINK;
        } else if (Boolean.TRUE.equals(isExternalLink(link))) {
            linkType = SupplierPortalConstants.EXTERNAL_LINK;
        }

        return linkType;
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

	public static String sanitizeLink(final String link, final SlingHttpServletRequest request) {
		if (StringUtils.isBlank(link)) {
			return "#";
		} else if (Boolean.TRUE.equals(isPreviewURL(request))) {
			return request.getResourceResolver().map(link);
		} else if (link.startsWith("/content/") && !link.startsWith("/content/dam/") && !link.endsWith(".html")
				&& !link.endsWith(".htm")) {
			/*
			 * if (GlobalUtil.isPublish()) { return request.getResourceResolver().map(link);
			 * }
			 */
			return link + ".html";
		}
		return link;
	}
	
	public static Boolean isPreviewURL(SlingHttpServletRequest request) {
		String previewHeader = request.getHeader("preview");
		Boolean isPreviewURL = false;
		if ("true".equalsIgnoreCase(previewHeader)) {
			isPreviewURL = true;
		}
		return isPreviewURL;
	}

}
