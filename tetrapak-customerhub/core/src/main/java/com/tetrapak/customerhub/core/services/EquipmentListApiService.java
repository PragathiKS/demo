package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;

import java.util.List;

public interface EquipmentListApiService {

	/**
	 * Method to get Equipment List from API GEE API
	 *
	 * @param paramsRequest params
	 * @param token         token
	 * @return json object
	 */
	List<Equipments> getEquipmentList(String token, String countryCode);

	int getNoOfRecordsCount();
}
