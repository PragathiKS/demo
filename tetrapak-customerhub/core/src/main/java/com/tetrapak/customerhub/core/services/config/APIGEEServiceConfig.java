package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "API GEE Service Configuration", description = "API GEE Service Configuration")
public @interface APIGEEServiceConfig {

    @AttributeDefinition(name = "API GEE Service URL", description = "API GEE Service URL", type = AttributeType.STRING)
    String apigeeServiceUrl();

    @AttributeDefinition(name = "API GEE Service Client ID", description = "API GEE Service Client ID", type = AttributeType.STRING)
    String apigeeClientID();

    @AttributeDefinition(name = "API GEE Service Client Secret", description = "API GEE Service Client Secret", type = AttributeType.STRING)
    String apigeeClientSecret();
}
