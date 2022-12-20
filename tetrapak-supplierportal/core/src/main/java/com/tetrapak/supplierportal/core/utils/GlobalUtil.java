package com.tetrapak.supplierportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import java.util.Iterator;

public class GlobalUtil {

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

    public static boolean isPublish() {
        final SlingSettingsService slingSettingsService = getService(SlingSettingsService.class);
        if (slingSettingsService == null) {
            return false;
        }
        return slingSettingsService.getRunModes().contains(SupplierPortalConstants.PUBLISH);
    }

    public static Resource getNavigationConfigurationResource(SlingHttpServletRequest request) {
        Resource childResource = request.getResourceResolver().getResource(
                getSupplierPortalConfigPagePath(request.getResource()) + "/jcr:content/root/responsivegrid");
        if (null != childResource) {
            return getNavigationConfigNode(childResource);
        }
        return null;
    }

    public static String getSupplierPortalConfigPagePath(Resource contentPageResource) {
        String supplierportalConfigPagePath = StringUtils.EMPTY;
        Page configPage = getSupplierPortalConfigPage(contentPageResource);
        if (null != configPage) {
            supplierportalConfigPagePath = configPage.getPath();
        }
        return supplierportalConfigPagePath;
    }

    public static Page getSupplierPortalConfigPage(Resource contentPageResource) {
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

    private static Resource getNavigationConfigNode(Resource childResource) {
        Resource res = childResource.getChild("navigationconfiguration");
        if (null != res) {
            return res;
        } else {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource nextResource = itr.next();
                if (nextResource.isResourceType(SupplierPortalConstants.NAVIGATION_CONFIGURATION_RESOURCE_TYPE)) {
                    return nextResource;
                }
            }
        }
        return null;
    }
}
