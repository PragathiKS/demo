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
	
	String getUserPreferencesTableName();

	void createTable(String tableName) throws StorageException, RuntimeException, IOException, InvalidKeyException,
			IllegalArgumentException, URISyntaxException, IllegalStateException;

	DynamicTableEntity retrieveDataFromTable(String tableName, String userId)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException, StorageException;

	void insertOrUpdateRowInTable(String tableName, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

	boolean deleteRowInTable(String tableName, String userId)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

	void saveUserPreferencesToAzureTable(String tableName, String userId, String userPrefType,
			String userPreferencesData) throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

	String getUserPreferencesFromAzureTable(String tableName, String userId, String userPrefType) throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

}
