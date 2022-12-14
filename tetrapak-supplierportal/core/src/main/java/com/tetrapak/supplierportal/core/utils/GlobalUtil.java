package com.tetrapak.supplierportal.core.utils;

import org.apache.sling.settings.SlingSettingsService;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This is a global util class to access globally common utility methods.
 *
 * @author Akarsh
 */
public class GlobalUtil {

    /**
     * Method to get service.
     *
     * @param <T>   the generic type
     * @param clazz class type
     * @return T
     */
    @SuppressWarnings("unchecked")
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
