package com.tetrapak.publicweb.core.services;

import java.util.List;

import com.tetrapak.publicweb.core.beans.pxp.BearerToken;
import com.tetrapak.publicweb.core.beans.pxp.DeltaFillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.DeltaPackageType;
import com.tetrapak.publicweb.core.beans.pxp.DeltaProcessingEquipement;
import com.tetrapak.publicweb.core.beans.pxp.Files;
import com.tetrapak.publicweb.core.beans.pxp.FillingMachine;
import com.tetrapak.publicweb.core.beans.pxp.Packagetype;
import com.tetrapak.publicweb.core.beans.pxp.ProcessingEquipement;

/**
 * API GEE Service class.
 */
public interface APIGEEService {

    /**
     * Gets the bearer token.
     *
     * @return the bearer token
     */
    BearerToken getBearerToken();

    /**
     * Gets the filling machines.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the filling machines
     */
    List<FillingMachine> getFillingMachines(String token, String fileURI);

    /**
     * Gets the list of files.
     *
     * @param type
     *            the type
     * @param token
     *            the token
     * @return the list of files
     */
    Files getListOfFiles(String type, String token);

    /**
     * Gets the package types.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the package types
     */
    List<Packagetype> getPackageTypes(String token, String fileURI);

    /**
     * Gets the processing equipements.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the processing equipements
     */
    List<ProcessingEquipement> getProcessingEquipements(String token, String fileURI);

    /**
     * Gets the delta filling machines.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the delta filling machines
     */
    DeltaFillingMachine getDeltaFillingMachines(String token, String fileURI);

    /**
     * Gets the delta package types.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the delta package types
     */
    DeltaPackageType getDeltaPackageTypes(String token, String fileURI);

    /**
     * Gets the delta processing equipements.
     *
     * @param token
     *            the token
     * @param fileURI
     *            the file URI
     * @return the delta processing equipements
     */
    DeltaProcessingEquipement getDeltaProcessingEquipements(String token, String fileURI);

}
