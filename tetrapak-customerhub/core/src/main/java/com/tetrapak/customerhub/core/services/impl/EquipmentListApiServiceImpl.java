package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipmentlist.Equipments;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = EquipmentListApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class EquipmentListApiServiceImpl implements EquipmentListApiService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentListApiServiceImpl.class);

    @Reference
    private APIGEEService apigeeService;

    /**
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
    @Override
    //public JsonObject getEquipmentList(Equipments paramsRequest, String token) {
    public JsonObject getEquipmentList(String token) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(apigeeService, "myequipment-equipmentlist")+ "?results=extended";
        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }
}
