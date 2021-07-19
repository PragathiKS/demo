package com.tetralaval.services.impl;

import com.tetralaval.services.ErrorPageRedirectService;
import com.tetralaval.services.config.ErrorPageRedirectConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(immediate = true, service = ErrorPageRedirectService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ErrorPageRedirectConfig.class)
public class ErrorPageRedirectServiceImpl implements ErrorPageRedirectService {

    private ErrorPageRedirectConfig config;

    @Activate
    public void activate(ErrorPageRedirectConfig config) {

        this.config = config;
    }

    @Override
    public String getErrorPagePath() {
        return config.errorPagePath();
    }
}
