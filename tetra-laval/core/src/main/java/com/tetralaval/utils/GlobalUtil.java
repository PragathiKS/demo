package com.tetralaval.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.settings.SlingSettingsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

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
    
    /**
     * This method is used to get the string value from string Array in format of
     * {"key":"value","key1":"value1"}
     * @param stringArray
     * @param key
     * @return String value
     */
    public static String getKeyValueFromStringArray(final String stringArray, final String key){
        try {
            return new JSONObject(stringArray).get(key).toString();
        }catch (final JSONException exception) {
        }
        return StringUtils.EMPTY;
    }
}
