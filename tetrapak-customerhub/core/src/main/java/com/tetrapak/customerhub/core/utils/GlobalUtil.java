package com.tetrapak.customerhub.core.utils;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
     * Method to get API Mappings
     *
     * @param apigeeService API GEE Service
     * @return String array
     */
    public static String[] getApiMappings(APIGEEService apigeeService) {
        return null != apigeeService ? apigeeService.getApiMappings() : null;
    }

    /**
     * Method to get a particular endpoint e.g. order-details-parts
     *
     * @param apigeeService API GEE Service
     * @param prefix        String prefix
     * @return String value
     */
    public static String getSelectedApiMapping(APIGEEService apigeeService, String prefix) {
        String[] mappings = getApiMappings(apigeeService);
        String mappingValue = StringUtils.EMPTY;
        for (String mapping : mappings) {
            if (prefix.equalsIgnoreCase(StringUtils.substringBefore(mapping, ":"))) {
                mappingValue = StringUtils.substringAfter(mapping, ":");
                break;
            }
        }
        return mappingValue;
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
     * Method to check the run mode QA to execute the launch js for QA environment -r test
     *
     * @return boolean
     */
    public static boolean isRunModeQA() {
        return isRunModeAvailable("qa");
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
     * The method provides the i18n value provided the following parameters.
     *
     * @param request request
     * @param prefix  prefix
     * @param key     key
     * @return value
     */
    public static String getI18nValue(SlingHttpServletRequest request, String prefix, String key) {
        I18n i18n = new I18n(request);
        return i18n.get(prefix + key);
    }

    /**
     * The method returns title of the page provided the resource.
     *
     * @param resource Resource
     * @return String page title
     */
    public static String getPageTitle(Resource resource) {
        if (null == resource) {
            return "";
        }
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if (null == pageManager) {
            return "";
        }
        Page currentPage = pageManager.getContainingPage(resource);
        return currentPage.getTitle();
    }

    /**
     * The method returns page depth of the page provided the resource.
     *
     * @param res Resource
     * @return depth
     */
    public static int getPageDepth(Resource res) {
        PageManager pageMgr = res.getResourceResolver().adaptTo(PageManager.class);
        if (null == pageMgr) {
            return 0;
        }
        Page currentPage = pageMgr.getContainingPage(res);
        return currentPage.getDepth();
    }

    /**
     * Method to set page reference
     *
     * @param resourceResolver    resource resolver
     * @param componentsReference component reference
     * @param locale              locale
     * @param pageContentPath     page component path
     */
    public static void setPageReferences(ResourceResolver resourceResolver, List<String> componentsReference,
                                         String locale, String pageContentPath) {
        locale = StringUtils.isNotBlank(locale) ? locale : "en";
        String pagePath = String.valueOf(pageContentPath);
        pagePath = pagePath.replace("/en", CustomerHubConstants.PATH_SEPARATOR + locale);
        String resGridPathWithoutJcrContent = CustomerHubConstants.PATH_SEPARATOR
                + CustomerHubConstants.ROOT_NODE + CustomerHubConstants.PATH_SEPARATOR +
                CustomerHubConstants.RESPONSIVE_GRID_NODE;
        String resGridPath = pagePath.endsWith(JcrConstants.JCR_CONTENT) ? resGridPathWithoutJcrContent :
                CustomerHubConstants.PATH_SEPARATOR + JcrConstants.JCR_CONTENT + resGridPathWithoutJcrContent;
        pagePath = pagePath + resGridPath;
        GlobalUtil.pageReferenceComponents(resourceResolver, componentsReference, pagePath);
    }

    /**
     * Method to set component references
     *
     * @param resourceResolver    resource resolver
     * @param componentsReference component reference
     * @param path                path
     */
    public static void pageReferenceComponents(ResourceResolver resourceResolver, List<String> componentsReference, String path) {
        Resource componentResources = resourceResolver.getResource(path);
        if (Objects.nonNull(componentResources)) {
            Iterator<Resource> iterators = componentResources.listChildren();
            while (iterators.hasNext()) {
                componentsReference.add(iterators.next().getPath());
            }
        }
    }

}
