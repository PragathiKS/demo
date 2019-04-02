package com.tetrapak.customerhub.core.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.DynamicTableEntity;

public interface AzureTableStorageService {
	
	void createTable( String tableName)
			throws StorageException, RuntimeException, IOException, InvalidKeyException, IllegalArgumentException,
			URISyntaxException, IllegalStateException ;
	
	List<DynamicTableEntity> retrieveDataFromTable(String tableName, String userId)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException, StorageException;
	
	void insertOrUpdateRowInTable(String tableName, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;
	
	boolean deleteRowInTable(String tableName, String userId)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

}
