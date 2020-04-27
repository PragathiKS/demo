package com.tetrapak.publicweb.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.constants.PWConstants;

public class ProductUtil {
    
    protected ProductUtil() {
       //Only Sub Class can use Product Util Object 
    }

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
     * @param rootPath
     * @param resourceName
     * @param featureOptions
     * @throws PersistenceException
     */
    public static void createOrUpdateFeatureOrOpions(ResourceResolver resolver, String rootPath, String resourceName,
            List<FeatureOption> featureOptions) throws PersistenceException {
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
                    properties.put(PWConstants.IMAGE, featureOption.getImage());
                    if (featureOption.getVideo() != null) {
                        properties.put(PWConstants.SRC, featureOption.getVideo().getSrc());
                        properties.put(PWConstants.POSTER, featureOption.getVideo().getPoster());
                    }
                    ResourceUtil.createOrUpdateResource(resolver, featuresPath, String.valueOf(i), properties);
                    i++;
                }
            }
        }
    }

}
