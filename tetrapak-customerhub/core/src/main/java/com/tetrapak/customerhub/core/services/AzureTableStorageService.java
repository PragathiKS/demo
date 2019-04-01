package com.tetrapak.customerhub.core.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.DynamicTableEntity;

public interface AzureTableStorageService {

	CloudTableClient getTableClientReference()
			throws RuntimeException, IOException, URISyntaxException, InvalidKeyException;
	
	CloudTable createTable(CloudTableClient tableClient, String tableName)
			throws StorageException, RuntimeException, IOException, InvalidKeyException, IllegalArgumentException,
			URISyntaxException, IllegalStateException ;
	
	List<DynamicTableEntity> retrieveDataFromTable(CloudTable cloudTable)
			throws InvalidKeyException, RuntimeException, IOException, URISyntaxException;
	
	void insertOrUpdateRowInTable(CloudTable cloudTable, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;
	
	void deleteRowInTable(CloudTable cloudTable, DynamicTableEntity entity)
			throws StorageException, InvalidKeyException, URISyntaxException, RuntimeException, IOException;

}
