package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

/**
 * @author ojaswarn
 * The Interface MyEquipmentCountryListApiService.
 */
public interface MyEquipmentApiService {
	
	/**
	 * Gets the country list.
	 *
	 * @param token the token
	 * @return the country list
	 */
	JsonObject getCountryList(String token);
	
	/**
	 * Gets the list of equipments.
	 *
	 * @param token the token
	 * @param countryCode the country code
	 * @param count the count
	 * @return the list of equipments
	 */
	JsonObject getListOfEquipments(String token, String countryCode, String count);
}
