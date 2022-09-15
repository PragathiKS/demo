package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

/**
 * OSGi configuration for DAM Utility Service
 *
 * @author Aalekh Mathur
 */
@ObjectClassDefinition(name = "Customer Hub DAM Utility Service Configuration", description = "DAM Utility Service Configuration")
public @interface DamUtilityServiceConfig {

    /**
     * DAM Utility Service root path
     *
     * @return DAM Utility Service root path
     */
    @AttributeDefinition(name = "DAM Utility Service root path", description = "DAM Utility Service root path", type = AttributeType.STRING)
    String DamUtilityRootPath() default CustomerHubConstants.PW_CONTENT_DAM_PATH;

}
