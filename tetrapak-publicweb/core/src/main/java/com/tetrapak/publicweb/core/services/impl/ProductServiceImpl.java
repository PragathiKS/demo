package com.tetrapak.publicweb.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
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
@Designate(ocd = ProductServiceImpl.Config.class)
@Component(immediate = true, service = ProductService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ProductServiceImpl implements ProductService {
    
    @ObjectClassDefinition(name = "Public Web Product Import Config",
            description = "Public Web Product Import Config")
    public static @interface Config {

        /**
         * PXP DAM ROOT PATH
         *
         * @return dam root path
         */
        @AttributeDefinition(name = "DAM root Path")
        String damRootPath() default "/content/dam/tetrapak/products/pxp";

        /**
         * PXP Video Types
         *
         * @return video types
         */
        @AttributeDefinition(name = "Video Type Mapping", description = "types of video in PXP.Add comma seprated.")
        String videoTypes() default "mp4,webm,mpg,mp2,mpeg,mpe,mpv,ogg,m4p,m4v,avi,wmv,mov,qt,flv,swf,avchd";
    }

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /** The paths to replicate. */
    private List<String> pathToReplicate;
    
    /** The dam root path. */
    private String damRootPath;
    
    /** The video types. */
    private String videoTypes;

    /**
     * @param resolver
     * @param session
     * @param productType
     * @param fillingMachines
     * @param language
     * @param damRootPath
     * @param videoTypes
     * @return
     */
    @Override
    public List<String> createOrUpdateProductFillingMachine(ResourceResolver resolver, Session session,
            String productType, List<FillingMachine> fillingMachines, String language) {
        pathToReplicate = new ArrayList<>();
        try {
            Resource productTypeResource = ProductUtil.createProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                for (FillingMachine fillingMachine : fillingMachines) {
                    processFillingMachine(resolver, productTypeResPath, productType, fillingMachine, language,
                            damRootPath, videoTypes);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating filling machine", e);
        }
        return new ArrayList<>(pathToReplicate);
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
            String productPath = FillingMachineUtil.createOrUpdateFillingMachine(resolver, productType,
                    productTypeResPath, fillingMachine, language, damRootPath, videoTypes);
            pathToReplicate.add(productPath);
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
     * @return
     */
    @Override
    public List<String> createOrUpdateProductPackageType(ResourceResolver resolver, Session session, String productType,
            List<Packagetype> packageTypes, String language) {
        pathToReplicate = new ArrayList<>();
        try {
            Resource productTypeResource = ProductUtil.createProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                for (Packagetype packageType : packageTypes) {
                    String productPath = PackageTypeUtil.createOrUpdatePackageType(resolver, productType,
                            productTypeResPath, packageType, language, damRootPath, videoTypes);
                    pathToReplicate.add(productPath);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating package type", e);

        }
        return new ArrayList<>(pathToReplicate);
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
    public List<String> createOrUpdateProductProcessingEquipement(ResourceResolver resolver, Session session,
            String productType, List<ProcessingEquipement> equipments, String language) {
        pathToReplicate = new ArrayList<>();
        try {
            Resource equipementResource = ProductUtil.createProductRootResource(resolver, productType);
            if (equipementResource != null) {
                String equipementResourcePath = equipementResource.getPath();
                for (ProcessingEquipement equipment : equipments) {
                    String productPath = ProcessingEquipementUtil.createOrUpdateProcessingEquipements(resolver,
                            productType, equipementResourcePath, equipment, language, damRootPath, videoTypes);
                    pathToReplicate.add(productPath);
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating product node", e);

        }
        return new ArrayList<>(pathToReplicate);
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
    
    /**
     * @param config
     */
    @Activate
    protected void activate(final Config config) {
        damRootPath = config.damRootPath();
        videoTypes = config.videoTypes();
    }

    /**
     * @param config
     */
    @Modified
    protected void modified(final Config config) {
        activate(config);
    }

}
