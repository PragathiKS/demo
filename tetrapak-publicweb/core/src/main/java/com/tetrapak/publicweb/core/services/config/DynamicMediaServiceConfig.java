package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Public Web Dynamic Media Service Configuration", description = "Public Web Dynamic Media Service Configuration")
public @interface DynamicMediaServiceConfig {
    
    @AttributeDefinition(name = "Dynamic Media Service Image Service Url", description = "Dynamic Media Image Service Url", type = AttributeType.STRING)
    String imageServiceUrl();
    
    @AttributeDefinition(name = "Dynamic Media Service Components Configuration Map", description = "Dynamic Media Service Components Configuration Map", type = AttributeType.STRING)
    String[] dynamicMediaConfMap();
    
    @AttributeDefinition(name = "Dynamic Media Service Root Path", description = "Dynamic Media Service Root Path", type = AttributeType.STRING)
    String rootPath();
}
