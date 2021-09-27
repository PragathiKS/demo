package com.tetrapak.customerhub.core.services.impl;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;
import com.tetrapak.customerhub.core.services.config.EquipmentListApiServiceConfig;
import com.tetrapak.customerhub.core.utils.GlobalUtil;
import com.tetrapak.customerhub.core.utils.HttpUtil;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = EquipmentListApiService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = EquipmentListApiServiceConfig.class) public class EquipmentListApiServiceImpl
        implements EquipmentListApiService {

    @Reference private APIGEEService apigeeService;

    private EquipmentListApiServiceConfig config;

    /**
     * activate method
     *
     * @param config API GEE Service configuration
     */
    @Activate public void activate(EquipmentListApiServiceConfig config) {

        this.config = config;
    }

    /**
     * @param
     * @return number of records to fetch
     */
    @Override public int getNoOfRecordsCount() {

        return config.noOfRecords();
    }

    /**
     * @param paramsRequest params
     * @param token         token
     * @return json object
     */
    @Override public JsonObject getEquipmentList(String token, String countryCode) {
        JsonObject jsonResponse = new JsonObject();
        final String url = apigeeService.getApigeeServiceUrl() + CustomerHubConstants.PATH_SEPARATOR + GlobalUtil
                .getSelectedApiMapping(apigeeService, "myequipment-equipmentlist") + CustomerHubConstants.QUESTION_MARK
                + CustomerHubConstants.COUNTRY_CODE + CustomerHubConstants.EQUALS + countryCode
                + CustomerHubConstants.AMPERSAND + CustomerHubConstants.DOWNLOAD_EQUIPMENT_EXCEL_API_PARAMETER
                + CustomerHubConstants.AMPERSAND + CustomerHubConstants.COUNT + CustomerHubConstants.EQUALS
                + getNoOfRecordsCount();
        return HttpUtil.getJsonObject(token, jsonResponse, url);
    }
}
