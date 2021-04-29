package com.tetrapak.customerhub.core.services.impl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.MyEquipmentApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;

/**
 * @author ojaswarn
 * The Class MyEquipmentCountryListApiServiceImpl.
 */
@Component(immediate = true, service = MyEquipmentApiService.class)
public class MyEquipmentApiServiceImpl implements MyEquipmentApiService {

	/** The service. */
	@Reference
	private APIGEEService service;

	/**
	 * Gets the country list.
	 *
	 * @param token the token
	 * @return the country list
	 */
	@Override
	public JsonObject getCountryList(String token) {
		JsonObject jsonResponse = new JsonObject();
		String url = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(service, "myequipment-countrylist");
		return HttpUtil.getJsonObject(token, jsonResponse, url);
	}

	/**
	 * Gets the list of equipments.
	 *
	 * @param token       the token
	 * @param countryCode the country code
	 * @param count       the count
	 * @return the list of equipments
	 */
	@Override
	public JsonObject getListOfEquipments(String token, String countryCode, String count) {
		JsonObject jsonResponse = new JsonObject();
		String url = service.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR
				+ GlobalUtil.getSelectedApiMapping(service, "myequipment-equipmentlist") + "?count=" + count
				+ "&countrycode=" + countryCode;
		return HttpUtil.getJsonObject(token, jsonResponse, url);
	}
}
