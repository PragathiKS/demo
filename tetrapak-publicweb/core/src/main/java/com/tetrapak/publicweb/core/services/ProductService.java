package com.tetrapak.publicweb.core.services;

import java.util.List;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class
 */
public interface ProductService {

    void createProductFillingMachine(String productType,List<FillingMachine> fillingMachines,String langauge);
    
    String getFileType(String fileURI);
    
    String getLanguage(String fileURI);

    void createProductPackageType(String productType,List<Packagetype> packageTypes, String langauge);

    void createProductProcessingEquipement(String productType,List<ProcessingEquipement> equipements, String langauge);
}

