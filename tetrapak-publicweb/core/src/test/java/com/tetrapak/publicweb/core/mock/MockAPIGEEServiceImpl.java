package com.tetrapak.publicweb.core.mock;

import com.tetrapak.publicweb.core.beans.pxp.DeltaProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.DeltaPackageType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParseException;
import com.tetrapak.publicweb.core.beans.pxp.DeltaFillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import java.io.IOException;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import java.util.List;
import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.services.APIGEEService;

public class MockAPIGEEServiceImpl implements APIGEEService
{
    public BearerToken getBearerToken() {
        final BearerToken token = new BearerToken();
        token.setAccessToken("Yyl1PU2AdlNbDaNNsaiR5GM3LaVp");
        return token;
    }
    
    public List<FillingMachine> getFillingMachines(final String token, final String fileURI) {
        try {
            return Arrays.asList(new ObjectMapper().readValue(FillingMachine[].class.getResourceAsStream("/pxp/full_feed_fillingmachines.json"), FillingMachine[].class));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Files getListOfFiles(final String type, final String token) {
        try {
            if (type.equalsIgnoreCase("delta")) {
                return new ObjectMapper().readValue(Files.class.getResourceAsStream("/pxp/delta_feed_files_list.json"), Files.class);
            }
            return new ObjectMapper().readValue(Files.class.getResourceAsStream("/pxp/full_feed_files_list.json"), Files.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Packagetype> getPackageTypes(final String token, final String fileURI) {
        try {
            return Arrays.asList((Packagetype[])new ObjectMapper().readValue(Packagetype[].class.getResourceAsStream("/pxp/full_feed_packagetypes.json"), Packagetype[].class));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<ProcessingEquipement> getProcessingEquipements(final String token, final String fileURI) {
        try {
            return Arrays.asList((ProcessingEquipement[])new ObjectMapper().readValue(ProcessingEquipement[].class.getResourceAsStream("/pxp/full_feed_processingequipements.json"), ProcessingEquipement[].class));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public DeltaFillingMachine getDeltaFillingMachines(final String token, final String fileURI) {
        try {
            return new ObjectMapper().readValue(DeltaFillingMachine.class.getResourceAsStream("/pxp/full_feed_deltaFillingmachine.json"), DeltaFillingMachine.class);
        }
        catch (JsonParseException e) {
            e.printStackTrace();
        }
        catch (JsonMappingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    public DeltaPackageType getDeltaPackageTypes(final String token, final String fileURI) {
        try {
            return new ObjectMapper().readValue(DeltaPackageType.class.getResourceAsStream("/pxp/full_feed_deltapackagetype.json"), DeltaPackageType.class);
        }
        catch (JsonParseException e) {
            e.printStackTrace();
        }
        catch (JsonMappingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    public DeltaProcessingEquipement getDeltaProcessingEquipements(final String token, final String fileURI) {
        try {
            return (DeltaProcessingEquipement)new ObjectMapper().readValue(DeltaProcessingEquipement.class.getResourceAsStream("/pxp/full_feed_deltaprocessingequipement.json"), DeltaProcessingEquipement.class);
        }
        catch (JsonParseException e) {
            e.printStackTrace();
        }
        catch (JsonMappingException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return null;
    }
}