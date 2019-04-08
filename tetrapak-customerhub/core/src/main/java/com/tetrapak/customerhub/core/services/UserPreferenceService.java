package com.tetrapak.customerhub.core.services;

/**
 * User preference Service Interface
 */
public interface UserPreferenceService {

	/**
	 * 
	 * Retrieve user preferences data on basis of userID and preferenceType
	 * 
	 * @param userId   unique userId of a user
	 * @param prefType type of user-preference
	 * 
	 * @return user preferences data
	 */
	String getSavedPreferences(String userId, String prefType);

	/**
	 * 
	 * Set user preferences in the user table on basis of userID, preferenceType
	 * 
	 * @param userId              unique userId of a user
	 * @param prefType            type of user-preference
	 * 
	 * @param userPreferencesData data stored corresponding to the preference type
	 * @return true if successful otherwise false
	 */
	boolean setPreferences(String userId, String prefType, String userPreferencesData);

}
