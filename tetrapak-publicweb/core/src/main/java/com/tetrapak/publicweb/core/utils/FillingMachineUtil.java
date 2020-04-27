package com.tetrapak.publicweb.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tetrapak.publicweb.core.constants.PWConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;

public final class FillingMachineUtil extends ProductUtil {


    /**
     * @param resolver
     * @param productType
     * @param productTypeResPath
     * @param fillingMachine
     * @param language
     * @param damRootPath
     * @param videoTypes
     * @return product path
     * @throws PersistenceException
     */
    public static String createOrUpdateFillingMachine(ResourceResolver resolver, String productType,
            String productTypeResPath, FillingMachine fillingMachine, String language, String damRootPath,
            String videoTypes) throws PersistenceException {
        String productID = fillingMachine.getId();
        Resource productRes = createOrUpdateProductResource(resolver, productTypeResPath, fillingMachine.getId());
        String productPath = StringUtils.EMPTY;
        if (productRes != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            properties.put(PWConstants.BENEFITS, fillingMachine.getBenefits());
            properties.put(PWConstants.NAME, fillingMachine.getName());
            properties.put(PWConstants.BENEFITS_IMAGE, processAndGetPXPAssetDAMPath(resolver, damRootPath,
                    fillingMachine.getBenefitsimage(), productType, productID, videoTypes));
            properties.put(PWConstants.HEADER, fillingMachine.getHeader());
            productPath = productRes.getPath();
            ResourceUtil.deleteResource(resolver, productPath + PWConstants.SLASH + language);
            Resource languageRes = ResourceUtil.createOrUpdateResource(resolver, productRes.getPath(), language,
                    properties);
            if (languageRes != null) {
                createOrUpdateFeatureOrOpions(resolver, productType, productID, languageRes.getPath(),
                        PWConstants.FEATURES, fillingMachine.getFeatures(), damRootPath, videoTypes);
                createOrUpdateFeatureOrOpions(resolver, productType, productID, languageRes.getPath(),
                        PWConstants.OPTIONS, fillingMachine.getOptions(), damRootPath, videoTypes);
                final Map<String, Object> propertiesPackageType = new HashMap<>();
                propertiesPackageType.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
                Resource packageRootRes = ResourceUtil.createOrUpdateResource(resolver, languageRes.getPath(),
                        "packagetypes", propertiesPackageType);
                if (packageRootRes != null) {
                    createOrUpdatePackageTypesReferences(resolver, productType, packageRootRes.getPath(),
                            fillingMachine.getPackagetypes(), damRootPath, videoTypes);
                }
            }
        }
        return productPath;
    }

    /**
     * @param resolver
     * @param productType
     * @param rootPath
     * @param packageTypes
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    public static void createOrUpdatePackageTypesReferences(ResourceResolver resolver, String productType,
            String rootPath, List<Packagetype> packageTypes, String damRootPath, String videoTypes)
            throws PersistenceException {
        if (packageTypes != null) {
            Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            for (Packagetype packageType : packageTypes) {
                if (packageType != null) {
                    properties.put(PWConstants.ID, packageType.getId());
                    properties.put(PWConstants.NAME, packageType.getName());
                    Resource packageTypeRes = ResourceUtil.createOrUpdateResource(resolver, rootPath,
                            packageType.getId(), properties);
                    if (packageTypeRes != null && packageType.getShapes() != null
                            && !packageType.getShapes().isEmpty()) {
                        PackageTypeUtil.createOrUpdateShapes(resolver, productType, packageType.getId(), packageTypeRes,
                                packageType, damRootPath, videoTypes);

                    }
                }
            }
        }
    }
}
