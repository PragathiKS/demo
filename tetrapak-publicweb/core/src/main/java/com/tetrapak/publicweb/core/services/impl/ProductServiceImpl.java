package com.tetrapak.publicweb.core.services.impl;


import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.ProductImportUtil;

/**
 * Impl class for API GEE Service
 * 
 * @author Sandip Kumar
 */
@Component(immediate = true, service = ProductService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final String FILLING_MACHINE = "fillingmachines";

    private static final String PROCESSING_EQUIPEMENT = "processingequipments";

    private static final String PACKAGE_TYPE = "packagetypes";

    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resolver;

    private Session session;

    @Override
    public void createProductFillingMachine(String productType, List<FillingMachine> fillingMachines, String language) {
        setResourceResolver();
        if (resolver == null) {
            LOGGER.info("Sytem User Session is null");
            return;
        }
        try {
            Resource productTypeResource = ProductImportUtil.createOrUpdateProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                for (FillingMachine fillingMachine : fillingMachines) {
                    if (fillingMachine != null && StringUtils.isNotBlank(fillingMachine.getId())) {
                        ProductImportUtil.createOrUpdateFillingMachine(resolver,productTypeResPath,fillingMachine,language);
                    }
                }
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating root product node", e);
        } finally {
            if (resolver.isLive()) {
                resolver.close();
            }
            if (session != null && session.isLive()) {
                session.logout();
            }
        }

    }

    @Override
    public void createProductPackageType(String productType, List<Packagetype> packageTypes, String language) {
        setResourceResolver();
        if (resolver == null) {
            LOGGER.info("Sytem User Session is null");
            return;
        }
        try {
            Resource productTypeResource = ProductImportUtil.createOrUpdateProductRootResource(resolver, productType);
            if (productTypeResource != null) {
                String productTypeResPath = productTypeResource.getPath();
                ProductImportUtil.createOrUpdatePackageTypes(resolver, productTypeResPath, packageTypes, language);
            }
            saveSession(session);
        } catch (PersistenceException e) {
            LOGGER.error("PersistenceException while creating root product node", e);
        } finally {
            if (resolver.isLive()) {
                resolver.close();
            }
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
    }

    @Override
    public void createProductProcessingEquipement(String productType, List<ProcessingEquipement> equipements,
            String langauge) {
        //TO DO
    }


    @Override
    public String getLanguage(String fileURI) {
        return fileURI.substring(fileURI.lastIndexOf('/') + 1).split("_")[1].replaceAll(".json", "");
    }

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
     * Sets the resource resolver.
     */
    private void setResourceResolver() {
        this.resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
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
