package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.day.cq.commons.Externalizer;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import java.util.Optional;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;

/**
 * This is a global util class to access globally common utility methods
 *
 * @author Nitin Kumar
 */
public final class GlobalUtil {

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
     * get scene 7 video url
     *
     * @param damVideoPath
     *            video path
     * @param dynamicMediaService
     *            dynamic media service
     * @return video path from scene 7
     */
    public static String getVideoUrlFromScene7(final ResourceResolver resourceResolver, final String damVideoPath,
            final DynamicMediaService dynamicMediaService) {
        String path = damVideoPath;
        if(StringUtils.isNotBlank(damVideoPath)) {
            path = dynamicMediaService.getVideoServiceUrl() + PWConstants.SLASH + getScene7FileName(resourceResolver, damVideoPath);
        }
        return path;
    }

    /**
     * get scene 7 image url
     *
     * @param damImagePath
     *            image path
     * @param dynamicMediaService
     *            dynamic media service
     * @return image path from scene 7
     */
    public static String getImageUrlFromScene7(final ResourceResolver resourceResolver, final String damImagePath,
            final DynamicMediaService dynamicMediaService) {
        String path = damImagePath;
        if(StringUtils.isNotBlank(damImagePath)) {
            path = dynamicMediaService.getImageServiceUrl() + PWConstants.SLASH + getScene7FileName(resourceResolver, damImagePath);
        }
        return path;
    }

    /**
     * Gets the scene 7 file name.
     *
     * @param resourceResolver the resource resolver
     * @param path the path
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
     * @param paramMap
     *            Java Util Map
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
     * Global function which provides DAM asset path for PXP integration
     *
     * @param productId
     * @param categoryId
     * @param sourceurl
     * @return
     */
    public static String getDAMPath(final String damRootPath, final String sourceurl, final String categoryId, final String productId,
            final String videoTypes) {
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
     * Extract filename from given URL
     *
     * @param fileURL
     * @return
     */
    public static String getFileName(final String fileURL) {
        return fileURL.substring(fileURL.lastIndexOf('/') + 1, fileURL.length());
    }

    /**
     * Fetch Asset Type based on File extension depending on mapping
     *
     * @param sourceurl
     * @param videoTypes
     * @return
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
     * Fetch Asset Type based on File extension depending on mapping
     *
     * @param sourceurl
     * @param videoTypes
     * @return
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
     * @param request
     *            the request
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
     * method to check the if path is external or internal and appends html accordingly
     * method also uses externalizer
     * @param link
     * @param resourceResolver
     * @return String full path along with domain
     */
	public static String dotHtmlLink(String link, ResourceResolver resourceResolver) {
		String linkPath = link;
		Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
		if (null != linkPath) {
			String domainName = getExternalizerDomainNameBySiteRootPath(linkPath);
			if (startsWithAnySiteContentRootPath(linkPath)) {
				linkPath = externalizer.externalLink(resourceResolver, domainName,
						LinkUtils.sanitizeLink(linkPath, resourceResolver));
			} else if (!StringUtils.startsWith(linkPath, PWConstants.HTTP_PROTOCOL)
					&& !StringUtils.startsWith(linkPath, PWConstants.HTTPS_PROTOCOL)) {
				linkPath = externalizer.externalLink(resourceResolver, domainName,
						LinkUtils.sanitizeLink(linkPath, resourceResolver));
			} else if (StringUtils.startsWith(linkPath, PWConstants.HTTP_PROTOCOL)
					|| StringUtils.startsWith(linkPath, PWConstants.HTTPS_PROTOCOL)) {
				linkPath = link;
			} else if (StringUtils.startsWith(linkPath, PWConstants.WWW)) {
				linkPath = PWConstants.HTTP_PROTOCOL + linkPath;
			}
		} else {
			linkPath = StringUtils.EMPTY;
		}
		return linkPath;
	}

	public static String getExternalizerDomainNameBySiteRootPath(final String contentPath) {
		String domainName = getMatchedTagPathBySiteRootPathPrefix(contentPath,
				PWConstants.siteRootPathConfigForExternalizer);
		return StringUtils.isNotBlank(domainName) ? domainName : PWConstants.EXTERNALIZER_DOMAIN_PUBLICWEB;
	}

	public static String getMatchedTagPathBySiteRootPathPrefix(String contentPath,
			final Map<String, String> tagPathConfigs) {
		String tagPath = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(contentPath)) {
			for (Map.Entry<String, String> marketTagEntry : tagPathConfigs.entrySet()) {
				if (contentPath.startsWith(marketTagEntry.getKey())) {
					tagPath = marketTagEntry.getValue();
					break;
				}
			}
		}
		return tagPath;
	}

	public static boolean startsWithAnySiteContentRootPath(final String contentPath) {
		return getSiteRootPath(contentPath).isPresent();
	}

	private static Optional<String> getSiteRootPath(String contentPath) {
		return PWConstants.siteRootPathConfig.stream().filter(contentPath::startsWith).findFirst();
	}

}
