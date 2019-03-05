package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.ApiJeeService;
import com.tetrapak.customerhub.core.services.config.ApiJeeServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = ApiJeeService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ApiJeeServiceConfig.class)
public class ApiJeeServiceImpl implements ApiJeeService {

    private ApiJeeServiceConfig config;

    @Activate
    public void activate(ApiJeeServiceConfig config) {
        this.config = config;
    }

    @Override
    public String getApiJeeServiceUrl() {
        return config.apiJeeServiceUrl();
    }
}
