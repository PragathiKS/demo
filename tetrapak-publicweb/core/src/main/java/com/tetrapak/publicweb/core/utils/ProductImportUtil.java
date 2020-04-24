package com.tetrapak.publicweb.core.utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.constants.PWConstants;

public final class ProductImportUtil {

    private static final String ROOT_PATH = "/var/commerce/products";

    private static final String PXP = "pxp";

    private static final String SLING_FOLDER = "sling:Folder";

    private static final String NT_UNSTRUCTURED = "nt:unstructured";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImportUtil.class);

    public static Resource createOrUpdateProductRootResource(ResourceResolver resolver, String productType) {
        String rootPath = ROOT_PATH + PWConstants.SLASH + PXP;
        Resource productTypeRes = null;
        try {
            final Map<String, Object> properties = new HashMap<>();
            properties.put("jcr:primaryType", SLING_FOLDER);
            properties.put("cq:commerceProvider", PXP);
            createOrUpdateResource(resolver, ROOT_PATH, PXP, properties);
            properties.remove("cq:commerceProvider");
            productTypeRes = createOrUpdateResource(resolver, rootPath, productType, properties);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating root product node", e);
        }
        return productTypeRes;
    }

    public static Resource createOrUpdateProductResource(ResourceResolver resolver, String productTypeResPath,
            String productId) throws PersistenceException {
        final Map<String, Object> productProperties = new HashMap<>();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        productProperties.put("jcr:primaryType", NT_UNSTRUCTURED);
        productProperties.put("sling:resourceType", "commerce/components/product");
        productProperties.put("cq:commerceType", "product");
        productProperties.put("jcr:lastModified", calendar);
        return createOrUpdateResource(resolver, productTypeResPath, productId, productProperties);
    }

    public static void createOrUpdateFeatureOrOpions(ResourceResolver resolver, String rootPath, String resourceName,
            List<FeatureOption> featureOptions) throws PersistenceException {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jcr:primaryType", NT_UNSTRUCTURED);
        createOrUpdateResource(resolver, rootPath, resourceName, properties);
        String featuresPath = rootPath + PWConstants.SLASH + resourceName;
        int i = 1;
        if (featureOptions != null) {
            for (FeatureOption featureOption : featureOptions) {
                if (featureOption != null) {
                    properties.put("name", featureOption.getName());
                    properties.put("header", featureOption.getHeader());
                    properties.put("body", featureOption.getBody());
                    properties.put("image", featureOption.getImage());
                    if (featureOption.getVideo() != null) {
                        properties.put("scr", featureOption.getVideo().getSrc());
                        properties.put("poster", featureOption.getVideo().getPoster());
                    }
                    createOrUpdateResource(resolver, featuresPath, String.valueOf(i), properties);
                    i++;
                }
            }
        }
    }

    public static void createOrUpdatePackageTypes(ResourceResolver resolver, String rootPath, String resourceName,
            List<Packagetype> packageTypes) throws PersistenceException {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jcr:primaryType", NT_UNSTRUCTURED);
        createOrUpdateResource(resolver, rootPath, resourceName, properties);
        String packagePath = rootPath + PWConstants.SLASH + resourceName;
        if (packageTypes != null) {
            for (Packagetype packageType : packageTypes) {
                createOrUpdatePackageType(resolver, packageType, properties, packagePath);
            }
        }
    }

    public static void createOrUpdatePackageType(ResourceResolver resolver, Packagetype packageType,
            Map<String, Object> properties, String packagePath) throws PersistenceException {
        if (packageType != null) {
            properties.put("id", packageType.getId());
            properties.put("name", packageType.getName());
            Resource packageTypeRes = createOrUpdateResource(resolver, packagePath, packageType.getId(), properties);
            if (packageTypeRes != null) {
                if (packageType.getShapes() != null && !packageType.getShapes().isEmpty()) {
                    createOrUpdateShapes(resolver, packageTypeRes, properties, packageType);
                }
            }
        }
    }

    public static void createOrUpdateShapes(ResourceResolver resolver, Resource packageTypeRes,
            Map<String, Object> properties, Packagetype packageType) throws PersistenceException {
        final Map<String, Object> shapesProperties = new HashMap<>();
        properties.put("jcr:primaryType", NT_UNSTRUCTURED);
        Resource shapeRes = createOrUpdateResource(resolver, packageTypeRes.getPath(), "shapes", shapesProperties);
        if (shapeRes != null) {
            int i = 1;
            for (Shape shape : packageType.getShapes()) {
                if (shape != null) {
                    properties.remove("id");
                    properties.put("name", shape.getName());
                    properties.put("header", shape.getThumbnail());
                    if (shape.getVolumes() != null && !shape.getVolumes().isEmpty()) {
                        properties.put("volumes", shape.getVolumes().toArray(new String[shape.getVolumes().size()]));
                    }
                    createOrUpdateResource(resolver, shapeRes.getPath(), String.valueOf(i), properties);
                    i++;
                }
            }
        }

    }

    public static Resource createOrUpdateResource(ResourceResolver resolver, String rootPath, String resourceName,
            Map<String, Object> properties) throws PersistenceException {
        final Resource rootResource = resolver.getResource(rootPath);
        properties.values().removeAll(Collections.singleton(null));
        Resource resource = resolver.getResource(rootPath + PWConstants.SLASH + resourceName);
        if (null != rootResource && null == resource) {
            resource = resolver.create(rootResource, resourceName, properties);
            resolver.commit();
        } else if (null != resource) {
            ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
            map.putAll(properties);
            resolver.commit();
        }
        return resource;
    }

}
