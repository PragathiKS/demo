package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
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
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.DynamicTableEntity;
import com.microsoft.azure.storage.table.EntityProperty;
import com.microsoft.azure.storage.table.TableServiceException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;

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

	@Reference
	private AzureTableStorageService azureTableStorageService;

	protected final static String tableNamePrefix = "tetrapakusertable";

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureDataConnectionServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		LOGGER.info("HTTP GET request from AzureDataConnectionServlet");
		String tableName = tableNamePrefix;
		
		
		try {
			azureTableStorageService.createTable(tableName);
			LOGGER.info("\tSuccessfully created the table.");

		} catch (InvalidKeyException | RuntimeException | URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
			LOGGER.error("\tSome error occured while creating table or inserting the data.");
		}

		try {
			insertDataInTable(tableName);
		} catch (TableServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder str = new StringBuilder();
		try {
			List<DynamicTableEntity> tableData = azureTableStorageService.retrieveDataFromTable(tableName, "12345");
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

	private void insertDataInTable(String tableName)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException {
		// Create a sample entities for use
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
		DynamicTableEntity entity = new DynamicTableEntity("tetrapakuser", "12345", properties);
		LOGGER.info("\nInsert the new entities.");
		
		azureTableStorageService.insertOrUpdateRowInTable(tableName, entity);
	}

}
