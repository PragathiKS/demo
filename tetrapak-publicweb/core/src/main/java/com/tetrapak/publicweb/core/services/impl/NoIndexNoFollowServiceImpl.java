package com.tetrapak.publicweb.core.services.impl;
import com.tetrapak.publicweb.core.services.NoIndexNoFollowService;
import com.tetrapak.publicweb.core.services.config.NoIndexNoFollowConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Impl class for NoIndexNoFollowService.
 */
@Component(immediate = true, service = NoIndexNoFollowService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = NoIndexNoFollowConfig.class)
public class NoIndexNoFollowServiceImpl implements NoIndexNoFollowService{

    /** The config. */
    private NoIndexNoFollowConfig config;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    public void activate(final NoIndexNoFollowConfig config) {
        this.config = config;
    }

    @Override
    public String[] getApplicationName() {
        return config.applicationName();
    }
}
