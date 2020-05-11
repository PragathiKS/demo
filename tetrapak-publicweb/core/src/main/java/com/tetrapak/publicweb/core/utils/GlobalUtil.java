package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SiteImproveScriptService;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);

    private GlobalUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Method to get service
     *
     * @param clazz
     *            class type
     * @return T
     */
    public static <T> T getService(final Class<T> clazz) {
        if (FrameworkUtil.getBundle(clazz) == null) {
            return null;
        }
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());
        if (null == serviceReference) {
            return null;
        }
        return (T) bundleContext.getService(serviceReference);
    }

    /**
     * @return site improve script
     */
    public static String getSiteImproveScript() {
        SiteImproveScriptService siteImproveScriptService = getService(SiteImproveScriptService.class);
        if (null == siteImproveScriptService) {
            return null;
        }
        return siteImproveScriptService.getSiteImproveScriptUrl();
    }

    /**
     * get scene 7 video url
     *
     * @param damVideoPath
     *            video path
     * @param dynamicMediaService
     *            dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(String damVideoPath, DynamicMediaService dynamicMediaService) {
        if(StringUtils.isNotBlank(damVideoPath)) {
            final String FORWARD_SLASH = PWConstants.SLASH;
            damVideoPath = StringUtils.substringBeforeLast(damVideoPath, ".");
            damVideoPath = StringUtils.substringAfterLast(damVideoPath, FORWARD_SLASH);
            damVideoPath = dynamicMediaService.getVideoServiceUrl() + dynamicMediaService.getRootPath() + FORWARD_SLASH
                    + damVideoPath;
        }
        return damVideoPath;
    }

    /**
     * This method returns service resolver based on parameter map.
     * 
     * @param resourceFactory
     *            ResourceResolverFactory
     * @param paramMap
     *            Java Util Map
     * @return sling resource resolver
     */
    public static ResourceResolver getResourceResolverFromSubService(final ResourceResolverFactory resourceFactory) {
        ResourceResolver resourceResolver = null;
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        try {
            resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
        } catch (final LoginException e) {
            LOG.error("Unable to fetch resourceResolver for subservice {} exception {}",
                    paramMap.get(ResourceResolverFactory.SUBSERVICE), e.getMessage(), e);
        }
        return resourceResolver;
    }

    /**
     * Global function which provides DAM asset path for PXP integration
     * 
     * @param productId
     * @param categoryId
     * @param sourceurl
     * @return
     */
    public static String getDAMPath(String damRootPath, String sourceurl, String categoryId, String productId,
            String videoTypes) {
        String finalDAMPath = null;
        String assetType = getAssetType(sourceurl, videoTypes);
        String fileName = getFileName(sourceurl);

        if (!StringUtils.isEmpty(assetType) && !StringUtils.isEmpty(categoryId) && !StringUtils.isEmpty(productId)
                && !StringUtils.isEmpty(fileName)) {
            finalDAMPath = new StringBuffer(damRootPath).append(PWConstants.SLASH).append(categoryId)
                    .append(PWConstants.SLASH).append(productId).append(PWConstants.SLASH).append(assetType)
                    .append(PWConstants.SLASH).append(fileName).toString();
        } else {
            LOG.error(
                    "One of the mandatory input not provided for DAM path \n Asset Type {}  \nCategoryId  {} \nProductId {} \nfileName {}",
                    assetType, categoryId, productId, fileName);
        }
        return finalDAMPath;

    }

    /**
     * Extract filename from given URL
     * 
     * @param fileURL
     * @return
     */
    public static String getFileName(String fileURL) {
        return fileURL.substring(fileURL.lastIndexOf('/') + 1, fileURL.length());
    }

    /**
     * Fetch Asset Type based on File extension depending on mapping
     * 
     * @param sourceurl
     * @param videoTypes
     * @return
     */
    public static String getAssetType(String sourceurl, String videoTypes) {
        String assetType = PWConstants.IMAGE;
        String fileExtension = sourceurl.substring(sourceurl.lastIndexOf('.') + 1, sourceurl.length());
        LOG.debug("fileExtension {}", fileExtension);
        if (videoTypes.contains(fileExtension)) {
            assetType = PWConstants.VIDEO;
        }
        return assetType;
    }

}
