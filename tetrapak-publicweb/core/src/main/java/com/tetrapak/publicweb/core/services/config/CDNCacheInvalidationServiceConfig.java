package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for CDN Cache Invalidation Service.
 */
@ObjectClassDefinition(
        name = "Public Web CDN Cache Invalidation Service Configuration",
        description = "CDN Cache Invalidation Service Configuration")
public @interface CDNCacheInvalidationServiceConfig {

    /**
     * Content path for CDN cache invalidation.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Content Path",
            description = "Content Path for CDN Cache Invalidation, For example : /content/tetrapak/publicweb",
            type = AttributeType.STRING)
    String contentPathForCDNCacheInvalidation();

    /**
     * Dam path for CDN cache invalidation.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "DAM Path",
            description = "DAM Path for CDN Cache Invalidation, For example : /content/dam/tetrapak",
            type = AttributeType.STRING)
    String damPathForCDNCacheInvalidation();

    /**
     * Domain for CDN.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Domain",
            description = "Domain for CDN Cache Invalidation, For example : http://www.tetrapak.com",
            type = AttributeType.STRING)
    String domainForCDN();

    /**
     * Url mapping.
     *
     * @return the string[]
     */
    @AttributeDefinition(
            name = "AEM Content path to URL mapping",
            description = "Mapping for AEM content path to CDN path. Example 'en-gb=/gb/en'",
            type = AttributeType.STRING)
    String[] urlMapping();

    /**
     * DS Domain for CDN.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "DS Domain",
            description = "Design System Domain for CDN Cache Invalidation, For example : http://design.tetrapak.com",
            type = AttributeType.STRING)
    String dsDomainForCDN();

    /**
     * Online Help Domain for CDN.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Online Help Domain",
            description = "Online Help Domain for CDN Cache Invalidation, For example : http://editor-guide.tetrapak.com",
            type = AttributeType.STRING)
    String onlineHelpDomainForCDN();

    /**
     * Tetra laval domain for CDN.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "Tetra Laval Domain",
            description = "Tetra Laval Domain for CDN Cache Invalidation, For example : http://www.tetralaval.com",
            type = AttributeType.STRING)
    String tetraLavalDomainForCDN();

    @AttributeDefinition(
            name = "XF Path",
            description = "Experience Fragments Path for CDN Cache Invalidation, For example : /content/experience-fragments/publicweb",
            type = AttributeType.STRING)
    String xfPathForCDNCacheInvalidation();

}
