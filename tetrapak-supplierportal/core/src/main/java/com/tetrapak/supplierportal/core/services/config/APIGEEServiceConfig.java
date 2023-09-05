package com.tetrapak.supplierportal.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for API GEE Service
 *
 * @author Sunil Kumar Yadav
 */
@ObjectClassDefinition(name = "SupplierPortal APIGEE Service Configuration", description = "SupplierPortal APIGEE Service Configuration")
public @interface APIGEEServiceConfig {

    /**
     * API GEE Service Url
     *
     * @return API GEE Service Url
     */
    @AttributeDefinition(name = "API GEE Service URL", description = "API GEE Service URL", type = AttributeType.STRING)
    String apigeeServiceUrl();

    /**
     * API GEE  Client ID
     *
     * @return API GEE  Client ID
     */
    @AttributeDefinition(name = "API GEE Service Client ID", description = "API GEE Service Client ID", type = AttributeType.STRING)
    String apigeeClientID();

    /**
     * API GEE  Client Secret
     *
     * @return API GEE  Client Secret
     */
    @AttributeDefinition(name = "API GEE Service Client Secret", description = "API GEE Service Client Secret", type = AttributeType.STRING)
    String apigeeClientSecret();

    /**
     * API GEE Mappings
     *
     * @return API GEE Mappings
     */
    @AttributeDefinition(name = "API Mappings with components", description = "API Mappings with components", type = AttributeType.STRING)
    String[] apiMappings();
}
