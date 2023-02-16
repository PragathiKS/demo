package com.tetralaval.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.tetralaval.services.CookieDataDomainScriptService;
import com.tetralaval.services.config.CookieDataDomainScriptServiceConfig;

@Component(immediate = true, service = CookieDataDomainScriptService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CookieDataDomainScriptServiceConfig.class)
public class CookieDataDomainScriptServiceImpl implements CookieDataDomainScriptService {

    private CookieDataDomainScriptServiceConfig config;

    /**
     * activate method
     *
     * @param config Cookie Data Domain Script Service configuration
     */
    @Activate
    public void activate(CookieDataDomainScriptServiceConfig config) {

        this.config = config;
    }

    /**
     * @return the data domain script confmap
     */
    @Override
    public String[] getCookieDomainScriptConfig() {
        return config.cookieDomainScriptConfig();
    }
}
