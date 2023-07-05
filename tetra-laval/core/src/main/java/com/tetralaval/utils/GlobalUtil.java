package com.tetralaval.utils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.settings.SlingSettingsService;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.services.DynamicMediaService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import java.util.*;
import org.apache.sling.api.resource.ResourceResolver;
/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {
    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);
    private GlobalUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }
    /**
     * Method to get service
     *
     * @param clazz
     *            class type
     * @return T
     */
    public static <T> T getService(final Class<T> clazz) {
        if (FrameworkUtil.getBundle(clazz) == null) {
            return null;
        }
        final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
        final ServiceReference serviceReference = bundleContext.getServiceReference(clazz.getName());
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
        return slingSettingsService.getRunModes().contains("publish");
    }

    /**
     * This method is used to get the string value from string Array in format of
     * {"key":"value","key1":"value1"}
     * @param stringArray
     * @param key
     * @return String value
     */
    public static String getKeyValueFromStringArray(final String stringArray, final String key){
        try {
            return new JSONObject(stringArray).get(key).toString();
        }catch (final JSONException exception) {
        }
        return StringUtils.EMPTY;
    }
    /**
     * get scene 7 video url.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param damVideoPath
     *            video path
     * @param dynamicMediaService
     *            dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(final ResourceResolver resourceResolver, final String damVideoPath,
                                               final DynamicMediaService dynamicMediaService) {
        String path = damVideoPath;
        if (StringUtils.isNotBlank(damVideoPath)) {
            path = dynamicMediaService.getVideoServiceUrl() + TLConstants.SLASH
                    + getScene7FileName(resourceResolver, damVideoPath);
        }
        return path;
    }
    /**
     * Gets the scene 7 file name.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param path
     *            the path
     * @return the scene 7 file name
     */
    public static String getScene7FileName(final ResourceResolver resourceResolver, final String path) {
        String fileName = StringUtils.EMPTY;
        final Resource resource = resourceResolver.getResource(path + "/jcr:content/metadata");
        if (Objects.nonNull(resource)) {
            final ValueMap properties = resource.getValueMap();
            fileName = properties.get("dam:scene7File", StringUtils.EMPTY);
        }
        return fileName;
    }
    /**
     * Set proper format date
     * @param dateString
     * @return
     */
    public static String formatDate(String dateString) {
        if (dateString != null && dateString.length() > 0 && dateString.contains("T")) {
            final String parsedDate = dateString.substring(0, dateString.indexOf("T"));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date d = null;
            if (parsedDate.length() > 0) {
                try {
                    d = formatter.parse(parsedDate);
                } catch (final Exception e) {
                    LOG.error("Error occurred while parsing date: {} ", e.getMessage(), e);
                }
            }
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return formatter.format(d);
        }
        return StringUtils.EMPTY;
    }
}

