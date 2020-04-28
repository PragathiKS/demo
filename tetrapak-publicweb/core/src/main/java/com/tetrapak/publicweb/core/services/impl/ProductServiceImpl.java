package com.tetrapak.publicweb.core.services.impl;

import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.utils.FillingMachineUtil;
import com.tetrapak.publicweb.core.utils.PackageTypeUtil;
import com.tetrapak.publicweb.core.utils.ProcessingEquipementUtil;
import com.tetrapak.publicweb.core.utils.ProductUtil;

/**
 * Impl class for API GEE Service
 * 
 * @author Sandip Kumar
 */
@Component(immediate = true, service = ProductService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ProductServiceImpl implements ProductService {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /** The filling machines. */
    private static final String FILLING_MACHINE = "fillingmachines";

    /** The processing equipments. */
    private static final String PROCESSING_EQUIPEMENT = "processingequipments";

    /** The package types. */
    private static final String PACKAGE_TYPE = "packagetypes";

    /**
     * @param resolver
     * @param session
     * @param productType
     * @param fillingMachines
     * @param language
     * @param damRootPath
     * @param videoTypes
     */
    @Override
    public void createProductFillingMachine(ResourceResolver resolver, Session session, String productType,
            List<FillingMachine> fillingMachines, String language, String damRootPath, String videoTypes) {
        try {
            Resource productTypeResource = ProductUtil.createProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                for (FillingMachine fillingMachine : fillingMachines) {
                    processFillingMachine(resolver, productTypeResPath, productTypeResPath, fillingMachine, language,
                            damRootPath, videoTypes);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating filling machine", e);
        }
    }
    
    /**
     * @param resolver
     * @param productTypeResPath
     * @param productType
     * @param fillingMachine
     * @param language
     * @param damRootPath
     * @param videoTypes
     * @throws PersistenceException
     */
    private void processFillingMachine(ResourceResolver resolver, String productTypeResPath, String productType,
            FillingMachine fillingMachine, String language, String damRootPath, String videoTypes)
            throws PersistenceException {
        if (fillingMachine != null && StringUtils.isNotBlank(fillingMachine.getId())) {
            FillingMachineUtil.createOrUpdateFillingMachine(resolver, productType, productTypeResPath, fillingMachine,
                    language, damRootPath, videoTypes);

        }
    }

    /**
     * @param resolver
     * @param session
     * @param productType
     * @param packageTypes
     * @param language
     * @param damRootPath
     * @param videoTypes
     */
    @Override
    public void createProductPackageType(ResourceResolver resolver, Session session, String productType,
            List<Packagetype> packageTypes, String language, String damRootPath, String videoTypes) {
        try {
            Resource productTypeResource = ProductUtil.createProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                for (Packagetype packageType : packageTypes) {
                    PackageTypeUtil.createOrUpdatePackageType(resolver, productType, productTypeResPath, packageType,
                            language, damRootPath, videoTypes);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating package type", e);

        }
    }

    /**
     * @param resolver
     * @param session
     * @param productType
     * @param equipments
     * @param language
     * @param damRootPath
     * @param videoTypes
     */
    @Override
    public void createProductProcessingEquipement(ResourceResolver resolver, Session session, String productType,
            List<ProcessingEquipement> equipments, String language, String damRootPath, String videoTypes) {
        try {
            Resource equipementResource = ProductUtil.createProductRootResource(resolver, productType);
            if (equipementResource != null) {
                String equipementResourcePath = equipementResource.getPath();
                for (ProcessingEquipement equipment : equipments) {
                    ProcessingEquipementUtil.createOrUpdateProcessingEquipements(resolver, productType,
                            equipementResourcePath, equipment, language, damRootPath, videoTypes);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating product node", e);

        }
    }

    /**
     * return language
     */
    @Override
    public String getLanguage(String fileURI) {
        return fileURI.substring(fileURI.lastIndexOf('/') + 1).split("_")[1].replaceAll(".json", "");
    }

    /**
     * return file type.
     */
    @Override
    public String getFileType(String fileURI) {
        String fileType = StringUtils.EMPTY;
        if (fileURI.contains(PROCESSING_EQUIPEMENT)) {
            fileType = PROCESSING_EQUIPEMENT;
        }
        if (fileURI.contains(FILLING_MACHINE)) {
            fileType = FILLING_MACHINE;
        }
        if (fileURI.contains(PACKAGE_TYPE)) {
            fileType = PACKAGE_TYPE;
        }
        return fileType;
    }

    /**
     * Save session.
     * 
     * @param session
     *            the session
     */
    private static void saveSession(final Session session) {
        if (session != null && session.isLive()) {
            try {
                session.save();
            } catch (final RepositoryException e) {
                LOGGER.error("Error saving session", e);
            }
        }

    }

}
