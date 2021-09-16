package com.tetralaval.utils;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        final ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());
        if (null == serviceReference) {
            return null;
        }
        return (T) bundleContext.getService(serviceReference);
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
     * Checks if it is publish.
     *
     * @return true, if is publish
     */
    public static boolean isPublish() {
        final SlingSettingsService slingSettingsService = getService(SlingSettingsService.class);
        if (slingSettingsService == null) {
            return false;
        }
        return slingSettingsService.getRunModes().contains("publish");
    }
}
