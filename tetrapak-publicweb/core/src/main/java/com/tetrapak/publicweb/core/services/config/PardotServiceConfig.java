package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface PardotServiceConfig.
 */
@ObjectClassDefinition(name = "Public Web Pardot Service Configuration", description = "Pardot Service Configuration")
public @interface PardotServiceConfig {

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
    
    /**
     * Custom form service url.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Custom Token Generation URL",
            description = "Custom Token Generation URL",
            type = AttributeType.STRING)
    String customTokenGenerationUrl();
    
    /**
     * Custom form service url.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Custom Form Service URL",
            description = "Custom Form Service URL",
            type = AttributeType.STRING)
    String customFormServiceUrl();

    /**
     * Custom form service client ID.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Custom Form Service Client ID",
            description = "Custom Form Service Client ID",
            type = AttributeType.STRING)
    String customFormServiceClientID();

    /**
     * Custom form service client secret.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Custom Form Service Client Secret",
            description = "Custom Form Service Client Secret",
            type = AttributeType.STRING)
    String customFormServiceClientSecret();
}
