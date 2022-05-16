package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Licenses Emails
 */
@ObjectClassDefinition(name = "Plant Master Licenses Email Configuration", description = "Plant Master Licenses Email Configuration")
public @interface PlantMasterLicensesEmailConfiguration {
    
    /**
     * Recipient Email Address
     * @return Recipient Email Address
     */
    @AttributeDefinition(name = "Recipient Email Address", description = "Recipient Email Address",
            type = AttributeType.STRING)
    String[] recipientAddresses();

    /**
     * Active License Withdrawal Recipient Email Address
     * @return Recipient Email Address
     */
    @AttributeDefinition(name = "Recipient Email Address", description = "Recipient Email Address",
            type = AttributeType.STRING)
    String[] withdrawalRequestRecipientAddresses();
    
    /**
     * Enable sending of email
     * @return Enable sending of email
     */
    @AttributeDefinition(name = "Enable sending of email", description = "Enable sending of email",
            type = AttributeType.BOOLEAN)
    boolean isLicensesEmailEnabled();
}
