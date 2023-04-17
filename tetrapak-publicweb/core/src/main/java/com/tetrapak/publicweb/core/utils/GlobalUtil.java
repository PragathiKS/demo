package com.tetrapak.publicweb.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.FormConstants;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;

/**
 * This is a global util class to access globally common utility methods.
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalUtil.class);    

    /**
     * Instantiates a new global util.
     */
    private GlobalUtil() {
        /*
         * adding a private constructor to hide the implicit one
         */
    }

    /**
     * Method to get service.
     *
     * @param <T>
     *            the generic type
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
            path = dynamicMediaService.getVideoServiceUrl() + PWConstants.SLASH
                    + getScene7FileName(resourceResolver, damVideoPath);
        }
        return path;
    }

    /**
     * get scene 7 image url.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param damImagePath
     *            image path
     * @param dynamicMediaService
     *            dynamic media service
     * @return image path from scene 7
     */
    public static String getImageUrlFromScene7(final ResourceResolver resourceResolver, final String damImagePath,
            final DynamicMediaService dynamicMediaService) {
        String path = damImagePath;
        if (StringUtils.isNotBlank(damImagePath)) {
            path = dynamicMediaService.getImageServiceUrl() + PWConstants.SLASH
                    + getScene7FileName(resourceResolver, damImagePath);
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
     * This method returns service resolver based on parameter map.
     *
     * @param resourceFactory
     *            ResourceResolverFactory
     * @return sling resource resolver
     */
    public static ResourceResolver getResourceResolverFromSubService(final ResourceResolverFactory resourceFactory) {
        ResourceResolver resourceResolver = null;
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "tetrapak-system-user");
        try {
            resourceResolver = resourceFactory.getServiceResourceResolver(paramMap);
        } catch (final LoginException e) {
            LOG.error("Unable to fetch resourceResolver for subservice {} exception {}",
                    paramMap.get(ResourceResolverFactory.SUBSERVICE), e.getMessage(), e);
        }
        return resourceResolver;
    }

    /**
     * Global function which provides DAM asset path for PXP integration.
     *
     * @param damRootPath
     *            the dam root path
     * @param sourceurl
     *            the sourceurl
     * @param categoryId
     *            the category id
     * @param productId
     *            the product id
     * @param videoTypes
     *            the video types
     * @return the DAM path
     */
    public static String getDAMPath(final String damRootPath, final String sourceurl, final String categoryId,
            final String productId, final String videoTypes) {
        String finalDAMPath = null;
        final String assetType = getAssetType(sourceurl, videoTypes);
        final String fileName = getFileName(sourceurl);

        if (!StringUtils.isEmpty(assetType) && !StringUtils.isEmpty(categoryId) && !StringUtils.isEmpty(productId)
                && !StringUtils.isEmpty(fileName)) {
            finalDAMPath = new StringBuffer(damRootPath).append(PWConstants.SLASH).append(categoryId)
                    .append(PWConstants.SLASH).append(productId).append(PWConstants.SLASH).append(assetType)
                    .append(PWConstants.SLASH).append(fileName).toString();
        } else {
            LOG.error(
                    "One of the mandatory input not provided for DAM path \n Asset Type {}  \nCategoryId  {} \nProductId {} \nfileName {}",
                    assetType, categoryId, productId, fileName);
        }
        return finalDAMPath;

    }

    /**
     * Extract filename from given URL.
     *
     * @param fileURL
     *            the file URL
     * @return the file name
     */
    public static String getFileName(final String fileURL) {
        return fileURL.substring(fileURL.lastIndexOf('/') + 1, fileURL.length());
    }

    /**
     * Fetch Asset Type based on File extension depending on mapping.
     *
     * @param sourceurl
     *            the sourceurl
     * @param contentTypeMapping
     *            the content type mapping
     * @return the asset content type
     */
    public static String getAssetContentType(final String sourceurl, final String[] contentTypeMapping) {
        String contenType = PWConstants.APPLICATION_OCTET_STREAM;
        final String fileExtension = sourceurl.substring(sourceurl.lastIndexOf('.') + 1, sourceurl.length());
        LOG.debug("fileExtension {}", fileExtension);
        for (final String mapping : contentTypeMapping) {
            if (mapping.contains(fileExtension)) {
                contenType = mapping.split("=")[0];
                break;
            }
        }
        LOG.debug("asset Type {}", contenType);
        return contenType;
    }

    /**
     * Fetch Asset Type based on File extension depending on mapping.
     *
     * @param sourceurl
     *            the sourceurl
     * @param videoTypes
     *            the video types
     * @return the asset type
     */
    public static String getAssetType(final String sourceurl, final String videoTypes) {
        String assetType = PWConstants.IMAGE;
        final String fileExtension = sourceurl.substring(sourceurl.lastIndexOf('.') + 1, sourceurl.length());
        LOG.debug("fileExtension {}", fileExtension);
        if (videoTypes.contains(fileExtension)) {
            assetType = PWConstants.VIDEO;
        }
        return assetType;
    }

    /**
     * Fetch config resource.
     *
     * @param resource
     *            the resource
     * @param configPath
     *            the config path
     * @return the resource
     */
    public static Resource fetchConfigResource(final Resource resource, final String configPath) {
        final PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        final Page page = pageManager.getContainingPage(resource);
        final String rootPath = LinkUtils.getRootPath(page.getPath());
        final String path = new StringBuffer(rootPath).append(configPath).toString();
        return resource.getResourceResolver().getResource(path);
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
     * This method is used to get the string value from string Array in format of {"key":"value","key1":"value1"}.
     *
     * @param stringArray
     *            the string array
     * @param key
     *            the key
     * @return String value
     */
    public static String getKeyValueFromStringArray(final String stringArray, final String key) {
        try {
            return new JSONObject(stringArray).get(key).toString();
        } catch (final JSONException exception) {
            LOG.error("JSONException while converting array string to json object: ", exception);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Checks if is china data flow.
     *
     * @param request
     *            the request
     * @return true, if is china data flow
     */
    public static boolean isChinaDataFlow(final SlingHttpServletRequest request) {
        boolean isChina = Boolean.FALSE;
        String marketPath = LinkUtils.getCountryPath(request.getPathInfo());
        final String country = StringUtils.substringAfterLast(marketPath, PWConstants.SLASH);
        if (country.equalsIgnoreCase(PWConstants.CHINA_COUNTRY_CODE)
                || (request.getParameterMap().get(FormConstants.COUNTRY)[0]).equalsIgnoreCase(PWConstants.CHINA)) {
            isChina = Boolean.TRUE;
        }
        return isChina;
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
