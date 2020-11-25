package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Pardot Service.
 */
@ObjectClassDefinition(name = "Public Web Pardot Service Configuration", description = "Pardot Service Configuration")
public @interface PardotServiceConfig {

    /**
     * Pardot BusinessInquiry Service Url.
     *
     * @return Pardot BusinessInquiry Service Url
     */
    @AttributeDefinition(
            name = "Pardot BusinessInquiry form handler URL",
            description = "Pardot BusinessInquiry Form URL",
            type = AttributeType.STRING)
    String pardotBusinessInquiryServiceUrl();

    /**
     * Pardot subscription form URL.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Pardot Subscription form handler URL",
            description = "Pardot Subscription Form URL",
            type = AttributeType.STRING)
    String pardotSubscriptionFormURL();

}
