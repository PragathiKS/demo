package com.tetrapak.supplierportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.supplierportal.core.services.UserPreferenceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;

import javax.jcr.Session;
import javax.servlet.http.Cookie;
import java.util.Iterator;

/**
 * This is a global util class to access globally common utility methods.
 *
 * @author Nitin Kumar
 */
public class GlobalUtil {

    private static final String NAVIGATION_PATH = "/jcr:content/root/responsivegrid";
    private static final String NAVIGATION = "navigationconfiguration";

    /**
     * Method to get service.
     *
     * @param <T>   the generic type
     * @param clazz class type
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

    /**
     * Method to get navigation config resource for a request.
     *
     * @param request sling request
     * @return navigation config resource
     */
    public static Resource getNavigationConfigurationResource(SlingHttpServletRequest request) {
        Resource childResource = request.getResourceResolver()
                .getResource(getSupplierPortalConfigPagePath(request.getResource()) + NAVIGATION_PATH);
        if (null != childResource) {
            return getNavigationConfigNode(childResource);
        }
        return null;
    }

    /**
     * This method provides the supplier portal navigation config page path.
     *
     * @param contentPageResource content page resource
     * @return String navigation config page path
     */
    public static String getSupplierPortalConfigPagePath(Resource contentPageResource) {
        String supplierportalConfigPagePath = StringUtils.EMPTY;
        Page configPage = getSupplierPortalConfigPage(contentPageResource);
        if (null != configPage) {
            supplierportalConfigPagePath = configPage.getPath();
        }
        return supplierportalConfigPagePath;
    }

    /**
     * This method provides the supplier portal navigation config page.
     *
     * @param contentPageResource content page resource
     * @return Page navigation config
     */
    public static Page getSupplierPortalConfigPage(Resource contentPageResource) {
        final int DEPTH = 4;
        return getPageFromResource(contentPageResource, DEPTH);
    }

    /**
     * The method provides the page provided the following parameters.
     *
     * @param contentPageResource content resource
     * @param depth               calculated from 'content' node
     * @return Page content page
     */
    public static Page getPageFromResource(Resource contentPageResource, int depth) {
        PageManager pageManager = contentPageResource.getResourceResolver().adaptTo(PageManager.class);
        Page contentPage = null;
        if (null != contentPageResource && null != pageManager) {
            Page currentPage = pageManager.getContainingPage(contentPageResource);
            contentPage = currentPage.getAbsoluteParent(depth);
        }
        return contentPage;
    }

    /**
     * Gets the navigation config node.
     *
     * @param childResource the child resource
     * @return the navigation config node
     */
    private static Resource getNavigationConfigNode(Resource childResource) {
        Resource res = childResource.getChild(NAVIGATION);
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

    /**
     * Method to get selected language.
     *
     * @param request               sling request
     * @param userPreferenceService user preference service
     * @return string language code
     */
    public static String getSelectedLanguage(SlingHttpServletRequest request, UserPreferenceService userPreferenceService) {
        Cookie languageCookie = request.getCookie("lang-code");
        if (null != languageCookie) {
            return languageCookie.getValue();
        }
        Session session = request.getResourceResolver().adaptTo(Session.class);
        if (null != session && null != userPreferenceService) {
            return userPreferenceService.getSavedPreferences(session.getUserID(), SupplierPortalConstants.LANGUGAGE_PREFERENCES);
        }
        return null;
    }
}
