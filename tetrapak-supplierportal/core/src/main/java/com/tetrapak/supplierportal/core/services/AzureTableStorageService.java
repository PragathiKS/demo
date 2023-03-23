package com.tetrapak.supplierportal.core.services;

import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * This Service is used for the CRUD operations related to Azure storage
 *
 * @author Swati Lamba
 */
public interface AzureTableStorageService {

    /**
     * This service is to get the name of the user-preference table being used in
     * Azure Storage Service.
     *
     * @return the name of the table configured for the Azure Table Storage of user
     * preferences
     */
    String getUserPreferencesTableName();

    /**
     * This service is used for saving the user preference data to be given in JSON
     * format as String of a particular type into the Azure Table Storage table.
     *
     * @param tableName           Name of the table in Azure Table Storage
     * @param userId              unique userId of a user
     * @param userPrefType        type of user-preference
     * @param userPreferencesData data stored corresponding to the preference type
     * @throws StorageException    Represents an exception for the Microsoft Azure
     *                             storage service
     * @throws InvalidKeyException This is the exception for invalid Keys (invalid
     *                             encoding, wronglength, uninitialized, etc).
     * @throws URISyntaxException  Checked exception thrown to indicate that a
     *                             string could not be parsed as aURI reference.
     * @throws IOException         Signals that an I/O exception of some sort has
     *                             occurred.
     */
    void saveUserPreferencesToAzureTable(String tableName, String userId, String userPrefType,
            String userPreferencesData) throws InvalidKeyException, StorageException, URISyntaxException, IOException;

    /**
     * This service is used to fetch a particular user preference's data for a
     * userId and returns in JSON format as String.
     *
     * @param tableName    Name of the table in Azure Table Storage
     * @param userId       unique userId of a user
     * @param userPrefType type of user-preference
     * @return particular user preference's data for a userId
     * @throws StorageException    Represents an exception for the Microsoft Azure
     *                             storage service.
     * @throws InvalidKeyException This is the exception for invalid Keys (invalid
     *                             encoding, wrong length, uninitialized, etc
     * @throws URISyntaxException  Checked exception thrown to indicate that a
     *                             string could not be parsed as aURI reference
     * @throws IOException         Signals that an I/O exception of some sort has
     *                             occurred. This class is the general class of
     *                             exceptions produced by failed or interrupted
     *                             I/O operations.
     */
    String getUserPreferencesFromAzureTable(String tableName, String userId, String userPrefType)
            throws InvalidKeyException, StorageException, URISyntaxException, IOException;

}
