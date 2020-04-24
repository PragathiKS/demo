package com.tetrapak.publicweb.core.services.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.commons.jcr.JcrUtil;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.utils.GlobalUtil;

/**
 * Impl class for API GEE Service
 * @author Sandip Kumar
 */
@Component(immediate = true, service = ProductService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ProductServiceImpl implements ProductService {

    
    private static final Logger LOGGER =  LoggerFactory.getLogger(ProductServiceImpl.class);
      
    private static final String FILLING_MACHINE = "fillingmachines";
    
    private static final String PROCESSING_EQUIPEMENT = "processingequipments";
    
    private static final String PACKAGE_TYPE = "packagetypes";
    
    private static final String ROOT_PATH = "/var/commerce/products/pxp/";
    
    private static final String SLING_FOLDER = "sling:Folder";
    
    @Reference
    private ResourceResolverFactory resolverFactory;
    
    private ResourceResolver resourceResolver;

    @Override
    public void createProductRootIfNotExists(String fileType) {
        try {
            JcrUtil.createPath(ROOT_PATH, SLING_FOLDER, resourceResolver.adaptTo(Session.class));
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void createProductFillingMachine(FillingMachine fillingMachine, String langauge) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void createProductPackageType(Packagetype packageType, String langauge) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void createProductProcessingEquipement(ProcessingEquipement equipement, String langauge) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public String getLanguage(String fileURI) {
        return fileURI.substring(fileURI.lastIndexOf('/')+1).split("_")[1].replaceAll(".json", "");
    }
    
    @Override
    public String getFileType(String fileURI) {
        String fileType = StringUtils.EMPTY;
        if(fileURI.contains(PROCESSING_EQUIPEMENT)) {
            fileType = PROCESSING_EQUIPEMENT;
        }
        if(fileURI.contains(FILLING_MACHINE)) {
            fileType = FILLING_MACHINE;
        }
        if(fileURI.contains(PACKAGE_TYPE)) {
            fileType = PACKAGE_TYPE;
        }
        return fileType;           
    }
    
    /**
     * Sets the resource resolver.
     */
    private void setResourceResolver() {       
        this.resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);
    }    
    
}
