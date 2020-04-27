package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.pxp.Category;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.Technology;
import com.tetrapak.publicweb.core.beans.pxp.TechnologyType;
import com.tetrapak.publicweb.core.constants.PWConstants;

public class ProcessingEquipementUtil extends ProductUtil {

    /**
     * @param resolver
     * @param productType
     * @param rootPath
     * @param equipment
     * @param language
     * @param damRootPath
     * @param videoTypes
     * @return product path
     * @throws PersistenceException
     */
    public static String createOrUpdateProcessingEquipements(ResourceResolver resolver, String productType,
            String rootPath, ProcessingEquipement equipment, String language, String damRootPath, String videoTypes)
            throws PersistenceException {
        String productID = equipment.getId();
        Resource equipmentsRes = createOrUpdateProductResource(resolver, rootPath, productID);
        String productPath = StringUtils.EMPTY;
        if (equipmentsRes != null) {
            productPath = equipmentsRes.getPath();
            ResourceUtil.deleteResource(resolver, productPath + PWConstants.SLASH + language);
            final Map<String, Object> langProperties = new HashMap<>();
            langProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            langProperties.put(PWConstants.NAME, equipment.getName());
            langProperties.put(PWConstants.HEADER, equipment.getHeader());
            langProperties.put(PWConstants.BENEFITS, equipment.getBenefits());
            langProperties.put(PWConstants.BENEFITS_IMAGE, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                    equipment.getBenefitsimage(), productType, productID, videoTypes));
            Resource langRes = ResourceUtil.createOrUpdateResource(resolver, productPath, language, langProperties);
            if (langRes != null) {
                createOrUpdateFeatureOrOpions(resolver, productType, productID, langRes.getPath(), PWConstants.FEATURES,
                        equipment.getFeatures(), damRootPath, videoTypes);
                createOrUpdateFeatureOrOpions(resolver, productType, productID, langRes.getPath(), PWConstants.OPTIONS,
                        equipment.getOptions(), damRootPath, videoTypes);
                createOrUpdateCategories(resolver, langRes.getPath(), equipment.getCategories());
                createOrUpdateTechnology(resolver, langRes.getPath(), equipment.getTechnology());
                createOrUpdateTechnologyTypes(resolver, langRes.getPath(), equipment.getTechnologyType());
            }
        }
        return productPath;
    }

    /**
     * @param resolver
     * @param rootPath
     * @param categories
     * @throws PersistenceException
     */
    public static void createOrUpdateCategories(ResourceResolver resolver, String rootPath, List<Category> categories)
            throws PersistenceException {
        if (categories != null && !categories.isEmpty()) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            Resource categoriesRes = ResourceUtil.createOrUpdateResource(resolver, rootPath, "categories", properties);
            if (categoriesRes != null) {
                int i = 1;
                for (Category category : categories) {
                    if (category != null) {
                        properties.put(PWConstants.ID, category.getId());
                        properties.put(PWConstants.NAME, category.getName());
                        ResourceUtil.createOrUpdateResource(resolver, categoriesRes.getPath(), String.valueOf(i),
                                properties);
                        i++;
                    }
                }
            }
        }
    }

    /**
     * @param resolver
     * @param rootPath
     * @param technology
     * @throws PersistenceException
     */
    public static void createOrUpdateTechnology(ResourceResolver resolver, String rootPath, Technology technology)
            throws PersistenceException {
        if (technology != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            properties.put(PWConstants.ID, technology.getId());
            properties.put(PWConstants.NAME, technology.getName());
            ResourceUtil.createOrUpdateResource(resolver, rootPath, "technology", properties);
        }
    }

    /**
     * @param resolver
     * @param rootPath
     * @param technologyType
     * @throws PersistenceException
     */
    public static void createOrUpdateTechnologyTypes(ResourceResolver resolver, String rootPath,
            TechnologyType technologyType) throws PersistenceException {
        if (technologyType != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            properties.put(PWConstants.ID, technologyType.getId());
            properties.put(PWConstants.NAME, technologyType.getName());
            ResourceUtil.createOrUpdateResource(resolver, rootPath, "technologytype", properties);
        }
    }

}
