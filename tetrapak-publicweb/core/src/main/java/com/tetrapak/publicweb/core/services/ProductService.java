package com.tetrapak.publicweb.core.services;

import java.util.List;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;

import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class
 */
public interface ProductService {

    void createProductFillingMachine(ResourceResolver resolver, Session session, String productType,
            List<FillingMachine> fillingMachines, String langauge);

    String getFileType(String fileURI);

    String getLanguage(String fileURI);

    void createProductPackageType(ResourceResolver resolver, Session session, String productType,
            List<Packagetype> packageTypes, String langauge);

    void createProductProcessingEquipement(ResourceResolver resolver, Session session, String productType,
            List<ProcessingEquipement> equipements, String langauge);
}
