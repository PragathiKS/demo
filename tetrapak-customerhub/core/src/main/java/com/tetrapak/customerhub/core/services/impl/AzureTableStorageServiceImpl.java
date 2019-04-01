package com.tetrapak.customerhub.core.services.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.DynamicTableEntity;
import com.microsoft.azure.storage.table.EntityProperty;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;
import com.tetrapak.customerhub.core.services.config.AzureTableStorageServiceConfig;
import com.tetrapak.customerhub.core.servlets.UserDataEntity;

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

	/**
	 * activate method
	 * 
	 * @param config
	 */
	@Activate
	public void activate(AzureTableStorageServiceConfig config) {

		this.config = config;
	}

	@Override
	public CloudTableClient getTableClientReference()
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

		return storageAccount.createCloudTableClient();
	}

	/**
	 * Creates and returns a table for the sample application to use.
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
	public CloudTable createTable(CloudTableClient tableClient, String tableName)
			throws StorageException, RuntimeException, IOException, InvalidKeyException, IllegalArgumentException,
			URISyntaxException, IllegalStateException {

		// Create a new table
		CloudTable table = tableClient.getTableReference(tableName);
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

		return table;
	}

	@Override
	public List<DynamicTableEntity> retrieveDataFromTable(CloudTable cloudTable)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException {
		List<DynamicTableEntity> rowList = new ArrayList<DynamicTableEntity>();
		// Create a cloud table object for the table.
		try {
			// Create a filter condition where the partition key is "Smith".
			//String partitionFilter = TableQuery.generateFilterCondition("PartitionKey", QueryComparisons.EQUAL, "Harp");

			// Specify a partition query, using "Smith" as the partition key filter.
			TableQuery<DynamicTableEntity> partitionQuery = TableQuery.from(DynamicTableEntity.class);// .where(partitionFilter);

			// Loop through the results, displaying information about the entity.
			for (DynamicTableEntity entity : cloudTable.execute(partitionQuery)) {
				LOGGER.info(entity.getPartitionKey() + " " + entity.getRowKey());
				rowList.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw e;
		}
		return rowList;
	}

	@Override
	public void insertOrUpdateRowInTable(CloudTable cloudTable, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		// Create and insert new customer entities
		LOGGER.info("\nInsert the new entities.");
		cloudTable.execute(TableOperation.insertOrMerge(entity));
	}

	@Override
	public void deleteRowInTable(CloudTable cloudTable, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		cloudTable.execute(TableOperation.delete(entity));
	}

}
