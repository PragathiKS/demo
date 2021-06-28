package com.tetrapak.publicweb.core.services.impl;

import com.tetrapak.publicweb.core.services.config.CookieDataDomainScriptServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

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
     * @return the site name confmap
     */
    @Override
    public String[] getSiteNameConfMap() {
        return config.siteNameConfMap();
    }

    /**
     * @return the data domain script confmap
     */
    @Override
    public String[] getDataDomainScriptConfMap() {
        return config.dataDomainScriptConfMap();
    }
}
