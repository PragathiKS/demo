package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Dynamic Media Service Configuration", description = "Dynamic Media Service Configuration")
public @interface DynamicMediaServiceConfig {
    
    @AttributeDefinition(name = "Dynamic Media Service Image Service Url", description = "Dynamic Media Image Service Url", type = AttributeType.STRING)
    String imageServiceUrl();
    
    @AttributeDefinition(name = "Dynamic Media Service Dynamic Media Configuration Map", description = "Dynamic Media Service Dynamic Media Configuration Map", type = AttributeType.STRING)
    String[] dynamicMediaConfMap();
    
    @AttributeDefinition(name = "Dynamic Media Service Root Path", description = "Dynamic Media Service Root Path", type = AttributeType.STRING)
    String rootPath();
}
