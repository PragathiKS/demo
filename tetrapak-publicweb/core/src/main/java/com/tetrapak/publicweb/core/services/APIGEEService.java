package com.tetrapak.publicweb.core.services;

import java.util.List;

import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class
 */
public interface APIGEEService {

    BearerToken getBearerToken();

    List<FillingMachine> getFillingMachines(String token, String fileURI);
    
    Files getListOfFiles(String type,String token);

    List<Packagetype> getPackageTypes(String token, String fileURI);

    List<ProcessingEquipement> getProcessingEquipements(String token, String fileURI);
}

