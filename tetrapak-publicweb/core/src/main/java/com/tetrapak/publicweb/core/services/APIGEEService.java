package com.tetrapak.publicweb.core.services;

import java.util.List;

import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;

/**
 * API GEE Service class
 */
public interface APIGEEService {

    BearerToken getBearerToken();

    List<FillingMachine> getFillingMachines(String token, String fileURI);
    
    Files getListOfFiles(String type,String token);
}

