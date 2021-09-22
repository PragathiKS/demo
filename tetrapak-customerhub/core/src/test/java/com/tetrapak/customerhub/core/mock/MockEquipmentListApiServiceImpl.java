package com.tetrapak.customerhub.core.mock;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.EquipmentListApiService;

public class MockEquipmentListApiServiceImpl implements EquipmentListApiService {
    private static final String PARAM_STRING = "{\\n  \\\"data\\\": [\\n    {\\n      \\\"id\\\": \\\"8901000009\\\",\\n      \\\"countryCode\\\": \\\"DE\\\",\\n      \\\"countryName\\\": \\\"Germany\\\",\\n      \\\"lineName\\\": \\\"\\\",\\n      \\\"equipmentStatus\\\": \\\"Produced New Machine\\\",\\n      \\\"isSecondhand\\\": \\\"false\\\",\\n      \\\"equipmentType\\\": \\\"CE_AUX\\\",\\n      \\\"equipmentTypeDesc\\\": \\\"CE Auxiliary Eq\\\",\\n     \\\"functionalLocation\\\": \\\"DEC-PARMALATBERLI-00008\\\",\\n     \\\"functionalLocationDesc\\\": \\\"\\\",\\n     \\\"serialNumber\\\": \\\"8901000009\\\",\\n     \\\"siteName\\\": \\\"PARMALATBERLI\\\",\\n     \\\"siteDesc\\\": \\\"\\\",\\n     \\\"permanentVolumeConversion\\\": \\\"false\\\",\\n     \\\"position\\\": \\\"\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"material\\\": \\\"228496-0302\\\",\\n     \\\"machineSystem\\\": \\\"CE Other\\\",\\n     \\\"materialDesc\\\": \\\"PLATFORM\\\",\\n     \\\"manufacturerModelNumber\\\": \\\"3543\\\",\\n     \\\"manufacturerSerialNumber\\\": \\\"\\\",\\n     \\\"superiorEquipment\\\": \\\"9901000043\\\",\\n     \\\"superiorEquipmentName\\\": \\\"R2 200 TETRA BRIK MACHINE TBA/19\\\",\\n     \\\"superiorEquipmentSerialNumber\\\": \\\"64816/00008\\\",\\n     \\\"manufacturer\\\": \\\"Weisel\\\",\\n     \\\"manufacturerCountry\\\": \\\"DE\\\",\\n     \\\"constructionYear\\\": \\\"\\\",\\n     \\\"customerWarrantyStartDate\\\": \\\"\\\",\\n     \\\"customerWarrantyEndDate\\\": \\\"\\\",\\n     \\\"businessType\\\": \\\"Packaging\\\",\\n     \\\"equipmentCategory\\\": \\\"E\\\",\\n     \\\"equipmentCategoryDesc\\\": \\\"External equipment (3rd Party)\\\",\\n     \\\"eofsConfirmationDate\\\": \\\"\\\",\\n     \\\"eofsValidFromDate\\\": \\\"\\\" }    ]\\n}";

    @Override
    public JsonObject getEquipmentList(String token, String countryCode) {
    	JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty(CustomerHubConstants.RESULT, PARAM_STRING);
        jsonResponse.addProperty("status", 200);
        return jsonResponse;
    }
    
    @Override
    public int getNoOfRecordsCount() {
    	return 750;
    }
}
