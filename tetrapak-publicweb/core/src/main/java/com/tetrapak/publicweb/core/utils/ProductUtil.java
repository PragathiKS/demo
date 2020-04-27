package com.tetrapak.publicweb.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.constants.PWConstants;

public class ProductUtil {

    protected ProductUtil() {
        // Only Sub Class can use Product Util Object
    }

    /** LOGGER */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductUtil.class);

    /**
     * @param resolver
     * @param productType
     * @return product root resource
     * @throws PersistenceException
     */
    public static Resource createProductRootResource(ResourceResolver resolver, String productType)
            throws PersistenceException {
        String rootPath = PWConstants.ROOT_PATH + PWConstants.SLASH + PWConstants.PXP;
        Resource productTypeRes = null;
        final Map<String, Object> properties = new HashMap<>();
        properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.SLING_FOLDER);
        properties.put(PWConstants.COMMERCE_PROVIDER, PWConstants.PXP);
        ResourceUtil.createResource(resolver, PWConstants.ROOT_PATH, PWConstants.PXP, properties);
        properties.remove(PWConstants.COMMERCE_PROVIDER);
        productTypeRes = ResourceUtil.createResource(resolver, rootPath, productType, properties);
        return productTypeRes;
    }

    /**
     * @param resolver
     * @param productTypeResPath
     * @param productId
     * @return product resource
     * @throws PersistenceException
     */
    public static Resource createOrUpdateProductResource(ResourceResolver resolver, String productTypeResPath,
            String productId) throws PersistenceException {
        final Map<String, Object> productProperties = new HashMap<>();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        productProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
        productProperties.put("sling:resourceType", "commerce/components/product");
        productProperties.put("cq:commerceType", "product");
        productProperties.put("jcr:lastModified", calendar);
        productProperties.put(PWConstants.ID, productId);
        return ResourceUtil.createOrUpdateResource(resolver, productTypeResPath, productId, productProperties);
    }

    /**
     * @param resolver
     * @param productType
     * @param productID
     * @param rootPath
     * @param resourceName
     * @param featureOptions
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createOrUpdateFeatureOrOpions(ResourceResolver resolver, String productType, String productID,
            String rootPath, String resourceName, List<FeatureOption> featureOptions, String damRootPath,
            String videoTypes) throws PersistenceException {
        if (featureOptions != null && !featureOptions.isEmpty()) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            ResourceUtil.createOrUpdateResource(resolver, rootPath, resourceName, properties);
            String featuresPath = rootPath + PWConstants.SLASH + resourceName;
            int i = 1;
            for (FeatureOption featureOption : featureOptions) {
                if (featureOption != null) {
                    properties.put(PWConstants.NAME, featureOption.getName());
                    properties.put(PWConstants.HEADER, featureOption.getHeader());
                    properties.put(PWConstants.BODY, featureOption.getBody());
                    properties.put(PWConstants.IMAGE, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                            featureOption.getImage(), productType, productID, videoTypes));
                    if (featureOption.getVideo() != null) {
                        properties.put(PWConstants.SRC, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                                featureOption.getVideo().getSrc(), productType, productID, videoTypes));
                        properties.put(PWConstants.POSTER, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                                featureOption.getVideo().getPoster(), productType, productID, videoTypes));
                    }
                    ResourceUtil.createOrUpdateResource(resolver, featuresPath, String.valueOf(i), properties);
                    i++;
                }
            }
        }
    }

    /**
     * @param resolver
     * @param jobMgr
     * @param damRootPath
     * @param sourceurl
     * @param categoryId
     * @param productId
     * @param videoTypes
     * @return PXP Asset Dam Path
     */
    public static String processAndGetPXPAssetDAMPath(ResourceResolver resolver, String damRootPath, String sourceurl,
            String productType, String productId, String videoTypes) {
        String damPath = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(sourceurl)) {
            damPath = GlobalUtil.getDAMPath(damRootPath, sourceurl, productType, productId, videoTypes);
            if (StringUtils.isNotBlank(damPath) && resolver.getResource(damPath) == null) {
                // Download & Activate PXP Asset
                JobManager jobMgr = GlobalUtil.getService(JobManager.class);
                if (jobMgr != null) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("sourceurl", sourceurl);
                    properties.put("finalDAMPath", damPath);
                    jobMgr.addJob("pxp/dam/assets/create", properties);
                } else {
                    LOGGER.error("JobManager Reference null in processPXPAssetDAMPath");
                }
            } else {
                LOGGER.debug("Unable to download PXP Asset as {} either exists or empty", damPath);
            }
        }
        return damPath;
    }

}
