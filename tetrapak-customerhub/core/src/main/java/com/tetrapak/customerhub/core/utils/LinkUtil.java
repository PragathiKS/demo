package com.tetrapak.customerhub.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

public class LinkUtil {
    
    private static Boolean isExternalLink;
    private static String path;
    
    public static String getValidLink(Resource resource, String pathField) {
        if (null == pathField) {
            return StringUtils.EMPTY;
        }
        if (isInternalLink(pathField)) {
            ResourceResolver resolver = resource.getResourceResolver();
            if (null == resolver) {
                return StringUtils.EMPTY;
            }
            String resolvedPath = resolver.map(pathField);
            if (null == resolvedPath) {
                return StringUtils.EMPTY;
            }
            return resolvedPath + CustomerHubConstants.HTML_EXTENSION;
        } else {
            return path;
        }
    }
    
    private static boolean isInternalLink(String path) {
        return StringUtils.isNotBlank(path) && path.startsWith("/content");
    }
}
