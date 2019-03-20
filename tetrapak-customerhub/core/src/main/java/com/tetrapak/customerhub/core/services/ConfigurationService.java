package com.tetrapak.customerhub.core.services;

import java.util.Map;

public interface ConfigurationService {

    Map<String, String> getDynamicMediaConfMap();

    String getImageServiceUrl();
    
}
