package com.tetrapak.publicweb.core.utils;

import com.tetrapak.publicweb.core.services.SiteImproveScriptService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public class GlobalUtil {

    /**
     * Method to get service
     *
     * @param clazz class type
     * @return T
     */
    @SuppressWarnings("unchecked")
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
}
