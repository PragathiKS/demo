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

    /**
     * Pardot manage pref api URL.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Pardot Manage Preference API URL",
            description = "Pardot Manage Preference API URL",
            type = AttributeType.STRING)
    String pardotManagePrefApiURL();
    
    /**
     * Pardot manage pref api api credentials.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Pardot Manage Preference API credentials",
            description = "Pardot Manage Preference API credentials",
            type = AttributeType.STRING)
    String pardotManagePrefApiCredentials();
	
	/**
    * Pardot subscribers data api URL.
    *
    * @return the string
    */
    @AttributeDefinition(
    		name = "Pardot Subscribers Data API URL",
    		description = "Pardot Subscribers Data API URL",
    		type = AttributeType.STRING)
    String pardotSubscribersDataApiURL();

    /**
    * Pardot subscribers data api credentials.
    *
    * @return the string
    */
    @AttributeDefinition(
    		name = "Pardot Subscribers Data API credentials",
    		description = "Pardot Subscribers Data API credentials",
    		type = AttributeType.STRING)
    String pardotSubscribersDataApiCredentials();
}
