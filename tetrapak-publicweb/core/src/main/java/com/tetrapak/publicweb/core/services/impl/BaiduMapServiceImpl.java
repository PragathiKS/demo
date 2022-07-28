package com.tetrapak.publicweb.core.services.impl;


import com.tetrapak.publicweb.core.services.BaiduMapService;
import com.tetrapak.publicweb.core.services.config.BaiduMapServiceConfig;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Impl class for BaiduMapService.
 */
@Component(immediate = true, service = BaiduMapService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = BaiduMapServiceConfig.class)
public class BaiduMapServiceImpl implements BaiduMapService{

    /** The config. */
    private BaiduMapServiceConfig config;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    public void activate(final BaiduMapServiceConfig config) {
        this.config = config;
    }

    /**
     * Gets the Baidu Map Key.
     *
     * @return the Baidu Map Key
     */
    @Override
    public String getBaiduMapKey() {
        return config.baiduMapKey();
    }

}
