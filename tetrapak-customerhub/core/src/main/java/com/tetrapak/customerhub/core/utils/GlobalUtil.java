package com.tetrapak.customerhub.core.utils;

import com.tetrapak.customerhub.core.services.APIJEEService;

public class GlobalUtil {
    public static String getApiURL(APIJEEService apiJeeService, String defaultJson) {
        return null != apiJeeService ? apiJeeService.getApiJeeServiceUrl() : defaultJson;
    }
}
