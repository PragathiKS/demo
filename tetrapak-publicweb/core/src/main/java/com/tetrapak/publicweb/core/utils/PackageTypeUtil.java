package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Openingclosure;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Shape;
import com.tetrapak.publicweb.core.constants.PWConstants;

public class PackageTypeUtil extends ProductUtil{
    

    /**
     * @param resolver
     * @param rootPath
     * @param packageType
     * @param language
     * @return product path
     * @throws PersistenceException
     */
    public static String createOrUpdatePackageType(ResourceResolver resolver, String rootPath, Packagetype packageType,
            String language) throws PersistenceException {
        String productPath=StringUtils.EMPTY;
        Resource packageTypeRes = createOrUpdateProductResource(resolver, rootPath, packageType.getId());
        if (packageTypeRes != null) {
            productPath = packageTypeRes.getPath();
            final Map<String, Object> langProperties = new HashMap<>();
            langProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            langProperties.put(PWConstants.NAME, packageType.getName());
            Resource langRes = ResourceUtil.createOrUpdateResource(resolver, productPath, language, langProperties);
            if (langRes != null) {
                createOrUpdatePackageTypeProps(resolver, langRes, packageType);
            }
        }
        return productPath;
    }

    /**
     * @param resolver
     * @param langRes
     * @param packageType
     * @throws PersistenceException
     */
    public static void createOrUpdatePackageTypeProps(ResourceResolver resolver, Resource langRes,
            Packagetype packageType) throws PersistenceException {
        if (packageType.getShapes() != null && !packageType.getShapes().isEmpty()) {
            createOrUpdateShapes(resolver, langRes, packageType);
        }
        if (packageType.getOpeningclosures() != null && !packageType.getOpeningclosures().isEmpty()) {
            createOrUpdateOpeningClosures(resolver, langRes, packageType);
        }
        createorUpdateFillingMachinesReferences(resolver, packageType, langRes.getPath());
    }
    
    /**
     * @param resolver
     * @param rootRes
     * @param packageType
     * @throws PersistenceException
     */
    public static void createOrUpdateShapes(ResourceResolver resolver, Resource rootRes, Packagetype packageType)
            throws PersistenceException {
        final Map<String, Object> shapesProperties = new HashMap<>();
        shapesProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
        Resource shapeRes = ResourceUtil.createOrUpdateResource(resolver, rootRes.getPath(), "shapes", shapesProperties);
        if (shapeRes != null) {
            int i = 1;
            for (Shape shape : packageType.getShapes()) {
                if (shape != null) {
                    shapesProperties.remove(PWConstants.ID);
                    shapesProperties.put(PWConstants.NAME, shape.getName());
                    shapesProperties.put(PWConstants.HEADER, shape.getThumbnail());
                    if (shape.getVolumes() != null && !shape.getVolumes().isEmpty()) {
                        shapesProperties.put("volumes",
                                shape.getVolumes().toArray(new String[shape.getVolumes().size()]));
                    }
                    ResourceUtil.createOrUpdateResource(resolver, shapeRes.getPath(), String.valueOf(i), shapesProperties);
                    i++;
                }
            }
        }
    }
    
    /**
     * @param resolver
     * @param packageType
     * @param langResPath
     * @throws PersistenceException
     */
    public static void createorUpdateFillingMachinesReferences(ResourceResolver resolver, Packagetype packageType,
            String langResPath) throws PersistenceException {
        if (packageType.getFillingmachines() != null && !packageType.getFillingmachines().isEmpty()) {
            Map<String, Object> fillingMachineProps = new HashMap<>();
            fillingMachineProps.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            Resource fillingMachineRootRes = ResourceUtil.createOrUpdateResource(resolver, langResPath, "fillingmachines",
                    fillingMachineProps);
            if (fillingMachineRootRes != null) {
                for (FillingMachine fillingMachine : packageType.getFillingmachines()) {
                    fillingMachineProps.put(PWConstants.ID, fillingMachine.getId());
                    fillingMachineProps.put(PWConstants.NAME, fillingMachine.getName());
                    fillingMachineProps.put(PWConstants.THUMBNAIL, fillingMachine.getThumbnail());
                    fillingMachineProps.put(PWConstants.HEADER, fillingMachine.getHeader());
                    ResourceUtil.createOrUpdateResource(resolver, fillingMachineRootRes.getPath(), fillingMachine.getId(),
                            fillingMachineProps);
                }
            }
        }
    }
    
    /**
     * @param resolver
     * @param rootRes
     * @param packageType
     * @throws PersistenceException
     */
    public static void createOrUpdateOpeningClosures(ResourceResolver resolver, Resource rootRes,
            Packagetype packageType) throws PersistenceException {
        final Map<String, Object> openingClouserProperties = new HashMap<>();
        openingClouserProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
        Resource openingClouserRes = ResourceUtil.createOrUpdateResource(resolver, rootRes.getPath(), "openingclousers",
                openingClouserProperties);
        if (openingClouserRes != null) {
            int i = 1;
            for (Openingclosure openingClousers : packageType.getOpeningclosures()) {
                if (openingClousers != null) {
                    openingClouserProperties.put(PWConstants.ID, openingClousers.getId());
                    openingClouserProperties.put(PWConstants.NAME, openingClousers.getName());
                    openingClouserProperties.put(PWConstants.THUMBNAIL, openingClousers.getThumbnail());
                    openingClouserProperties.put("type", openingClousers.getType());
                    openingClouserProperties.put("principle", openingClousers.getPrinciple());
                    if (openingClousers.getBenefits() != null && !openingClousers.getBenefits().isEmpty()) {
                        openingClouserProperties.put("benifits", openingClousers.getBenefits()
                                .toArray(new String[openingClousers.getBenefits().size()]));
                    }
                    ResourceUtil.createOrUpdateResource(resolver, openingClouserRes.getPath(), String.valueOf(i),
                            openingClouserProperties);
                    i++;
                }
            }
        }

    }
}
