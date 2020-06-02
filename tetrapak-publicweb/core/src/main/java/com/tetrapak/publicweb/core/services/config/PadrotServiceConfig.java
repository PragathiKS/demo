package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Padrot Service
 *
 */
@ObjectClassDefinition(
        name = "Public Web Padrot Service Configuration",
        description = "Padrot Service Configuration")
public @interface PadrotServiceConfig {

    /**
     * Padrot BusinessInquiry Service Url
     *
     * @return Padrot BusinessInquiry Service Url
     */
    @AttributeDefinition(
            name = "Padrot BusinessInquiry Service URL",
            description = "Padrot BusinessInquiry Service URL",
            type = AttributeType.STRING)
    String padrotBusinessInquiryServiceUrl();

}
