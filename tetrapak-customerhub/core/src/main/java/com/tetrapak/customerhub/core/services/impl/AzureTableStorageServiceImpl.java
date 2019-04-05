package com.tetrapak.customerhub.core.services.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.DynamicTableEntity;
import com.microsoft.azure.storage.table.EntityProperty;
import com.microsoft.azure.storage.table.TableOperation;
import com.tetrapak.customerhub.core.exceptions.AzureRuntimeException;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;
import com.tetrapak.customerhub.core.services.config.AzureTableStorageServiceConfig;

/**
 * Impl class for AzureTableStorage Service
 * 
 * @author Swati Lamba
 */
@Component(immediate = true, service = AzureTableStorageService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = AzureTableStorageServiceConfig.class)
public class AzureTableStorageServiceImpl implements AzureTableStorageService {

	private AzureTableStorageServiceConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureTableStorageServiceImpl.class);

	private static final String PARTITION_KEY = "tetrapakuser";

	/**
	 * activate method to set AzureTableStorage Configuration
	 * 
	 * @param config AzureTableStorage Configuration
	 */
	@Activate
	public void activate(AzureTableStorageServiceConfig config) {
		this.config = config;
	}

	/**
	 * @return the TableClientReference on basis of the connection details
	 *         configured via configuration
	 * @throws AzureRuntimeException Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wronglength, uninitialized, etc).
	 */
	private CloudTableClient getTableClientReference()
			throws IOException, URISyntaxException, InvalidKeyException, AzureRuntimeException {
		CloudStorageAccount storageAccount;

		try {
			storageAccount = CloudStorageAccount.parse("DefaultEndpointsProtocol=" + config.defaultEndpointsProtocol()
					+ ";AccountName=" + config.accountName() + ";AccountKey=" + config.accountKey());
		} catch (URISyntaxException e) {
			LOGGER.error("\nConnection string specifies an invalid URI.");
			LOGGER.error("Please confirm the connection string is in the Azure connection string format.");
			throw e;
		} catch (InvalidKeyException e) {
			LOGGER.error("\nConnection string specifies an invalid key.");
			LOGGER.error("Please confirm the AccountName and AccountKey in the connection string are valid.");
			throw e;
		} catch (RuntimeException e) {
			LOGGER.error("\nA run-time exception occured while getting client reference.");
			throw new AzureRuntimeException("An exception occured while getting client reference", e);
		}
		LOGGER.debug("getTableClientReference: " + storageAccount.toString(false));
		return storageAccount.createCloudTableClient();
	}

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
	 * @throws AzureRuntimeException    Custom exception to wrap the Azure
	 *                                  RuntimeException that can be thrown during
	 *                                  the normal operation of the Java Virtual
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
	@Override
	public void createTable(String tableName)
			throws StorageException, AzureRuntimeException, IOException, InvalidKeyException, URISyntaxException {

		// Create a new table
		CloudTable table = this.getTableClientReference().getTableReference(tableName);
		try {
			if (table.createIfNotExists() == false) {
				throw new AzureRuntimeException("Table already exists!",
						new IllegalStateException(String.format("Table with name \"%s\" already exists.", tableName)));
			}
		} catch (StorageException s) {
			if (s.getCause() instanceof java.net.ConnectException) {
				LOGGER.error(
						"Caught connection exception from the client. If running with the default configuration please make sure you have started the storage emulator.");
			}
			throw s;
		} catch (RuntimeException e) {
			LOGGER.error("\nA run-time exception occured while getting client reference.");
			throw new AzureRuntimeException("An exception occured while getting client reference", e);
		}
	}

	/**
	 * 
	 * This service is used to retrieve entity on basis of a particular userId.
	 * 
	 * @param tableName : Name of the table in Azure Table Storage
	 * @param userId    : unique userId of a user
	 * @return user data from the Azure Table Storage on basis of userId as row key
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wronglength, uninitialized, etc
	 * @throws RuntimeException      Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred. Thisclass is the general class of
	 *                               exceptions produced by failed orinterrupted I/O
	 *                               operations.
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference.
	 * @throws StorageException      Represents an exception for the Microsoft Azure
	 *                               storage service.
	 * @throws AzureRuntimeException
	 * 
	 */
	@Override
	public DynamicTableEntity retrieveDataFromTable(String tableName, String userId)
			throws StorageException, InvalidKeyException, AzureRuntimeException, URISyntaxException, IOException {
		DynamicTableEntity retrieveParticularUser = new DynamicTableEntity();
		CloudTable cloudTable = this.getTableClientReference().getTableReference(tableName);

		if (!userId.isEmpty()) {
			TableOperation retrieveParticularUserOperation = TableOperation.retrieve(PARTITION_KEY, userId,
					DynamicTableEntity.class);
			retrieveParticularUser = cloudTable.execute(retrieveParticularUserOperation).getResultAsType();
			LOGGER.debug("Data retrieval for the userID:{} successful from Azure Storage", userId);
		} else {
			LOGGER.warn("UserId cannot be empty!!");
		}

		return retrieveParticularUser;
	}

	/**
	 * 
	 * This service is used to: Either insert new user preference. Or update
	 * existing userPreferences.
	 * 
	 * @param tableName Name of the table in Azure Table Storage
	 * @param entity    on basis of userId of a user
	 * @throws AzureRuntimeException
	 * @throws StorageException      Represents an exception for the Microsoft Azure
	 *                               storage service
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wronglength, uninitialized, etc).
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference.
	 * @throws AzureRuntimeException Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred
	 */
	@Override
	public void insertOrUpdateRowInTable(String tableName, DynamicTableEntity entity)
			throws InvalidKeyException, AzureRuntimeException, StorageException, URISyntaxException, IOException {
		LOGGER.debug("\nInsert or merge the given entities.");
		try {
			this.getTableClientReference().getTableReference(tableName).execute(TableOperation.insertOrMerge(entity));
		} catch (RuntimeException e) {
			throw new AzureRuntimeException("Exception while inserting or updating an entity!", e);
		}
	}

	/**
	 * 
	 * This service is used to delete all userPreference data for given userId
	 * 
	 * @param tableName Name of the table in Azure Table Storage
	 * @param userId    unique userId of a user
	 * @return true if successful to delete the row on basis of userId
	 * @throws AzureRuntimeException
	 * @throws StorageException      Represents an exception for the Microsoft Azure
	 *                               storage service.
	 * 
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wronglength, uninitialized, etc).
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference
	 * @throws AzureRuntimeException Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred.
	 */
	@Override
	public boolean deleteRowInTable(String tableName, String userId)
			throws InvalidKeyException, AzureRuntimeException, StorageException, URISyntaxException, IOException {
		if (!userId.isEmpty()) {

			DynamicTableEntity entity = this.retrieveDataFromTable(tableName, userId);
			if (null != entity) {
				CloudTable cloudTable = this.getTableClientReference().getTableReference(tableName);
				cloudTable.execute(TableOperation.delete(entity));
				LOGGER.debug("Deleted the entity with the userID: {}", userId);
				return true;
			} else {
				LOGGER.warn("Could not find row with userID: " + userId);
			}
		} else {
			LOGGER.warn("Please provide a valid userID to delete the userData!");
		}
		return false;
	}

	/**
	 * 
	 * This service is used for saving the user preference data to be given in JSON
	 * format as String of a particular type into the Azure Table Storage table.
	 * 
	 * @param tableName           Name of the table in Azure Table Storage
	 * @param userId              unique userId of a user
	 * @param userPrefType        type of user-preference
	 * @param userPreferencesData data stored corresponding to the preference type
	 * 
	 * @throws StorageException      Represents an exception for the Microsoft Azure
	 *                               storage service
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wronglength, uninitialized, etc).
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference.
	 * @throws AzureRuntimeException Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred.
	 */
	@Override
	public void saveUserPreferencesToAzureTable(String tableName, String userId, String userPrefType,
			String userPreferencesData)
			throws InvalidKeyException, AzureRuntimeException, StorageException, URISyntaxException, IOException {

		DynamicTableEntity row = this.retrieveDataFromTable(tableName, userId);
		if (null == row) {
			row = new DynamicTableEntity();
			row.setRowKey(userId);
			row.setPartitionKey(PARTITION_KEY);
		}
		row.getProperties().put(userPrefType, new EntityProperty(userPreferencesData));
		this.insertOrUpdateRowInTable(tableName, row);
		LOGGER.debug("Data insert/merge for the userID:{} successful to Azure Storage", userId);
	}

	/**
	 * This service is used to fetch a particular user preference's data for a
	 * userId and returns in JSON format as String.
	 * 
	 * @param tableName    Name of the table in Azure Table Storage
	 * @param userId       unique userId of a user
	 * @param userPrefType type of user-preference
	 * @return particular user preference's data for a userId
	 * 
	 * @throws StorageException      Represents an exception for the Microsoft Azure
	 *                               storage service.
	 * 
	 * @throws InvalidKeyException   This is the exception for invalid Keys (invalid
	 *                               encoding, wrong length, uninitialized, etc
	 * @throws URISyntaxException    Checked exception thrown to indicate that a
	 *                               string could not be parsed as aURI reference
	 * @throws AzureRuntimeException Custom exception to wrap the Azure
	 *                               RuntimeException that can be thrown during the
	 *                               normal operation of the Java Virtual Machine.
	 * @throws IOException           Signals that an I/O exception of some sort has
	 *                               occurred. This class is the general class of
	 *                               exceptions produced by failed or interrupted
	 *                               I/O operations.
	 */
	@Override
	public String getUserPreferencesFromAzureTable(String tableName, String userId, String userPrefType)
			throws InvalidKeyException, AzureRuntimeException, StorageException, URISyntaxException, IOException {
		DynamicTableEntity userData = this.retrieveDataFromTable(tableName, userId);
		if (null != userData) {
			boolean ifPrefTypePresentInUserData = userData.getProperties().containsKey(userPrefType);
			if (ifPrefTypePresentInUserData) {
				return userData.getProperties().get(userPrefType).getValueAsString();
			}
		}
		return null;
	}

	/**
	 * @return the Table name to be used in Azure to save the userPreferences
	 */
	@Override
	public String getUserPreferencesTableName() {
		return config.tableName();
	}

}
