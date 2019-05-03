package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.services.config.DynamicMediaServiceConfig;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Impl class for Dynamic Media Service
 * @author Ruhee Sharma
 */
@Component(immediate = true, service = DynamicMediaService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DynamicMediaServiceConfig.class)
public class DynamicMediaServiceImpl implements DynamicMediaService {

    private DynamicMediaServiceConfig config;

    /**
     * activate method
     * @param config Dynamic Media Service configuration
     */
    @Activate
    public void activate(DynamicMediaServiceConfig config) {

        this.config = config;
    }
 
    /**
     * Sets the Image service url. return Image Service URL
     * @return the image service url
     */
    @Override
    public String getImageServiceUrl() {
        return config.imageServiceUrl();
    }

    /**
     * Sets the Dynamic Media Conf Map. return Dynamic Media Conf Map
     * @return the dynamic media confMap
     */   
    @Override
    public String[] getDynamicMediaConfMap() {
        return config.dynamicMediaConfMap();
        
    }
    
    /**
     * Sets the Root Path. return Root Path
     * @return the root Path
     */  
    @Override
    public String getRootPath() {
        return config.rootPath();
    }
    

}
