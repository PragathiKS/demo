package com.tetrapak.publicweb.core.services.impl;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.config.DynamicMediaServiceConfig;
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
