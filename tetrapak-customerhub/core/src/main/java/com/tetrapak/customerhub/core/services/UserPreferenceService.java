package com.tetrapak.customerhub.core.services;

import org.apache.sling.api.resource.Resource;

import java.util.Set;

/**
 * User preference Service Interface
 */
public interface UserPreferenceService {

	/**
	 * 
	 * Retrieve user preferences data on basis of userID and preferenceType
	 * 
	 * @param prefType
	 * @param resource sling resource
	 * @return
	 */
	String getSavedPreferences(String userId, String prefType);

	/**
	 * 
	 * Set user preferences in the user table on basis of userID, preferenceType 
	 * 
	 * @param userId
	 * @param prefType
	 * @param userPreferencesData
	 * @return
	 */
	boolean setPreferences(String userId, String prefType, String userPreferencesData);

}
