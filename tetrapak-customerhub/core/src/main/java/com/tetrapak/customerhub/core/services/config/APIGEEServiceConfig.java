package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "API JEE Service Configuration", description = "API JEE Service Configuration")
public @interface APIGEEServiceConfig {

    @AttributeDefinition(name = "API JEE Service URL", description = "API JEE Service URL", type = AttributeType.STRING)
    String apigeeServiceUrl();

    @AttributeDefinition(name = "API JEE Service Client ID", description = "API JEE Service Client ID", type = AttributeType.STRING)
    String apigeeClientID();

    @AttributeDefinition(name = "API JEE Service Client Secret", description = "API JEE Service Client Secret", type = AttributeType.STRING)
    String apigeeClientSecret();
}
