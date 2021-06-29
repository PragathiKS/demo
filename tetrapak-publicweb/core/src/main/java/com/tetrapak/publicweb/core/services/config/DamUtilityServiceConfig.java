package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for DAM Utility Service
 *
 * @author Aalekh Mathur
 */
@ObjectClassDefinition(name = "Public Web DAM Utility Service Configuration", description = "DAM Utility Service Configuration")
public @interface DamUtilityServiceConfig {

    /**
     * DAM Utility Service root path
     *
     * @return DAM Utility Service root path
     */
    @AttributeDefinition(name = "DAM Utility Service root path", description = "DAM Utility Service root path", type = AttributeType.STRING)
    String DamUtilityRootPath() default "/content/dam/tetrapak/publicweb";

}
