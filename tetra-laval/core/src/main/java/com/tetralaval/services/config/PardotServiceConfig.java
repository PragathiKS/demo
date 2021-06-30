package com.tetralaval.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface PardotServiceConfig.
 */
@ObjectClassDefinition(name = "Public Web Pardot Service Configuration", description = "Pardot Service Configuration")
public @interface PardotServiceConfig {

    /**
     * Pardot business inquiry service url.
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

    /**
     * Pardot subscribers api URL.
     *
     * @return the string
     */
    @AttributeDefinition(
    		name = "Pardot Subscribers API URL",
    		description = "Pardot Subscribers API URL",
    		type = AttributeType.STRING)
    String pardotSubscribersApiURL();

    /**
     * Pardot token generation api client id.
     *
     * @return the string
     */
    @AttributeDefinition(
    		name = "Pardot Token Generation API Client Id",
    		description = "Pardot Token Generation API Client Id",
    		type = AttributeType.STRING)
    String pardotTokenGenerationApiClientId();
    
    /**
     * Pardot token generation api client secret.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Pardot Token Generation API Client Secret",
            description = "Pardot Token Generation API Client Secret",
            type = AttributeType.STRING)
    String pardotTokenGenerationApiClientSecret();
    
    /**
     * Pardot token generation url.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Pardot Token Generation API URL",
            description = "Pardot Token Generation API URL",
            type = AttributeType.STRING)
    String pardotTokenGenerationUrl();
}
