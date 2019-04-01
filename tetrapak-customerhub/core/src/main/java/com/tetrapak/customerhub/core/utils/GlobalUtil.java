package com.tetrapak.customerhub.core.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.services.APIGEEService;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import java.util.Set;

/**
 * 
 * @author TetraPak
 *
 */
public class GlobalUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);

    /**
     * 
     * @param apigeeService
     * @param defaultJson
     * @return String
     */
    public static String getApiURL(APIGEEService apigeeService, String defaultJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : defaultJson;
    }
    /**
     * 
     * @param apigeeService
     * @param preferencesJson
     * @return String
     */
    public static String getPreferencesURL(APIGEEService apigeeService, String preferencesJson) {
        return null != apigeeService ? apigeeService.getApigeeServiceUrl() : preferencesJson;
    }
    /**
     * 
     * @param resourceFactory
     * @param paramMap
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
     * 
     * @param resp
     * @param jsonResponse
     * @throws IOException
     */
    public static void writeJsonResponse(SlingHttpServletResponse resp, JsonObject jsonResponse) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());
    }
    /**
     * 
     * @return boolean
     * @description To check the run mode development to execute the launch js for development environment -r dev
     */
    public static boolean isRunModeDevelopment(){
		return isRunModeAvailable("dev");
	}

    /**
     * 
     * @return boolean
     * @description To check the run mode staging to execute the launch js for staging environment -r stage
     */
	public static boolean isRunModeStaging(){
		return isRunModeAvailable("stage");
	}
	/**
	 * 
	 * @return boolean
	 * @description To check the run mode staging to execute the launch js for production environment -r prod
	 */
	public static boolean isRunModeProduction(){
		return isRunModeAvailable("prod");
	}
    
    /**
     * 
     * @param key String
     * @return boolean
     * @description To check rum mode available  - if available return true else false
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
     * 
     * @return Set<String>
     * @description To get available run modes
     */
	public static Set<String> getRunModes() {
    	return	getService(SlingSettingsService.class).getRunModes();
	}
    /**
     * 
     * @param clazz
     * @return T
     */    
	@SuppressWarnings("unchecked")
    public static <T> T getService(final Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        return (T) bundleContext.getService(bundleContext.getServiceReference(clazz.getName()));
    }
	
	/**
	 * The method provides the customer hub globalconfig page path. 
	 * @param contentPageResource
	 * @return String globalconfig page path
	 */
	public static String getCustomerhubConfigPagePath(Resource contentPageResource) {
		String customerhubConfigPagePath = StringUtils.EMPTY;
		if (null != contentPageResource) {
			customerhubConfigPagePath = getCustomerhubConfigPage(contentPageResource).getPath();
		}
		return customerhubConfigPagePath;		
	}
	
	/**
	 * The method provides the customer hub globalconfig page.
	 * 
	 * @param contentPageResource
	 * @return Page globalconfig
	 */
	public static Page getCustomerhubConfigPage(Resource contentPageResource) {
		PageManager pageManager = contentPageResource.getResourceResolver().adaptTo(PageManager.class);
		Page customerhubConfigPage = null;
		if (null != contentPageResource && null != pageManager) {
			Page contentPage = pageManager.getContainingPage(contentPageResource);
			customerhubConfigPage = contentPage.getAbsoluteParent(3);
		}
		return customerhubConfigPage;
	}

}
