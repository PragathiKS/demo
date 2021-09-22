package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;

public interface EquipmentListApiService {

    /**
     * Method to get Equipment List from API GEE API
     *
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
	JsonObject getEquipmentList(String token, String countryCode);
	
	int getNoOfRecordsCount();
}
