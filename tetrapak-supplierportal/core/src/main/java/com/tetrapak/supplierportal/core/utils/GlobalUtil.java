package com.tetrapak.supplierportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

import java.util.Iterator;

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
    @SuppressWarnings("unchecked") public static <T> T getService(final Class<T> clazz) {
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
        return slingSettingsService.getRunModes().contains(SupplierPortalConstants.PUBLISH);
    }

    public static Resource getGlobalConfigurationResource(SlingHttpServletRequest request) {
        Resource childResource = request.getResourceResolver().getResource(
                GlobalUtil.getCustomerhubConfigPagePath(request.getResource()) + "/jcr:content/root/responsivegrid");
        if (null != childResource) {
            return getGlobalConfigNode(childResource);
        }
        return null;
    }

    public static String getCustomerhubConfigPagePath(Resource contentPageResource) {
        String customerhubConfigPagePath = StringUtils.EMPTY;
        Page configPage = getCustomerhubConfigPage(contentPageResource);
        if (null != configPage) {
            customerhubConfigPagePath = configPage.getPath();
        }
        return customerhubConfigPagePath;
    }

    public static Page getCustomerhubConfigPage(Resource contentPageResource) {
        final int DEPTH = 4;
        return getPageFromResource(contentPageResource, DEPTH);
    }

    public static Page getPageFromResource(Resource contentPageResource, int depth) {
        PageManager pageManager = contentPageResource.getResourceResolver().adaptTo(PageManager.class);
        Page contentPage = null;
        if (null != contentPageResource && null != pageManager) {
            Page currentPage = pageManager.getContainingPage(contentPageResource);
            contentPage = currentPage.getAbsoluteParent(depth);
        }
        return contentPage;
    }

    private static Resource getGlobalConfigNode(Resource childResource) {
        Resource res = childResource.getChild("globalconfiguration");
        if (null != res) {
            return res;
        } else {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource nextResource = itr.next();
                if (nextResource.isResourceType(SupplierPortalConstants.GLOBAL_CONFIGURATION_RESOURCE_TYPE)) {
                    return nextResource;
                }
            }
        }
        return null;
    }
}
