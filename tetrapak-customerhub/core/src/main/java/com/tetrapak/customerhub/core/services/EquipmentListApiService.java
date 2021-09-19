package com.tetrapak.customerhub.core.services;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;

public interface EquipmentListApiService {

    /**
     * Method to get Equipment List from TETRA PAK API
     *
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
    //JsonObject getEquipmentList(Equipments paramsRequest, String token);
	JsonObject getEquipmentList(String token);
}
