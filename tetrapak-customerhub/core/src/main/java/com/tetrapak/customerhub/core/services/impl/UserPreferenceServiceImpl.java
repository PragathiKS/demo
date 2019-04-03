package com.tetrapak.customerhub.core.services.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.StorageException;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;

/**
 * Impl class for User Preference Service
 */
@Component(immediate = true, service = UserPreferenceService.class)
public class UserPreferenceServiceImpl implements UserPreferenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);
	private final static String tableName = "tetrapakusertable";
	
	@Reference
	private AzureTableStorageService azureTableStorageService;

	@Override
	public String getSavedPreferences(String userId, String prefType) {
		String savedPreferences = new String[] {}.toString();
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(prefType)) {
			return savedPreferences;
		}
		try {
			savedPreferences = azureTableStorageService.getUserPreferencesFromAzureTable(tableName, userId, prefType);
			LOGGER.debug("Got savedPreferences response {} for userID:{}, prefType:{}", userId, prefType,
					savedPreferences);
		} catch (InvalidKeyException | StorageException | URISyntaxException | RuntimeException | IOException e) {
			LOGGER.error("Some exception occured while retrieving userpref:{} data for userID:{}", prefType, userId, e);
		}
		return savedPreferences;
	}

	@Override
	public boolean setPreferences(String userId, String userPrefType, String userPreferencesData) {
		try {
			azureTableStorageService.saveUserPreferencesToAzureTable(tableName, userId, userPrefType,
					userPreferencesData);
		} catch (InvalidKeyException | StorageException | URISyntaxException | RuntimeException | IOException e) {
			LOGGER.error("Some exception occured while setting userpref:{} data for userID:{}", userPrefType, userId,
					e);
			return false;
		}
		return true;
	}

}
