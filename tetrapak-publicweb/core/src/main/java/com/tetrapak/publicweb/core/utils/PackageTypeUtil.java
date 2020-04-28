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

public class PackageTypeUtil extends ProductUtil {


    /**
     * @param resolver
     * @param productType
     * @param rootPath
     * @param packageType
     * @param language
     * @param damRootPath
     * @param videoTypes
     * @return product path
     * @throws PersistenceException
     */
    public static String createOrUpdatePackageType(ResourceResolver resolver, String productType, String rootPath,
            Packagetype packageType, String language, String damRootPath, String videoTypes)
            throws PersistenceException {
        String productPath = StringUtils.EMPTY;
        String productID = packageType.getId();
        Resource packageTypeRes = createOrUpdateProductResource(resolver, rootPath, packageType.getId());
        if (packageTypeRes != null) {
            productPath = packageTypeRes.getPath();
            ResourceUtil.deleteResource(resolver, productPath + PWConstants.SLASH + language);
            final Map<String, Object> langProperties = new HashMap<>();
            langProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            langProperties.put(PWConstants.NAME, packageType.getName());
            Resource langRes = ResourceUtil.createOrUpdateResource(resolver, productPath, language, langProperties);
            if (langRes != null) {
                createOrUpdatePackageTypeProps(resolver, productType, productID, langRes, packageType, damRootPath,
                        videoTypes);
            }
        }
        return productPath;
    }


    /**
     * @param resolver
     * @param productType
     * @param productID
     * @param langRes
     * @param packageType
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createOrUpdatePackageTypeProps(ResourceResolver resolver, String productType, String productID,
            Resource langRes, Packagetype packageType, String damRootPath, String videoTypes)
            throws PersistenceException {
        if (packageType.getShapes() != null && !packageType.getShapes().isEmpty()) {
            createOrUpdateShapes(resolver, productType, productID, langRes, packageType, damRootPath, videoTypes);
        }
        if (packageType.getOpeningclosures() != null && !packageType.getOpeningclosures().isEmpty()) {
            createOrUpdateOpeningClosures(resolver, productType, productID, langRes, packageType, damRootPath,
                    videoTypes);
        }
        createorUpdateFillingMachinesReferences(resolver, "fillingmachines", packageType, langRes.getPath(), damRootPath,
                videoTypes);
    }

    /**
     * @param resolver
     * @param productType
     * @param productID
     * @param rootRes
     * @param packageType
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createOrUpdateShapes(ResourceResolver resolver, String productType, String productID,
            Resource rootRes, Packagetype packageType, String damRootPath, String videoTypes)
            throws PersistenceException {
        final Map<String, Object> shapesProperties = new HashMap<>();
        shapesProperties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
        Resource shapeRes = ResourceUtil.createOrUpdateResource(resolver, rootRes.getPath(), "shapes",
                shapesProperties);
        if (shapeRes != null) {
            int i = 1;
            for (Shape shape : packageType.getShapes()) {
                if (shape != null) {
                    shapesProperties.remove(PWConstants.ID);
                    shapesProperties.put(PWConstants.NAME, shape.getName());
                    shapesProperties.put(PWConstants.THUMBNAIL, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                            shape.getThumbnail(), productType, productID, videoTypes));
                    if (shape.getVolumes() != null && !shape.getVolumes().isEmpty()) {
                        shapesProperties.put("volumes",
                                shape.getVolumes().toArray(new String[shape.getVolumes().size()]));
                    }
                    ResourceUtil.createOrUpdateResource(resolver, shapeRes.getPath(), String.valueOf(i),
                            shapesProperties);
                    i++;
                }
            }
        }
    }

    /**
     * @param resolver
     * @param productType
     * @param packageType
     * @param langResPath
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createorUpdateFillingMachinesReferences(ResourceResolver resolver, String productType,
            Packagetype packageType, String langResPath, String damRootPath, String videoTypes)
            throws PersistenceException {
        if (packageType.getFillingmachines() != null && !packageType.getFillingmachines().isEmpty()) {
            Map<String, Object> fillingMachineProps = new HashMap<>();
            fillingMachineProps.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            Resource fillingMachineRootRes = ResourceUtil.createOrUpdateResource(resolver, langResPath,
                    "fillingmachines", fillingMachineProps);
            if (fillingMachineRootRes != null) {
                for (FillingMachine fillingMachine : packageType.getFillingmachines()) {
                    fillingMachineProps.put(PWConstants.ID, fillingMachine.getId());
                    fillingMachineProps.put(PWConstants.NAME, fillingMachine.getName());
                    fillingMachineProps.put(PWConstants.THUMBNAIL, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                            fillingMachine.getThumbnail(), productType, fillingMachine.getId(), videoTypes));
                    fillingMachineProps.put(PWConstants.HEADER, fillingMachine.getHeader());
                    ResourceUtil.createOrUpdateResource(resolver, fillingMachineRootRes.getPath(),
                            fillingMachine.getId(), fillingMachineProps);
                }
            }
        }
    }

    /**
     * @param resolver
     * @param productType
     * @param productID
     * @param rootRes
     * @param packageType
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createOrUpdateOpeningClosures(ResourceResolver resolver, String productType, String productID,
            Resource rootRes, Packagetype packageType, String damRootPath, String videoTypes)
            throws PersistenceException {
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
                    openingClouserProperties.put(PWConstants.THUMBNAIL, processAndGetPXPAssetDAMPath(resolver,
                            damRootPath, openingClousers.getThumbnail(), productType, productID, videoTypes));
                    openingClouserProperties.put("type", openingClousers.getType());
                    openingClouserProperties.put("principle", openingClousers.getPrinciple());
                    if (openingClousers.getBenefits() != null && !openingClousers.getBenefits().isEmpty()) {
                        openingClouserProperties.put(PWConstants.BENEFITS, openingClousers.getBenefits()
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
