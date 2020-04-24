package com.tetrapak.publicweb.core.services;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class
 */
public interface ProductService {

    void createProductRootIfNotExists(String fileType);

    void createProductFillingMachine(FillingMachine fillingMachine,String langauge);
    
    String getFileType(String fileURI);
    
    String getLanguage(String fileURI);

    void createProductPackageType(Packagetype packageType, String langauge);

    void createProductProcessingEquipement(ProcessingEquipement equipement, String langauge);
}

