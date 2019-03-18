package com.tetrapak.customerhub.core.utils;

import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.customerhub.core.services.APIGEEService;

public class GlobalUtil {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);

    public static String getApiURL(APIGEEService apiJeeService, String defaultJson) {
        return null != apiJeeService ? apiJeeService.getApigeeServiceUrl() : defaultJson;
    }

    public static String getPreferencesURL(APIGEEService apiJeeService, String preferencesJson) {
        return null != apiJeeService ? apiJeeService.getApigeeServiceUrl() : preferencesJson;
    }

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
//  To check the runmode development to execute the launch js for development  environment -r development
    public static boolean isRunModeDevelopment(){
		return isRunModeAvailable("development");
	}

//  To check the runmode staging to execute the launch js for staging environment -r staging
	public static boolean isRunModeStaging(){
		return isRunModeAvailable("staging");
	}
//  To check the runmode staging to execute the launch js for production environment -r production
	public static boolean isRunModeProduction(){
		return isRunModeAvailable("production");
	}
    
// To check rum mode availble  - if available return true else false    
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
    // To get available run modes
	public static Set<String> getRunModes() {
    	return	getService(SlingSettingsService.class).getRunModes();
	}
    
	@SuppressWarnings("unchecked")
    public static <T> T getService(final Class<T> clazz) {
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        return (T) bundleContext.getService(bundleContext.getServiceReference(clazz.getName()));
    }


}
