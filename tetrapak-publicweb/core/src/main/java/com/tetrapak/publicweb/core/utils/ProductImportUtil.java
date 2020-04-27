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

import com.tetrapak.publicweb.core.beans.pxp.Category;
import com.tetrapak.publicweb.core.beans.pxp.FeatureOption;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Openingclosure;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.beans.pxp.Technology;
import com.tetrapak.publicweb.core.beans.pxp.TechnologyType;
import com.tetrapak.publicweb.core.constants.PWConstants;

public final class ProductImportUtil {

    private static final String ROOT_PATH = "/var/commerce/products";

    private static final String PXP = "pxp";

    private static final String SLING_FOLDER = "sling:Folder";

    private static final String NT_UNSTRUCTURED = "nt:unstructured";
    
    private static final String JCR_PRIMARY_TYPE = "jcr:primaryType";
    
    private static final String HEADER = "header";

    private static final String ID = "id";

    private static final String NAME = "name";

    private static final String BODY = "body";

    private static final String THUMBNAIL = "thumbnail";

    private static final String BENEFITS = "benefits";

    private static final String IMAGE = "image";

    private static final String COMMERCE_PROVIDER = "cq:commerceProvider";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImportUtil.class);

    private ProductImportUtil() {
        // private constructor
    }

    public static Resource createOrUpdateProductRootResource(ResourceResolver resolver, String productType) {
        String rootPath = ROOT_PATH + PWConstants.SLASH + PXP;
        Resource productTypeRes = null;
        try {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, SLING_FOLDER);
            properties.put(COMMERCE_PROVIDER, PXP);
            createOrUpdateResource(resolver, ROOT_PATH, PXP, properties);
            properties.remove(COMMERCE_PROVIDER);
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
        productProperties.put("id", productId);
        return createOrUpdateResource(resolver, productTypeResPath, productId, productProperties);
    }

    public static void createOrUpdateFeatureOrOpions(ResourceResolver resolver, String rootPath, String resourceName,
            List<FeatureOption> featureOptions) throws PersistenceException {
        if (featureOptions != null && !featureOptions.isEmpty()) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            createOrUpdateResource(resolver, rootPath, resourceName, properties);
            String featuresPath = rootPath + PWConstants.SLASH + resourceName;
            int i = 1;
            for (FeatureOption featureOption : featureOptions) {
                if (featureOption != null) {
                    properties.put(NAME, featureOption.getName());
                    properties.put(HEADER, featureOption.getHeader());
                    properties.put(BODY, featureOption.getBody());
                    properties.put(IMAGE, featureOption.getImage());
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
        Resource productRes = createOrUpdateProductResource(resolver, productTypeResPath, fillingMachine.getId());
        if (productRes != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            properties.put(BENEFITS, fillingMachine.getBenefits());
            properties.put(NAME, fillingMachine.getName());
            properties.put("benefitsimage", fillingMachine.getBenefitsimage());
            properties.put(HEADER, fillingMachine.getHeader());
            Resource languageRes = createOrUpdateResource(resolver, productRes.getPath(), language, properties);
            if (languageRes != null) {
                createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), "features",
                        fillingMachine.getFeatures());
                createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), "options", fillingMachine.getOptions());
                final Map<String, Object> propertiesPackageType = new HashMap<>();
                propertiesPackageType.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
                Resource packageRootRes = createOrUpdateResource(resolver, languageRes.getPath(), "packagetypes",
                        propertiesPackageType);
                if (packageRootRes != null) {
                    createOrUpdatePackageTypes(resolver, packageRootRes.getPath(), fillingMachine.getPackagetypes());
                }
            }
        }
    }
    
    public static void createOrUpdateCategories(ResourceResolver resolver, String rootPath,List<Category> categories) throws PersistenceException {
        if (categories != null && !categories.isEmpty()) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            Resource categoriesRes = createOrUpdateResource(resolver, rootPath, "categories",
                    properties);
            if(categoriesRes != null) {
                int i = 1;
                for(Category category:categories) {
                  if(category != null) {
                      properties.put(ID, category.getId());
                      properties.put(NAME, category.getName());
                      createOrUpdateResource(resolver, categoriesRes.getPath(), String.valueOf(i), properties);
                      i++;
                  }
                }
            }
        }
    }
    
    public static void createOrUpdateTechnology(ResourceResolver resolver, String rootPath,Technology technology) throws PersistenceException {
        if (technology != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            properties.put(ID, technology.getId());
            properties.put(NAME, technology.getName());
            createOrUpdateResource(resolver, rootPath, "technology",
                    properties);
        } 
    }
    
    public static void createOrUpdateTechnologyTypes(ResourceResolver resolver, String rootPath,TechnologyType technologyType) throws PersistenceException {
        if (technologyType != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            properties.put(ID, technologyType.getId());
            properties.put(NAME, technologyType.getName());
            createOrUpdateResource(resolver, rootPath, "technologytype",
                    properties);
        }  
    }

    public static void createOrUpdateProcessingEquipements(ResourceResolver resolver, String rootPath,
            List<ProcessingEquipement> equipments, String language) throws PersistenceException {
        if (equipments != null && !equipments.isEmpty()) {
            for (ProcessingEquipement equipment : equipments) {
                Resource equipmentsRes = createOrUpdateProductResource(resolver, rootPath, equipment.getId());
                if (equipmentsRes != null) {
                    final Map<String, Object> langProperties = new HashMap<>();
                    langProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
                    langProperties.put(NAME, equipment.getName());
                    langProperties.put(HEADER, equipment.getHeader());
                    langProperties.put(BENEFITS, equipment.getBenefits());
                    Resource langRes = createOrUpdateResource(resolver, equipmentsRes.getPath(), language,
                            langProperties);
                    if (langRes != null) {
                        createOrUpdateFeatureOrOpions(resolver, langRes.getPath(), "features", equipment.getFeatures());
                        createOrUpdateFeatureOrOpions(resolver, langRes.getPath(), "options", equipment.getOptions());
                        createOrUpdateCategories(resolver, langRes.getPath(), equipment.getCategories());
                        createOrUpdateTechnology(resolver, langRes.getPath(), equipment.getTechnology());
                        createOrUpdateTechnologyTypes(resolver, langRes.getPath(), equipment.getTechnologyType());
                    }
                }
            }
        }
    }

    public static void createOrUpdatePackageTypes(ResourceResolver resolver, String rootPath,
            List<Packagetype> packageTypes, String language) throws PersistenceException {
        if (packageTypes != null) {
            for (Packagetype packageType : packageTypes) {
                Resource packageTypeRes = createOrUpdateProductResource(resolver, rootPath, packageType.getId());
                if (packageTypeRes != null) {
                    final Map<String, Object> langProperties = new HashMap<>();
                    langProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
                    langProperties.put(NAME, packageType.getName());
                    Resource langRes = createOrUpdateResource(resolver, packageTypeRes.getPath(), language,
                            langProperties);
                    if (langRes != null) {
                        createOrUpdatePackageTypeProps(resolver, langRes, packageType);
                    }
                }
            }
        }
    }

    public static void createOrUpdatePackageTypeProps(ResourceResolver resolver, Resource langRes,
            Packagetype packageType) throws PersistenceException {
        if (packageType.getShapes() != null && !packageType.getShapes().isEmpty()) {
            createOrUpdateShapes(resolver, langRes, packageType);
        }
        if (packageType.getOpeningclosures() != null && !packageType.getOpeningclosures().isEmpty()) {
            createOrUpdateOpeningClosures(resolver, langRes, packageType);
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
                    fillingMachineProps.put(ID, fillingMachine.getId());
                    fillingMachineProps.put(NAME, fillingMachine.getName());
                    fillingMachineProps.put(THUMBNAIL, fillingMachine.getThumbnail());
                    fillingMachineProps.put(HEADER, fillingMachine.getHeader());
                    createOrUpdateResource(resolver, fillingMachineRootRes.getPath(), fillingMachine.getId(),
                            fillingMachineProps);
                }
            }
        }
    }

    public static void createOrUpdatePackageTypes(ResourceResolver resolver, String rootPath,
            List<Packagetype> packageTypes) throws PersistenceException {
        if (packageTypes != null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
            for (Packagetype packageType : packageTypes) {
                if (packageType != null) {
                    properties.put(ID, packageType.getId());
                    properties.put(NAME, packageType.getName());
                    Resource packageTypeRes = createOrUpdateResource(resolver, rootPath, packageType.getId(),
                            properties);
                    if (packageTypeRes != null && packageType.getShapes() != null
                            && !packageType.getShapes().isEmpty()) {
                        createOrUpdateShapes(resolver, packageTypeRes, packageType);

                    }
                }
            }
        }
    }

    public static void createOrUpdateOpeningClosures(ResourceResolver resolver, Resource rootRes,
            Packagetype packageType) throws PersistenceException {
        final Map<String, Object> openingClouserProperties = new HashMap<>();
        openingClouserProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        Resource openingClouserRes = createOrUpdateResource(resolver, rootRes.getPath(), "openingclousers",
                openingClouserProperties);
        if (openingClouserRes != null) {
            int i = 1;
            for (Openingclosure openingClousers : packageType.getOpeningclosures()) {
                if (openingClousers != null) {
                    openingClouserProperties.put(ID, openingClousers.getId());
                    openingClouserProperties.put(NAME, openingClousers.getName());
                    openingClouserProperties.put(THUMBNAIL, openingClousers.getThumbnail());
                    openingClouserProperties.put("type", openingClousers.getType());
                    openingClouserProperties.put("principle", openingClousers.getPrinciple());
                    if (openingClousers.getBenefits() != null && !openingClousers.getBenefits().isEmpty()) {
                        openingClouserProperties.put("benifits", openingClousers.getBenefits()
                                .toArray(new String[openingClousers.getBenefits().size()]));
                    }
                    createOrUpdateResource(resolver, openingClouserRes.getPath(), String.valueOf(i),
                            openingClouserProperties);
                    i++;
                }
            }
        }

    }

    public static void createOrUpdateShapes(ResourceResolver resolver, Resource rootRes, Packagetype packageType)
            throws PersistenceException {
        final Map<String, Object> shapesProperties = new HashMap<>();
        shapesProperties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        Resource shapeRes = createOrUpdateResource(resolver, rootRes.getPath(), "shapes", shapesProperties);
        if (shapeRes != null) {
            int i = 1;
            for (Shape shape : packageType.getShapes()) {
                if (shape != null) {
                    shapesProperties.remove(ID);
                    shapesProperties.put(NAME, shape.getName());
                    shapesProperties.put(HEADER, shape.getThumbnail());
                    if (shape.getVolumes() != null && !shape.getVolumes().isEmpty()) {
                        shapesProperties.put("volumes",
                                shape.getVolumes().toArray(new String[shape.getVolumes().size()]));
                    }
                    createOrUpdateResource(resolver, shapeRes.getPath(), String.valueOf(i), shapesProperties);
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
