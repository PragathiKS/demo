package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.SiteImproveScriptService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Impl class for site Improve Script URL Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = SiteImproveScriptService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SiteImproveScriptServiceImpl.SiteImproveScriptServiceConfig.class)
public class SiteImproveScriptServiceImpl implements SiteImproveScriptService {

    private SiteImproveScriptServiceConfig config;

    @Override
    public String getSiteImproveScriptUrl() {
        return config.siteImproveScriptUrl();
    }

    @ObjectClassDefinition(name = "URL Service Configuration for site Improve Script",
            description = "URL Service Configuration for site Improve Script")
    @interface SiteImproveScriptServiceConfig {

        /**
         * Returns siteImproveScript URL
         *
         * @return siteImproveScript URL
         */
        @AttributeDefinition(name = "site Improve Script URL", description = "site Improve Script URL", type = AttributeType.STRING)
        String siteImproveScriptUrl();

    }

    /**
     * activate method
     *
     * @param config site Improve Script URL configuration
     */
    @Activate
    public void activate(SiteImproveScriptServiceConfig config) {
        this.config = config;
    }


}
