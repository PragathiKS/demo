package com.tetrapak.publicweb.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.tetrapak.publicweb.core.services.SiteDomainService;

/**
 * The Class SiteDomainServiceImpl.
 */
@Component(immediate = true, service = SiteDomainService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SiteDomainServiceImpl.SiteDomainServiceConfig.class)
public class SiteDomainServiceImpl implements SiteDomainService {

    /** The config. */
    private SiteDomainServiceConfig config;

    /**
     * The Interface SiteDomainServiceConfig.
     */
    @ObjectClassDefinition(
            name = "Domain Configuration",
            description = "Domain configuration for the respective environment")
    @interface SiteDomainServiceConfig {

        /**
         * Gets the domain name.
         *
         * @return the domain name
         */
        @AttributeDefinition(name = "Domain Name", description = "Domain for the respective environment")
        String domainName() default "";

    }

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    public void activate(SiteDomainServiceConfig config) {
        this.config = config;
    }

    /**
     * Gets the domain.
     *
     * @return the domain
     */
    @Override
    public String getDomain() {
        return config.domainName();
    }

}
