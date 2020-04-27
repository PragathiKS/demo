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
     * @param productTypeResPath
     * @param fillingMachine
     * @param language
     * @return product path
     * @throws PersistenceException
     */
    public static String createOrUpdateFillingMachine(ResourceResolver resolver, String productTypeResPath,
            FillingMachine fillingMachine, String language) throws PersistenceException {
        Resource productRes = createOrUpdateProductResource(resolver, productTypeResPath, fillingMachine.getId());
        String productPath = StringUtils.EMPTY;
        if (productRes != null) {
            final Map<String, Object> properties = new HashMap<>();
            properties.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
            properties.put(PWConstants.BENEFITS, fillingMachine.getBenefits());
            properties.put(PWConstants.NAME, fillingMachine.getName());
            properties.put("benefitsimage", fillingMachine.getBenefitsimage());
            properties.put(PWConstants.HEADER, fillingMachine.getHeader());
            productPath = productRes.getPath();
            ResourceUtil.deleteResource(resolver, productPath + PWConstants.SLASH + language);
            Resource languageRes = ResourceUtil.createOrUpdateResource(resolver, productRes.getPath(), language,
                    properties);
            if (languageRes != null) {
                createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), PWConstants.FEATURES,
                        fillingMachine.getFeatures());
                createOrUpdateFeatureOrOpions(resolver, languageRes.getPath(), PWConstants.OPTIONS,
                        fillingMachine.getOptions());
                final Map<String, Object> propertiesPackageType = new HashMap<>();
                propertiesPackageType.put(PWConstants.JCR_PRIMARY_TYPE, PWConstants.NT_UNSTRUCTURED);
                Resource packageRootRes = ResourceUtil.createOrUpdateResource(resolver, languageRes.getPath(),
                        "packagetypes", propertiesPackageType);
                if (packageRootRes != null) {
                    createOrUpdatePackageTypesReferences(resolver, packageRootRes.getPath(),
                            fillingMachine.getPackagetypes());
                }
            }
        }
        return productPath;
    }

    /**
     * @param resolver
     * @param rootPath
     * @param packageTypes
     * @throws PersistenceException
     */
    public static void createOrUpdatePackageTypesReferences(ResourceResolver resolver, String rootPath,
            List<Packagetype> packageTypes) throws PersistenceException {
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
                        PackageTypeUtil.createOrUpdateShapes(resolver, packageTypeRes, packageType);

                    }
                }
            }
        }
    }
}
