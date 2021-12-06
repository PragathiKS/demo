package com.tetrapak.customerhub.core.services;

import java.util.List;

import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;

public interface EquipmentListApiService {

	/**
	 * Method to get Equipment List from API GEE API
	 *
	 * @param paramsRequest params
	 * @param token         token
	 * @return json object
	 */
	List<Equipments> getEquipmentList(String token, String countryCode);

	/**
	 * 
	 * Method to return total number of records
	 * 
	 * @return int
	 */
	int getNoOfRecordsCount();
}
