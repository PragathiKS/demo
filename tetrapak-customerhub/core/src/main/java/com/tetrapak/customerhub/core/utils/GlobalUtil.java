package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.services.APIGEEService;

public class GlobalUtil {
    public static String getApiURL(APIGEEService apiJeeService, String defaultJson) {
        return null != apiJeeService ? apiJeeService.getApigeeServiceUrl() : defaultJson;
    }
}
