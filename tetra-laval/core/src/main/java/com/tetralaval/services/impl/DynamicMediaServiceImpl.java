package com.tetralaval.services.impl;

import com.tetralaval.config.DynamicMediaServiceConfig;
import com.tetralaval.services.DynamicMediaService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = DynamicMediaService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DynamicMediaServiceConfig.class)
public class DynamicMediaServiceImpl implements DynamicMediaService {

    private DynamicMediaServiceConfig config;

    /**
     * activate method
     *
     * @param config Dynamic Media Service configuration
     */
    @Activate
    public void activate(DynamicMediaServiceConfig config) {

        this.config = config;
    }

    /**
     * @return the image service url
     */
    @Override
    public String getImageServiceUrl() {
        return config.imageServiceUrl();
    }

    /**
     * @return the video service url
     */
    @Override
    public String getVideoServiceUrl() {
        return config.videoServiceUrl();
    }

    /**
     * @return the dynamic media confMap
     */
    @Override
    public String[] getDynamicMediaConfMap() {
        return config.dynamicMediaConfMap();
    }
}

