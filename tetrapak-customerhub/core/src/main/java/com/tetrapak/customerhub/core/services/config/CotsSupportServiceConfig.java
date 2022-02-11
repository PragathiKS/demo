package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for COTS Support Service
 *
 */
@ObjectClassDefinition(name = "API GEE Service Configuration", description = "API GEE Service Configuration")
public @interface CotsSupportServiceConfig {

    /**
     * Recipient Email Address
     *
     * @return Recipient Email Address
     */
    @AttributeDefinition(name = "Recipient Email Address", description = "Recipient Email Address", type = AttributeType.STRING)
    String[] recipientAddresses();

    /**
     * Email Template Path
     *
     * @return Email Template Path
     */
    @AttributeDefinition(name = "Email Template Path", description = "Email Template Path", type = AttributeType.STRING)
    String emailTemplatePath();
}
