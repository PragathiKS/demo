package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.TabNamesMappingService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Implementation class for Tab Names Mapping Service
 *
 * @author Nitin Kumar
 */
@Component(immediate = true, service = TabNamesMappingService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = TabNamesMappingServiceImpl.TabsMappingConfig.class)
public class TabNamesMappingServiceImpl implements TabNamesMappingService {

    /**
     * mapping of URLs and tab names
     */
    @ObjectClassDefinition(name = "Customer Hub Tab Names Configuration", description = "Customer Hub Tab Names Configuration")
    public @interface TabsMappingConfig {

        /**
         * This method return list of mapping
         *
         * @return mapping of URLs and tab names
         */
        @AttributeDefinition(name = "Customer Hub Tab Names Mapping",
                description = "Customer Hub Tab Names Mapping", type = AttributeType.STRING)
        String[] tabNamesMap();
    }

    private TabsMappingConfig config;

    /**
     * Activate method
     *
     * @param config configuration map of navigation URLs and tab names
     */
    @Activate
    public void activate(TabsMappingConfig config) {

        this.config = config;
    }

    @Override
    public String[] getTabNamesMap() {
        return config.tabNamesMap();
    }
}
