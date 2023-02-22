package com.tetrapak.supplierportal.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.supplierportal.core.services.CookieDataDomainScriptService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.settings.SlingSettingsService;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import com.tetrapak.supplierportal.core.constants.SupplierPortalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * This is a global util class to access globally common utility methods.
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalUtil.class);


    private static final String NAVIGATION_PATH = "/jcr:content/root/responsivegrid";
    private static final String NAVIGATION = "navigationconfiguration";

    public GlobalUtil() {
    }
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
     * This method will fetch the OneTrust cookie script
     */
    public static String getDataDomainScript() {
        CookieDataDomainScriptService cookieDataDomainScriptService = getService(CookieDataDomainScriptService.class);
        String dataDomainScript = org.apache.commons.lang.StringUtils.EMPTY;
        if (cookieDataDomainScriptService != null) {
            String[] cookieParamArray = cookieDataDomainScriptService.getCookieDomainScriptConfig();
            for (String param : cookieParamArray) {
                if (param.contains(SupplierPortalConstants.SUPPLIER_PORTAL)) {
                    final String domainAbbreviationJsonString = param.split("=")[1];
                    dataDomainScript = GlobalUtil.getKeyValueFromStringArray(domainAbbreviationJsonString,
                            SupplierPortalConstants.DOMAINSCRIPT);
                    break;
                }
            }
        }
        return dataDomainScript;
    }

    /**
     * This method is used to get the string value from string Array in format of {"key":"value","key1":"value1"}.
     *
     * @param stringArray the string array
     * @param key         the key
     * @return String value
     */
    private static String getKeyValueFromStringArray(final String stringArray, final String key) {
        try {
            return new JSONObject(stringArray).get(key).toString();
        } catch (final JSONException exception) {
            LOGGER.error("JSONException while converting array string to json object: ", exception);
        }
        return org.apache.commons.lang.StringUtils.EMPTY;
    }
}
