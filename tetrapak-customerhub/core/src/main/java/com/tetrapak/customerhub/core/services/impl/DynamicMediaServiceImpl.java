package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.DynamicMediaService;
import com.tetrapak.customerhub.core.services.config.DynamicMediaServiceConfig;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = DynamicMediaService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DynamicMediaServiceConfig.class)
public class DynamicMediaServiceImpl implements DynamicMediaService {

    private DynamicMediaServiceConfig config;

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

   
    @Override
    public String[] getDynamicMediaConfMap() {
        return config.dynamicMediaConfMap();
        
    }
    
    @Override
    public String getRootPath() {
        return config.rootPath();
    }
    

}
