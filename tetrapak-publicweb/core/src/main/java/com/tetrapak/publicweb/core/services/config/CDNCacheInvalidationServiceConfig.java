package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for CDN Cache Invalidation Service
 *
 */
@ObjectClassDefinition(
        name = "Public Web CDN Cache Invalidation Service Configuration",
        description = "CDN Cache Invalidation Service Configuration")
public @interface CDNCacheInvalidationServiceConfig {

    @AttributeDefinition(
            name = "Content Path",
            description = "Content Path for CDN Cache Invalidation, For example : /content/tetrapak/publicweb",
            type = AttributeType.STRING)
    String contentPathForCDNCacheInvalidation();

    @AttributeDefinition(
            name = "DAM Path",
            description = "DAM Path for CDN Cache Invalidation, For example : /content/dam/tetrapak",
            type = AttributeType.STRING)
    String damPathForCDNCacheInvalidation();

    @AttributeDefinition(
            name = "Domain",
            description = "Domain for CDN Cache Invalidation, For example : http://www.tetrapak.com",
            type = AttributeType.STRING)
    String domainForCDN();

    @AttributeDefinition(
            name = "AEM Content path to URL mapping",
            description = "Mapping for AEM content path to CDN path. Example 'en-gb=/gb/en'",
            type = AttributeType.STRING)
    String[] urlMapping();

}
