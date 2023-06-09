package com.tetralaval.services;


/**
 * The Interface DynamicMediaService.
 */
public interface DynamicMediaService {

    /**
     * Gets the dynamic media conf map.
     *
     * @return the dynamic media conf map
     */
    String[] getDynamicMediaConfMap();

    /**
     * Gets the image service url.
     *
     * @return the image service url
     */
    String getImageServiceUrl();

    /**
     * Gets the video service url.
     *
     * @return the video service url
     */
    String getVideoServiceUrl();
}
