package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for AIP Emails
 */
@ObjectClassDefinition(name = "AIP Email Configuration", description = "AIP Email Configuration")
public @interface AIPEmailConfiguration {
    
    /**
     * Recipient Email Address
     * @return Recipient Email Address
     */
    @AttributeDefinition(name = "Recipient Email Address", description = "Recipient Email Address",
            type = AttributeType.STRING)
    String[] recipientAddresses();
    
    /**
     * Email Template Path
     * @return Email Template Path
     */
    @AttributeDefinition(name = "COTS Support Email Template Path", description = "COTS Support Email Template Path",
            type = AttributeType.STRING)
    String emailTemplatePath();

    /**
     * Engineering License Email Template Path
     * @return Engineering License Email Template Path
     */
    @AttributeDefinition(name = "Engineering License Email Template Path", description = "Engineering License Email Template Path",
            type = AttributeType.STRING)
    String engineeringLicenseEmailTemplatePath();

    /**
     * Site License Email Template Path
     * @return Site License Email Template Path
     */
    @AttributeDefinition(name = "Site License Email Template Path", description = "Site License Email Template Path",
            type = AttributeType.STRING)
    String siteLicenseEmailTemplatePath();
    
    /**
     * Enable sending of email
     * @return Enable sending of email
     */
    @AttributeDefinition(name = "Enable sending of email", description = "Enable sending of email",
            type = AttributeType.BOOLEAN)
    boolean isCotsSupportEmailEnabled();

    /**
     * Enable sending of email
     * @return Enable sending of email
     */
    @AttributeDefinition(name = "Enable sending of email", description = "Enable sending of email",
            type = AttributeType.BOOLEAN)
    boolean isLicensesEmailEnabled();
}
