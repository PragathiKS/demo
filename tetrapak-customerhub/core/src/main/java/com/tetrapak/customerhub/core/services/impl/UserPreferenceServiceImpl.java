package com.tetrapak.customerhub.core.services.impl;

import com.microsoft.azure.storage.StorageException;
import com.tetrapak.customerhub.core.services.AzureTableStorageService;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Impl class for User Preference Service
 */
@Component(immediate = true, service = UserPreferenceService.class)
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    @Reference
    private AzureTableStorageService azureTableStorageService;

    /**
     * Retrieve user preferences data on basis of userID and preferenceType
     *
     * @param userId   unique userId of a user
     * @param prefType type of user-preference
     * @return user preferences data
     */
    @Override
    public String getSavedPreferences(String userId, String prefType) {
        String savedPreferences = null;
        if (null != azureTableStorageService && !StringUtils.isBlank(userId) && !StringUtils.isBlank(prefType)) {
            try {
                savedPreferences = azureTableStorageService.getUserPreferencesFromAzureTable(
                        azureTableStorageService.getUserPreferencesTableName(), userId, prefType);
                LOGGER.debug("Got savedPreferences response {} for userID:{}, prefType:{}", userId, prefType,
                        savedPreferences);
            } catch (InvalidKeyException | StorageException | URISyntaxException | RuntimeException | IOException e) {
                LOGGER.error("Some exception occured while retrieving userpref:{} data for userID:{}", prefType, userId,
                        e);
            }
        } else {
            LOGGER.warn("Could not fetch savedPreferences for userID:{}, prefType:{}", userId, prefType);
        }
        return savedPreferences;
    }

    /**
     * Set user preferences in the user table on basis of userID, preferenceType
     *
     * @param userId              unique userId of a user
     * @param prefType            type of user-preference
     * @param userPreferencesData data stored corresponding to the preference type
     * @return true if successful otherwise false
     */
    @Override
    public boolean setPreferences(String userId, String userPrefType, String userPreferencesData) {
        if (null != azureTableStorageService) {
            try {
                azureTableStorageService.saveUserPreferencesToAzureTable(
                        azureTableStorageService.getUserPreferencesTableName(), userId, userPrefType,
                        userPreferencesData);
            } catch (InvalidKeyException | StorageException | URISyntaxException | RuntimeException | IOException e) {
                LOGGER.error("Some exception occured while setting userpref:{} data for userID:{}", userPrefType,
                        userId, e);
                return false;
            }
            return true;
        } else {
            LOGGER.warn("Could not setPreferences for userID {} as azureTableStorageService is not active", userId);
            return false;
        }
    }

}
