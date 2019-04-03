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
	 * activate method
	 * 
	 * @param config
	 */
	@Activate
	public void activate(AzureTableStorageServiceConfig config) {
		this.config = config;
	}

	private CloudTableClient getTableClientReference()
			throws RuntimeException, IOException, URISyntaxException, InvalidKeyException {
		CloudStorageAccount storageAccount;

		try {
			storageAccount = CloudStorageAccount.parse("DefaultEndpointsProtocol=" + config.defaultEndpointsProtocol()
					+ ";AccountName=" + config.accountName() + ";AccountKey=" + config.accountKey());
		} catch (IllegalArgumentException | URISyntaxException e) {
			LOGGER.error("\nConnection string specifies an invalid URI.");
			LOGGER.error("Please confirm the connection string is in the Azure connection string format.");
			throw e;
		} catch (InvalidKeyException e) {
			LOGGER.error("\nConnection string specifies an invalid key.");
			LOGGER.error("Please confirm the AccountName and AccountKey in the connection string are valid.");
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		LOGGER.debug("getTableClientReference: " + storageAccount.toString(false));
		return storageAccount.createCloudTableClient();
	}

	/**
	 * Creates and returns a table for the application to use.
	 *
	 * @param tableClient CloudTableClient object
	 * @param tableName   Name of the table to create
	 * @return The newly created CloudTable object
	 *
	 * @throws StorageException
	 * @throws RuntimeException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws IllegalArgumentException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 */
	@Override
	public void createTable(String tableName) throws StorageException, RuntimeException, IOException,
			InvalidKeyException, IllegalArgumentException, URISyntaxException, IllegalStateException {

		// Create a new table
		CloudTable table = this.getTableClientReference().getTableReference(tableName);
		try {
			if (table.createIfNotExists() == false) {
				throw new IllegalStateException(String.format("Table with name \"%s\" already exists.", tableName));
			}
		} catch (StorageException s) {
			if (s.getCause() instanceof java.net.ConnectException) {
				LOGGER.error(
						"Caught connection exception from the client. If running with the default configuration please make sure you have started the storage emulator.");
			}
			throw s;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public DynamicTableEntity retrieveDataFromTable(String tableName, String userId)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException, StorageException {
		DynamicTableEntity retrieveParticularUser = new DynamicTableEntity();
		CloudTable cloudTable = this.getTableClientReference().getTableReference(tableName);
		try {
			if (!userId.isEmpty()) {
				TableOperation retrieveParticularUserOperation = TableOperation.retrieve(PARTITION_KEY, userId,
						DynamicTableEntity.class);
				retrieveParticularUser = cloudTable.execute(retrieveParticularUserOperation).getResultAsType();
				LOGGER.debug("Data retrieval for the userID:{} successful from Azure Storage", userId);
			} else {
				LOGGER.warn("UserId cannot be empty!!");
			}
		} catch (Exception e) {
			LOGGER.error("An exception occured while retrieving the data for userID:{} from the Azure table", userId,
					e);
			throw e;
		}
		return retrieveParticularUser;
	}

	@Override
	public void insertOrUpdateRowInTable(String tableName, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		LOGGER.debug("\nInsert or merge the given entities.");
		this.getTableClientReference().getTableReference(tableName).execute(TableOperation.insertOrMerge(entity));
	}

	@Override
	public boolean deleteRowInTable(String tableName, String userId)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
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

	@Override
	public void saveUserPreferencesToAzureTable(String tableName, String userId, String userPrefType,
			String userPreferencesData)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		try {
			DynamicTableEntity row = this.retrieveDataFromTable(tableName, userId);
			if(null == row) {
				row = new DynamicTableEntity();
				row.setRowKey(userId);
				row.setPartitionKey(PARTITION_KEY);
			}
			row.getProperties().put(userPrefType, new EntityProperty(userPreferencesData));
			this.insertOrUpdateRowInTable(tableName, row);
			LOGGER.debug("Data insert/merge for the userID:{} successful to Azure Storage", userId);
		} catch (InvalidKeyException | RuntimeException | IOException | URISyntaxException | StorageException e) {
			LOGGER.error("An exception occured while saving the userprefrences for userID:{}, exception: {} ", userId,
					e);
			throw e;
		}

	}

	@Override
	public String getUserPreferencesFromAzureTable(String tableName, String userId, String userPrefType)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		DynamicTableEntity userData = this.retrieveDataFromTable(tableName, userId);
		if (null != userData) {
			boolean ifPrefTypePresentInUserData = userData.getProperties().containsKey(userPrefType);
			if (ifPrefTypePresentInUserData) {
				return userData.getProperties().get(userPrefType).getValueAsString();
			}
		}
		return null;
	}

}
