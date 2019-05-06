package com.tetrapak.customerhub.core.utils;

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
    
    private LinkUtil() {
        throw new IllegalStateException("Utility class");
      }
    
    /**
     * Method to get Valid Link
     *
     * @param resource Resource
     * @param pathField String
     * @return String path
     */
    public static String getValidLink(Resource resource, String pathField) {       
        ResourceResolver resolver = resource.getResourceResolver();
        if (null == pathField || null == resource || null == resolver) {
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
}
