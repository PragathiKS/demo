package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface AIPCategoryServiceConfig.
 */
@ObjectClassDefinition(name = "AIP Category Service Configuration", description = "AIP Category Service Configuration")
public @interface AIPCategoryServiceConfig {

    /**
     * AIP automation trainings id.
     *
     * @return the string
     */
    @AttributeDefinition(name = "AIP Automation Trainings Category ID", description = "AIP Automation Trainings Category ID", type = AttributeType.STRING)
    String aipAutomationTrainingsId();
    
    /**
     * AIP engineering licenses id.
     *
     * @return the string
     */
    @AttributeDefinition(name = "AIP Engineering Licenses Category ID", description = "AIP Engineering Licenses Category ID", type = AttributeType.STRING)
    String aipEngineeringLicensesId();
    
    /**
     * AIP site licenses id.
     *
     * @return the string
     */
    @AttributeDefinition(name = "AIP Site Licenses Category ID", description = "AIP Site Licenses Category ID", type = AttributeType.STRING)
    String aipSiteLicensesId();

}
