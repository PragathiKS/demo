package com.tetrapak.customerhub.core.services;

/**
 * Dynamic Media Service class
 *
 * @author Nitin Kumar
 */
public interface DynamicMediaService {

    String[] getDynamicMediaConfMap();

    String getImageServiceUrl();

    String getVideoServiceUrl();

    String getRootPath();

}
