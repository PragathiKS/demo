package com.tetrapak.publicweb.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.tetrapak.publicweb.core.services.PadrotService;
import com.tetrapak.publicweb.core.services.config.PadrotServiceConfig;

/**
 * Impl class for PadrotService
 *
 */
@Component(immediate = true, service = PadrotService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = PadrotServiceConfig.class)
public class PadrotServiceImpl implements PadrotService {

    /** The config. */
    private PadrotServiceConfig config;

    /**
     * activate method
     *
     * @param config
     *            Padrot Service configuration
     */
    @Activate
    public void activate(final PadrotServiceConfig config) {

        this.config = config;
    }

    @Override
    public String getBusinesInquiryServiceURL() {
        return config.padrotBusinessInquiryServiceUrl();
    }

}
