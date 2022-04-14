package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Cots Support Emails
 */
@ObjectClassDefinition(name = "Cots Support Email Configuration", description = "Cots Support Email Configuration")
public @interface CotsSupportEmailConfiguration {
    
    /**
     * Recipient Email Address
     * @return Recipient Email Address
     */
    @AttributeDefinition(name = "Recipient Email Address", description = "Recipient Email Address",
            type = AttributeType.STRING)
    String[] recipientAddresses();
    
    /**
     * Cots Support Email Template Path
     * @return Email Template Path
     */
    @AttributeDefinition(name = "COTS Support Email Template Path", description = "COTS Support Email Template Path",
            type = AttributeType.STRING)
    String emailTemplatePath();

    /**
     * Enable sending of email
     * @return Enable sending of email
     */
    @AttributeDefinition(name = "Enable sending of email", description = "Enable sending of email",
            type = AttributeType.BOOLEAN)
    boolean isCotsSupportEmailEnabled();
}
