package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;

/**
 * API GEE Service class
 */
public interface ProductService {

    void createProductRootIfNotExists(String fileType);

    void createProductFillingMachine(FillingMachine fillingMachine,String langauge);
    
    String getFileType(String fileURI);
    
    String getLanguage(String fileURI);
}

