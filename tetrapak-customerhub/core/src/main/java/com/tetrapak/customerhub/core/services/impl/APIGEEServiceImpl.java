package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.APIGEEServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Impl class for API GEE Service
 * @author Nitin Kumar
 */
@Component(immediate = true, service = APIGEEService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = APIGEEServiceConfig.class)
public class APIGEEServiceImpl implements APIGEEService {

    private APIGEEServiceConfig config;

    /**
     * activate method
     * @param config
     */
    @Activate
    public void activate(APIGEEServiceConfig config) {

        this.config = config;
    }

    @Override
    public String getApigeeServiceUrl() {

        return config.apigeeServiceUrl();
    }

    @Override
    public String getApigeeClientID() {
        return config.apigeeClientID();
    }

    @Override
    public String getApigeeClientSecret() {
        return config.apigeeClientSecret();
    }
}
