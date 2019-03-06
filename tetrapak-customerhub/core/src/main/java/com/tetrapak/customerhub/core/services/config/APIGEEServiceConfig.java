package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "API JEE Service Configuration", description = "API JEE Service Configuration")
public @interface APIGEEServiceConfig {

    @AttributeDefinition(name = "API JEE Service URL", description = "API JEE Servie URL", type = AttributeType.STRING)
    String apigeeServiceUrl();
}
