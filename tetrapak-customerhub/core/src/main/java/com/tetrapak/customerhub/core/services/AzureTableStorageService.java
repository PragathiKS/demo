package com.tetrapak.customerhub.core.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.DynamicTableEntity;

/**
 * 
 * This Service is used for the CRUD operations related to Azure storage
 * 
 * @author swalamba
 *
 */
public interface AzureTableStorageService {

	/**
	 * This service is to get the name of the user-preference table being used in
	 * Azure Storage Service.
	 * 
	 * @return the name of the table configured for the Azure Table Storage of user
	 *         preferences
	 */
	String getUserPreferencesTableName();

	/**
	 * Creates and returns a table for the application to use.
	 * 
	 * The table will be created using the Azure Table Storage Configurations
	 * Account Name and Account Secret Key.
	 * 
	 * @param tableClient CloudTableClient object
	 * @param tableName   Name of the table to create
	 * @return The newly created CloudTable object
	 *
	 * @throws StorageException         Represents an exception for the Microsoft
	 *                                  Azure storage service.
	 * 
	 * @throws RuntimeException         RuntimeException is the superclass of
	 *                                  thoseexceptions that can be thrown during
	 *                                  the normal operation of theJava Virtual
	 *                                  Machine.
	 * @throws IOException              Signals that an I/O exception of some sort
	 *                                  has occurred
	 * @throws URISyntaxException       Checked exception thrown to indicate that a
	 *                                  string could not be parsed as aURI reference
	 * @throws IllegalArgumentException Thrown to indicate that a method has been
	 *                                  passed an illegal orinappropriate argument
	 * @throws InvalidKeyException      This is the exception for invalid Keys
	 *                                  (invalid encoding, wronglength,
	 *                                  uninitialized, etc).
	 * @throws IllegalStateException    Signals that a method has been invoked at an
	 *                                  illegal or inappropriate time
	 */
	void createTable(String tableName) throws StorageException, RuntimeException, IOException, InvalidKeyException,
			IllegalArgumentException, URISyntaxException, IllegalStateException;

	/**
	 * 
	 * This service is used to retrieve entity on basis of a particular userId.
	 * 
	 * @param tableName : Name of the table in Azure Table Storage
	 * @param userId    : unique userId of a user
	 * @return user data from the Azure Table Storage on basis of userId as row key
	 * @throws InvalidKeyException This is the exception for invalid Keys (invalid
	 *                             encoding, wronglength, uninitialized, etc
	 * @throws RuntimeException    RuntimeException is the superclass of
	 *                             thoseexceptions that can be thrown during the
	 *                             normal operation of theJava Virtual Machine.
	 * @throws IOException         Signals that an I/O exception of some sort has
	 *                             occurred. Thisclass is the general class of
	 *                             exceptions produced by failed orinterrupted I/O
	 *                             operations.
	 * @throws URISyntaxException  Checked exception thrown to indicate that a
	 *                             string could not be parsed as aURI reference.
	 * @throws StorageException    Represents an exception for the Microsoft Azure
	 *                             storage service.
	 * 
	 */
	DynamicTableEntity retrieveDataFromTable(String tableName, String userId)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException, StorageException;

	/**
	 * 
	 * This service is used to: Either insert new user preference. Or update
	 * existing userPreferences.
	 * 
	 * @param tableName Name of the table in Azure Table Storage
	 * @param entity    on basis of userId of a user
	 * @throws StorageException    Represents an exception for the Microsoft Azure
	 *                             storage service
	 * @throws InvalidKeyException This is the exception for invalid Keys (invalid
	 *                             encoding, wronglength, uninitialized, etc).
	 * @throws URISyntaxException  Checked exception thrown to indicate that a
	 *                             string could not be parsed as aURI reference.
	 * @throws RuntimeException    RuntimeException is the superclass of those
	 *                             exceptions that can be thrown during the normal
	 *                             operation of theJava Virtual Machine.
	 * @throws IOException         Signals that an I/O exception of some sort has
	 *                             occurred
	 */
	void insertOrUpdateRowInTable(String tableName, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

	/**
	 * 
	 * This service is used to delete all userPreference data for given userId
	 * 
	 * @param tableName Name of the table in Azure Table Storage
	 * @param userId    unique userId of a user
	 * @return true if successful to delete the row on basis of userId
	 * @throws StorageException    Represents an exception for the Microsoft Azure
	 *                             storage service.
	 * 
	 * @throws InvalidKeyException This is the exception for invalid Keys (invalid
	 *                             encoding, wronglength, uninitialized, etc).
	 * @throws URISyntaxException  Checked exception thrown to indicate that a
	 *                             string could not be parsed as aURI reference
	 * @throws RuntimeException    RuntimeException is the superclass of thos
	 *                             eexceptions that can be thrown during the normal
	 *                             operation of theJava Virtual Machine.
	 * @throws IOException         Signals that an I/O exception of some sort has
	 *                             occurred.
	 */
	boolean deleteRowInTable(String tableName, String userId)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

	/**
	 * 
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
	 * @throws RuntimeException    RuntimeException is the superclass of
	 *                             thoseexceptions that can be thrown during the
	 *                             normal operation of theJava Virtual Machine.
	 * @throws IOException         Signals that an I/O exception of some sort has
	 *                             occurred.
	 */
	void saveUserPreferencesToAzureTable(String tableName, String userId, String userPrefType,
			String userPreferencesData)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

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
	 * 
	 * @throws InvalidKeyException This is the exception for invalid Keys (invalid
	 *                             encoding, wrong length, uninitialized, etc
	 * @throws URISyntaxException  Checked exception thrown to indicate that a
	 *                             string could not be parsed as aURI reference
	 * @throws RuntimeException    RuntimeException is the superclass of those
	 *                             exceptions that can be thrown during the normal
	 *                             operation of theJava Virtual Machine.
	 * @throws IOException         Signals that an I/O exception of some sort has
	 *                             occurred. This class is the general class of
	 *                             exceptions produced by failed orinterrupted I/O
	 *                             operations.
	 */
	String getUserPreferencesFromAzureTable(String tableName, String userId, String userPrefType)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

}
