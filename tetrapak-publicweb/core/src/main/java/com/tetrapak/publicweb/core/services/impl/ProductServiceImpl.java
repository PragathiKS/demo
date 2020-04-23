package com.tetrapak.publicweb.core.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.services.ProductService;

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

    @Override
    public void createProductRootIfNotExists(String fileType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createProductFillingMachine(FillingMachine fillingMachine, String langauge) {
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
    
    
}
