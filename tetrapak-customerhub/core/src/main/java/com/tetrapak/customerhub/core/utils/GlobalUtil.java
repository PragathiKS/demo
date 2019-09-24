package com.tetrapak.customerhub.core.utils;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.beans.ImageBean;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.services.SiteImproveScriptService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.jcr.Session;
import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public class GlobalUtil {

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
     * Method to check the run mode development to execute the launch js for
     * development environment -r dev
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
     * @return boolean
     */
    public static boolean isRunModeStaging() {
        return isRunModeAvailable("stage");
    }

    /**
     * Method to check the run mode staging to execute the launch js for production
     * environment -r prod
     *
     * @return boolean
     */
    public static boolean isRunModeProduction() {
        return isRunModeAvailable("prod");
    }

    /**
     * Method to check rum mode available - if available return true else false
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
        SlingSettingsService slingSettingsService = getService(SlingSettingsService.class);
        if(null == slingSettingsService){
            return Collections.emptySet();
        }
        return slingSettingsService.getRunModes();
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
        ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());
        if(null == serviceReference){
            return null;
        }
        return (T) bundleContext.getService(serviceReference);
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
     * The method provides the i18n value provided the following parameters.
     *
     * @param request  request
     * @param prefix   prefix
     * @param key      key
     * @param language language
     * @return value
     */
    public static String getI18nValueForThisLanguage(SlingHttpServletRequest request, String prefix, String key, String language) {
        I18n i18n = new I18n(request.getResourceBundle(new Locale(language.substring(0,2), StringUtils.substringAfter(language,"_"))));
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
        locale = StringUtils.isNotBlank(locale) ? locale : CustomerHubConstants.DEFAULT_LOCALE;
        String pagePath = String.valueOf(pageContentPath);
        pagePath = pagePath.replace(CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.DEFAULT_LOCALE,
                CustomerHubConstants.PATH_SEPARATOR + locale);
        String resGridPathWithoutJcrContent = CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.ROOT_NODE
                + CustomerHubConstants.PATH_SEPARATOR + CustomerHubConstants.RESPONSIVE_GRID_NODE;
        String resGridPath = pagePath.endsWith(JcrConstants.JCR_CONTENT) ? resGridPathWithoutJcrContent
                : CustomerHubConstants.PATH_SEPARATOR + JcrConstants.JCR_CONTENT + resGridPathWithoutJcrContent;
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
    private static void pageReferenceComponents(ResourceResolver resourceResolver, List<String> componentsReference,
                                                String path) {
        Resource componentResources = resourceResolver.getResource(path);
        if (Objects.nonNull(componentResources)) {
            Iterator<Resource> iterators = componentResources.listChildren();
            while (iterators.hasNext()) {
                componentsReference.add(iterators.next().getPath());
            }
        }
    }

    /**
     * This method is used to get language page for the current resource
     *
     * @param request               current request
     * @param userPreferenceService user preference service
     * @return language page
     */
    public static Page getLanguagePage(SlingHttpServletRequest request, UserPreferenceService userPreferenceService) {
        String language = getSelectedLanguage(request, userPreferenceService);
        if (StringUtils.isEmpty(language)) {
            language = CustomerHubConstants.DEFAULT_LOCALE;
        }
        Resource checkResource = request.getResourceResolver().getResource("/content/tetrapak/customerhub/content-components/" + language);
        if (null == checkResource || ResourceUtil.isNonExistingResource(checkResource)) {
            language = CustomerHubConstants.DEFAULT_LOCALE;
        }
        Resource languagePageResource = request.getResourceResolver().getResource
                ("/content/tetrapak/customerhub/content-components/" + language);
        if (null == languagePageResource) {
            return null;
        }
        return languagePageResource.adaptTo(Page.class);
    }

    /**
     * Method to get selected language
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
            return userPreferenceService.getSavedPreferences(session.getUserID(), CustomerHubConstants.LANGUGAGE_PREFERENCES);
        }
        return null;
    }

    /**
     * This method prepares image bean from image resource
     *
     * @param imageResource image resource
     * @return image bean
     */
    public static ImageBean getImageBean(Resource imageResource) {
        ValueMap imgValueMap = imageResource.getValueMap();
        ImageBean imageBean = new ImageBean();
        imageBean.setImagePath((String) imgValueMap.get("fileReference"));
        imageBean.setAltText((String) imgValueMap.get("alt"));
        imageBean.setDheight((String) imgValueMap.get("dheight"));
        imageBean.setDwidth((String) imgValueMap.get("dwidth"));
        imageBean.setMheightl((String) imgValueMap.get("mheightl"));
        imageBean.setMwidthl((String) imgValueMap.get("mwidthl"));
        imageBean.setMheightp((String) imgValueMap.get("mheightp"));
        imageBean.setMwidthp((String) imgValueMap.get("mwidthp"));
        imageBean.setImageCrop((String) imgValueMap.get("imageCrop"));
        return imageBean;
    }

    /**
     * This method is used to get image resource from inside a child element of a multi-field
     *
     * @param res       Current Resource from a multi-field
     * @param imageName image node name
     * @return image resource
     */
    public static Resource getImageResource(Resource res, String imageName) {

        Resource listResource = res.getParent();
        if (null == listResource) {
            return null;
        }
        Resource tabResource = listResource.getParent();
        if (null == tabResource) {
            return null;
        }
        return tabResource.getChild(imageName);
    }

    /**
     * Method to get global config resource for a resource
     *
     * @param childResource resource
     * @return global config resource
     */
    public static Resource getGlobalConfigurationResource(Resource childResource) {
        Resource res = childResource.getChild("globalconfiguration");
        if (null != res) {
            return res;
        } else {
            Iterator<Resource> itr = childResource.listChildren();
            while (itr.hasNext()) {
                Resource nextResource = itr.next();
                if (nextResource.isResourceType(CustomerHubConstants.GLOBAL_CONFIGURATION_RESOURCE_TYPE)) {
                    return nextResource;
                }
            }
        }
        return null;
    }

    /**
     * Method to get global config resource for a request
     *
     * @param request sling request
     * @return global config resource
     */
    public static Resource getGlobalConfigurationResource(SlingHttpServletRequest request) {
        Resource childResource = request.getResourceResolver().getResource(
                GlobalUtil.getCustomerhubConfigPagePath(request.getResource()) + "/jcr:content/root/responsivegrid");
        if (null != childResource) {
            return getGlobalConfigurationResource(childResource);
        }
        return null;
    }

    /**
     * Method to get language from cookie
     *
     * @param request sling request
     * @return language
     */
    public static String getLanguage(SlingHttpServletRequest request) {
        Cookie languageCookie = request.getCookie("lang-code");
        if (null != languageCookie) {
            return languageCookie.getValue();
        }
        return CustomerHubConstants.DEFAULT_LOCALE;
    }

    /**
     * @return site improve script
     */
    public static String getSiteImproveScript() {
        SiteImproveScriptService siteImproveScriptService = getService(SiteImproveScriptService.class);
        if(null == siteImproveScriptService){
            return null;
        }
        return siteImproveScriptService.getSiteImproveScriptUrl();
    }

    /**
     * get scene 7 video url
     *
     * @param damVideoPath        video path
     * @param dynamicMediaService dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(String damVideoPath, DynamicMediaService dynamicMediaService) {
        damVideoPath = StringUtils.substringBeforeLast(damVideoPath, ".");
        damVideoPath = StringUtils.substringAfterLast(damVideoPath, CustomerHubConstants.PATH_SEPARATOR);
        damVideoPath = dynamicMediaService.getVideoServiceUrl() + dynamicMediaService.getRootPath()
                + CustomerHubConstants.PATH_SEPARATOR + damVideoPath;
        return damVideoPath;
    }

    /**
     * Get a name with special characters replaced by underscore
     *
     * @param name tab title
     * @return valid name
     */
    public static String getValidName(String name) {
        return JcrUtil.createValidName(name);
    }

    /**
     * Removes images which are no longer attached to any tab
     *
     * @param resource  resource
     * @param listName  list name the node which is not to be removed
     * @param imageList image list
     * @throws PersistenceException persistence exception
     */
    public static void cleanUpImages(Resource resource, String listName, List<String> imageList) throws PersistenceException {
        ResourceResolver resourceResolver = resource.getResourceResolver();
        Iterator<Resource> itr = resource.listChildren();
        while (itr.hasNext()) {
            Resource resource1 = itr.next();
            if (listName.equals(resource1.getName())) {
                continue;
            }
            if (!imageList.contains(resource1.getName())) {
                resourceResolver.delete(resource1);
                resourceResolver.commit();
            }
        }
    }
}
