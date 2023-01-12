package com.tetrapak.customerhub.core.services;

import com.tetrapak.customerhub.core.beans.rebuildingkits.RebuildingKits;
import java.util.List;

public interface RebuildingKitsApiService {

	/**
	 * Method to get Rebuilding Kits List from API GEE API
	 *
	 * 	 @param token  token
	 * 	 @param countryCode countryCode
	 * @return json object
	 */
	List<RebuildingKits> getRebuildingkitsList(String token, String countryCode);

	/**
	 * 
	 * Method to return total number of records
	 * 
	 * @return int
	 */
	int getNoOfRecordsCount();
}
