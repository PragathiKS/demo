package com.tetrapak.customerhub.core.utils;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.services.APIGEEService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public class GlobalUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);

    /**
     * Method to get API GEE URL
     *
     * @param apigeeService API GEE Service
     * @param defaultJson   default json
     * @return String
     */
    public static String getApiURL(APIGEEService apigeeService, String defaultJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : defaultJson;
    }

    /**
     * Method to get Preference Url
     *
     * @param apigeeService   API GEE Service
     * @param preferencesJson preference json
     * @return String
     */
    public static String getPreferencesURL(APIGEEService apigeeService, String preferencesJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : preferencesJson;
    }

    /**
     * Method to get resource resolver from the system user
     *
     * @param resourceFactory resource resolver factory
     * @param paramMap        parameters map
     * @return ResourceResolver
     */
    public static ResourceResolver getResourceResolverFromSubService(
            final ResourceResolverFactory resourceFactory, final Map<String, Object> paramMap) {
        ResourceResolver resourceResolver = null;
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

    /**
     * Method to check the run mode development to execute the launch js for development environment -r dev
     *
     * @return boolean
     */
    public static boolean isRunModeDevelopment() {
        return isRunModeAvailable("dev");
    }

    /**
     * Method to check the run mode staging to execute the launch js for staging environment -r stage
     *
     * @return boolean
     */
    public static boolean isRunModeStaging() {
        return isRunModeAvailable("stage");
    }

    /**
     * Method to check the run mode staging to execute the launch js for production environment -r prod
     *
     * @return boolean
     */
    public static boolean isRunModeProduction() {
        return isRunModeAvailable("prod");
    }

    /**
     * Method to check rum mode available  - if available return true else false
     *
     * @param key String
     * @return boolean
     */
    public static boolean isRunModeAvailable(String key) {
        Set<String> runModesSet = getRunModes();
        if (runModesSet.contains(key)) {
            return true;
        } else {
            String runMode = System.getProperty("sling.run.modes");
            if (runMode != null && runMode.contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to get all available run modes
     *
     * @return Set<String>
     */
    public static Set<String> getRunModes() {
        return getService(SlingSettingsService.class).getRunModes();
    }

    /**
     * Method to get service
     *
     * @param clazz class type
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(final Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        return (T) bundleContext.getService(bundleContext.getServiceReference(clazz.getName()));
    }

    /**
     * This method provides the customer hub global config page path.
     *
     * @param contentPageResource content page resource
     * @return String global config page path
     */
    public static String getCustomerhubConfigPagePath(Resource contentPageResource) {
        String customerhubConfigPagePath = StringUtils.EMPTY;
        Page configPage = getCustomerhubConfigPage(contentPageResource);
        if (null != configPage) {
            customerhubConfigPagePath = configPage.getPath();
        }
        return customerhubConfigPagePath;
    }

    /**
     * This method provides the customer hub global config page.
     *
     * @param contentPageResource content page resource
     * @return Page global config
     */
    public static Page getCustomerhubConfigPage(Resource contentPageResource) {
        PageManager pageManager = contentPageResource.getResourceResolver().adaptTo(PageManager.class);
        Page customerhubConfigPage = null;
        final int DEPTH =3;
        if (null != contentPageResource && null != pageManager) {
            Page contentPage = pageManager.getContainingPage(contentPageResource);
            customerhubConfigPage = contentPage.getAbsoluteParent(DEPTH);
        }
        return customerhubConfigPage;
    }
    
    /**
     * @param request
     * @param prefix
     * @param key
     * @return
     */
    public static String getI18nValue(SlingHttpServletRequest request, String prefix, String key){
        I18n i18n = new I18n(request);
        return i18n.get(prefix+key);
    }

}
