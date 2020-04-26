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
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Openingclosure;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.constants.PWConstants;

public final class ProductImportUtil {

    private static final String ROOT_PATH = "/var/commerce/products";

    private static final String PXP = "pxp";

    private static final String SLING_FOLDER = "sling:Folder";

    private static final String NT_UNSTRUCTURED = "nt:unstructured";
    
    private static final String JCR_PRIMARY_TYPE = "jcr:primaryType";
    
    private static final String HEADER = "header";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImportUtil.class);
    
    private ProductImportUtil(){
        // private constructor
    }

    public static Resource createOrUpdateProductRootResource(ResourceResolver resolver, String productType) {
        String rootPath = ROOT_PATH + PWConstants.SLASH + PXP;
        Resource productTypeRes = null;
        try {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, SLING_FOLDER);
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
        productProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        productProperties.put("sling:resourceType", "commerce/components/product");
        productProperties.put("cq:commerceType", "product");
        productProperties.put("jcr:lastModified", calendar);
        return createOrUpdateResource(resolver, productTypeResPath, productId, productProperties);
    }

    public static void createOrUpdateFeatureOrOpions(ResourceResolver resolver, String rootPath, String resourceName,
            List<FeatureOption> featureOptions) throws PersistenceException {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        createOrUpdateResource(resolver, rootPath, resourceName, properties);
        String featuresPath = rootPath + PWConstants.SLASH + resourceName;
        int i = 1;
        if (featureOptions != null) {
            for (FeatureOption featureOption : featureOptions) {
                if (featureOption != null) {
                    properties.put("name", featureOption.getName());
                    properties.put(HEADER, featureOption.getHeader());
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

    public static void createOrUpdateFillingMachine(ResourceResolver resolver, String productTypeResPath,
            FillingMachine fillingMachine, String language) throws PersistenceException {
        Resource productRes = ProductImportUtil.createOrUpdateProductResource(resolver, productTypeResPath,
                fillingMachine.getId());
        if (productRes != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            properties.put("benefits", fillingMachine.getBenefits());
            properties.put("name", fillingMachine.getName());
            properties.put("benefitsimage", fillingMachine.getBenefitsimage());
            properties.put(HEADER, fillingMachine.getHeader());
            Resource languageRes = ProductImportUtil.createOrUpdateResource(resolver, productRes.getPath(), language,
                    properties);
            if (languageRes != null) {
                ProductImportUtil.createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), "features",
                        fillingMachine.getFeatures());
                ProductImportUtil.createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), "options",
                        fillingMachine.getOptions());
                final Map<String, Object> propertiesPackageType = new HashMap<>();
                propertiesPackageType.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
                Resource packageRootRes = ProductImportUtil.createOrUpdateResource(resolver, languageRes.getPath(),
                        "packagetypes", propertiesPackageType);
                if (packageRootRes != null) {
                    ProductImportUtil.createOrUpdatePackageTypes(resolver, packageRootRes.getPath(),
                            fillingMachine.getPackagetypes(), properties);
                }
            }
        }
    }

    public static void createOrUpdatePackageTypes(ResourceResolver resolver, String rootPath,
            List<Packagetype> packageTypes, String language) throws PersistenceException {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        if (packageTypes != null) {
            for (Packagetype packageType : packageTypes) {
                properties.put("id", packageType.getId());
                Resource packageTypeRes = createOrUpdateResource(resolver, rootPath, packageType.getId(), properties);
                if (packageTypeRes != null) {
                    properties.remove("id");
                    properties.put("name", packageType.getName());
                    Resource langRes = createOrUpdateResource(resolver, packageTypeRes.getPath(), language, properties);
                    if (langRes != null) {
                        createOrUpdatePackageTypeProps(resolver, packageTypeRes, langRes, packageType, properties);
                    }
                }
            }
        }
    }
    
    public static void createOrUpdatePackageTypeProps(ResourceResolver resolver, Resource packageTypeRes,
            Resource langRes, Packagetype packageType, Map<String, Object> properties) throws PersistenceException {
        if (packageType.getShapes() != null && !packageType.getShapes().isEmpty()) {
            createOrUpdateShapes(resolver, packageTypeRes, properties, packageType);
        }
        if (packageType.getOpeningclosures() != null && !packageType.getOpeningclosures().isEmpty()) {
            createOrUpdateOpeningClosures(resolver, packageTypeRes, properties, packageType);
        }
        createorUpdateFillingMachines(resolver, packageType, langRes.getPath());
    }

    public static void createorUpdateFillingMachines(ResourceResolver resolver, Packagetype packageType,
            String langResPath) throws PersistenceException {
        if (packageType.getFillingmachines() != null && !packageType.getFillingmachines().isEmpty()) {
            Map<String, Object> fillingMachineProps = new HashMap<>();
            fillingMachineProps.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            Resource fillingMachineRootRes = createOrUpdateResource(resolver, langResPath, "fillingmachines",
                    fillingMachineProps);
            if (fillingMachineRootRes != null) {
                for (FillingMachine fillingMachine : packageType.getFillingmachines()) {
                    fillingMachineProps.put("id", fillingMachine.getId());
                    fillingMachineProps.put("name", fillingMachine.getName());
                    fillingMachineProps.put("thumbnail", fillingMachine.getThumbnail());
                    fillingMachineProps.put(HEADER, fillingMachine.getHeader());
                    createOrUpdateResource(resolver, fillingMachineRootRes.getPath(), fillingMachine.getId(),
                            fillingMachineProps);
                }
            }
        }
    }

    public static void createOrUpdatePackageTypes(ResourceResolver resolver, String rootPath,
            List<Packagetype> packageTypes, Map<String, Object> properties) throws PersistenceException {
        if (packageTypes != null) {
            for (Packagetype packageType : packageTypes) {
                if (packageType != null) {
                    properties.put("id", packageType.getId());
                    properties.put("name", packageType.getName());
                    Resource packageTypeRes = createOrUpdateResource(resolver, rootPath, packageType.getId(),
                            properties);
                    if (packageTypeRes != null && packageType.getShapes() != null
                            && !packageType.getShapes().isEmpty()) {
                        createOrUpdateShapes(resolver, packageTypeRes, properties, packageType);

                    }
                }
            }
        }
    }

    public static void createOrUpdateOpeningClosures(ResourceResolver resolver, Resource packageTypeRes,
            Map<String, Object> properties, Packagetype packageType) throws PersistenceException {
        final Map<String, Object> openingClouserProperties = new HashMap<>();
        openingClouserProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        Resource openingClouserRes = createOrUpdateResource(resolver, packageTypeRes.getPath(), "openingclousers",
                openingClouserProperties);
        if (openingClouserRes != null) {
            int i = 1;
            for (Openingclosure openingClousers : packageType.getOpeningclosures()) {
                if (openingClousers != null) {
                    properties.put("id", openingClousers.getId());
                    properties.put("name", openingClousers.getName());
                    properties.put("thumbnail", openingClousers.getThumbnail());
                    properties.put("type", openingClousers.getType());
                    properties.put("principle", openingClousers.getPrinciple());
                    if (openingClousers.getBenefits() != null && !openingClousers.getBenefits().isEmpty()) {
                        properties.put("benifits", openingClousers.getBenefits()
                                .toArray(new String[openingClousers.getBenefits().size()]));
                    }
                    createOrUpdateResource(resolver, openingClouserRes.getPath(), String.valueOf(i), properties);
                    i++;
                }
            }
        }

    }

    public static void createOrUpdateShapes(ResourceResolver resolver, Resource packageTypeRes,
            Map<String, Object> properties, Packagetype packageType) throws PersistenceException {
        final Map<String, Object> shapesProperties = new HashMap<>();
        shapesProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        Resource shapeRes = createOrUpdateResource(resolver, packageTypeRes.getPath(), "shapes", shapesProperties);
        if (shapeRes != null) {
            int i = 1;
            for (Shape shape : packageType.getShapes()) {
                if (shape != null) {
                    properties.remove("id");
                    properties.put("name", shape.getName());
                    properties.put(HEADER, shape.getThumbnail());
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
