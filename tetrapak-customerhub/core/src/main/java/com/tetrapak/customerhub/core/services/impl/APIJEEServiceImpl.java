package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.APIJEEService;
import com.tetrapak.customerhub.core.services.config.APIJEEServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = APIJEEService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = APIJEEServiceConfig.class)
public class APIJEEServiceImpl implements APIJEEService {

    private APIJEEServiceConfig config;

    @Activate
    public void activate(APIJEEServiceConfig config) {
        this.config = config;
    }

    @Override
    public String getApiJeeServiceUrl() {
        return config.apiJeeServiceUrl();
    }
}
