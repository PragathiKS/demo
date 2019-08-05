package com.tetrapak.customerhub.core.services;

/**
 * Dynamic Media Service class
 */
public interface DynamicMediaService {

    String[] getDynamicMediaConfMap();

    String getImageServiceUrl();

    String getVideoServiceUrl();

    String getRootPath();
    
}
