package com.tetrapak.customerhub.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.config.AIPCategoryServiceConfig;

/**
 * Impl class for AIP Category Service.
 */
@Component(immediate = true, service = AIPCategoryService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = AIPCategoryServiceConfig.class)
public class AIPCategoryServiceImpl implements AIPCategoryService {

    /** The config. */
    private AIPCategoryServiceConfig config;

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    public void activate(AIPCategoryServiceConfig config) {

        this.config = config;
    }

    /**
     * Gets the automation trainings id.
     *
     * @return the automation trainings id
     */
    @Override
    public String getAutomationTrainingsId() {

        return config.aipAutomationTrainingsId();
    }

    /**
     * Gets the engineering licenses id.
     *
     * @return the engineering licenses id
     */
    @Override
    public String getEngineeringLicensesId() {
        return config.aipEngineeringLicensesId();
    }

    /**
     * Gets the site licenses id.
     *
     * @return the site licenses id
     */
    @Override
    public String getSiteLicensesId() {
        return config.aipSiteLicensesId();
    }
}
