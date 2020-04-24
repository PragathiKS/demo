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

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.SiteImproveScriptService;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

    private GlobalUtil() {
        /*
            adding a private constructor to hide the implicit one
         */
    }
    
    private static final Logger LOG =  LoggerFactory.getLogger(GlobalUtil.class);

    /**
     * Method to get service
     *
     * @param clazz class type
     * @return T
     */
    public static <T> T getService(final Class<T> clazz) {
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
     * @param damVideoPath        video path
     * @param dynamicMediaService dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(String damVideoPath, DynamicMediaService dynamicMediaService) {
        final String FORWARD_SLASH = "/";
        damVideoPath = StringUtils.substringBeforeLast(damVideoPath, ".");
        damVideoPath = StringUtils.substringAfterLast(damVideoPath, FORWARD_SLASH);
        damVideoPath = dynamicMediaService.getVideoServiceUrl() + dynamicMediaService.getRootPath()
                + FORWARD_SLASH + damVideoPath;
        return damVideoPath;
    }
    
    /**
     * This method returns service resolver based on parameter map.
     * @param resourceFactory ResourceResolverFactory
     * @param paramMap Java Util Map
     * @return sling resource resolver
     */
    public static ResourceResolver getResourceResolverFromSubService(final ResourceResolverFactory resourceFactory) {
        ResourceResolver resourceResolver = null; final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        if (!paramMap.isEmpty()) {
            try {
                resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
                LOG.debug("resourceResolver for user {}", resourceResolver.getUserID());
            } catch (final LoginException e) {
                LOG.error("Unable to fetch resourceResolver for subservice {} exception {}",
                        paramMap.get(ResourceResolverFactory.SUBSERVICE), e);
            }
        }
        return resourceResolver;
    }
}
