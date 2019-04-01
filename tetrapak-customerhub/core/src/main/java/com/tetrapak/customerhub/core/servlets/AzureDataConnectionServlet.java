package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.DynamicTableEntity;
import com.microsoft.azure.storage.table.EntityProperty;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import com.microsoft.azure.storage.table.TableServiceException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

/**
 * 
 * This Servlet is used for the operations related to retrieve data from the
 * Azure storage
 * 
 * @author swalamba
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=AzureDataConnectionServlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/customerhub/azure/userdata" })
public class AzureDataConnectionServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1;
	protected final static String tableNamePrefix = "tetrapakusertable";

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureDataConnectionServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOGGER.info("HTTP GET request from AzureDataConnectionServlet");
		String tableName = tableNamePrefix;
		CloudTable cloudTable = null;
		try {
			cloudTable = createTable(getTableClientReference(), tableName);
			LOGGER.info("\tSuccessfully created the table.");

		} catch (InvalidKeyException | RuntimeException | URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
			LOGGER.error("\tSome error occured while creating table or inserting the data.");
		}

		try {
			if (null == cloudTable) {
				cloudTable = getTableClientReference().getTableReference(tableName);
			}
			insertDataInTable(cloudTable);
		} catch (TableServiceException e) {
			e.printStackTrace();
			if (e.getErrorCode() == "EntityAlreadyExists") {
				// Retrieve the entity with partition key of "Smith" and row key of "Jeff".
				TableOperation retrieve12345Swati = TableOperation.retrieve("12345", "Swati", UserDataEntity.class);

				// Submit the operation to the table service and get the specific entity.
				UserDataEntity specificEntity = null;
				try {
					specificEntity = cloudTable.execute(retrieve12345Swati).getResultAsType();

					Map<String, Map<String, String>> userPrefrences = new HashMap<String, Map<String, String>>();
					Map<String, String> orderPrefrences = new HashMap<String, String>();
					orderPrefrences.put("orderNumber", "Y");
					orderPrefrences.put("orderID", "Y");
					orderPrefrences.put("orderContact", "N");
					userPrefrences.put("orderPrefrences", orderPrefrences);

					Map<String, String> localePrefrences = new HashMap<String, String>();
					localePrefrences.put("preferredLanguage", "english");
					userPrefrences.put("localePrefrences", localePrefrences);

					Type mapType = new TypeToken<Map<String, Map<String, String>>>() {
					}.getType();
					Gson gson = new Gson();
					String json = gson.toJson(userPrefrences, mapType);

					// specificEntity.setUserPrefrences(json);

					// Create an operation to replace the entity.
					TableOperation replaceEntity = TableOperation.replace(specificEntity);
					// Submit the operation to the table service.
					cloudTable.execute(replaceEntity);
				} catch (StorageException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder str = new StringBuilder();
		try {
			List<DynamicTableEntity> tableData = retrieveDataFromTable(cloudTable);
			Iterator<DynamicTableEntity> itr = tableData.iterator();
			while (itr.hasNext()) {
				DynamicTableEntity ce = itr.next();
				str.append(ce.getRowKey() + "<br>");
				str.append(ce.getPartitionKey());
				str.append(ce.getProperties().get("userID").getValueAsString());
				str.append(ce.getProperties().get("userName").getValueAsString());
				str.append(ce.getProperties().get("userPrefrences").getValueAsString());
			}
		} catch (InvalidKeyException | RuntimeException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("result", str.toString());
		jsonResponse.addProperty("status", CustomerHubConstants.RESPONSE_STATUS_SUCCESS);
		response.setContentType("application/json");
		response.getWriter().write(jsonResponse.toString());
	}

	private void insertDataInTable(CloudTable cloudTable)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		// Create a sample entities for use
		UserDataEntity userData1 = new UserDataEntity("12345", "Swati");
		Map<String, Map<String, String>> userPrefrences = new HashMap<String, Map<String, String>>();
		Map<String, String> orderPrefrences = new HashMap<String, String>();
		orderPrefrences.put("orderNumber", "Y");
		orderPrefrences.put("orderID", "Y");
		orderPrefrences.put("orderContact", "N");
		userPrefrences.put("orderPrefrences", orderPrefrences);

		Map<String, String> localePrefrences = new HashMap<String, String>();
		localePrefrences.put("preferredLanguage", "english");
		userPrefrences.put("localePrefrences", localePrefrences);

		Type mapType = new TypeToken<Map<String, Map<String, String>>>() {
		}.getType();
		Gson gson = new Gson();
		String json = gson.toJson(userPrefrences, mapType);
		final HashMap<String, EntityProperty> properties = new HashMap<>();
		EntityProperty userIdVal = new EntityProperty("12345");
		EntityProperty userNameVal = new EntityProperty("Swati");
		EntityProperty userPrefrencesVal = new EntityProperty(json);
		properties.put("userID", userIdVal);
		properties.put("userName", userNameVal);
		properties.put("userPrefrences", userPrefrencesVal);
		DynamicTableEntity entity = new DynamicTableEntity("12345", "Swati", properties);

		// userData1.setUserPrefrences(json);
		// Create and insert new customer entities
		LOGGER.info("\nInsert the new entities.");
		cloudTable.execute(TableOperation.insertOrMerge(entity));
	}

	private List<DynamicTableEntity> retrieveDataFromTable(CloudTable cloudTable)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException {
		List<DynamicTableEntity> rowList = new ArrayList<DynamicTableEntity>();
		CloudTableClient tableClient = getTableClientReference();
		// Create a cloud table object for the table.
		try {
			// Create a filter condition where the partition key is "Smith".
			String partitionFilter = TableQuery.generateFilterCondition("PartitionKey", QueryComparisons.EQUAL, "Harp");

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
	private static CloudTable createTable(CloudTableClient tableClient, String tableName)
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

	CloudTableClient getTableClientReference()
			throws RuntimeException, IOException, URISyntaxException, InvalidKeyException {
		CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(
					"DefaultEndpointsProtocol=https;AccountName=ta01cfedsta01;AccountKey=Fa6WBGXsJZ+9Hyt5ggAKQD4WJQ4j77foq4a8S2S+wr663sVxPO5AFrhOPEgbxsPt+WBYDyfH654CIlfncy0klg==");
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

}
