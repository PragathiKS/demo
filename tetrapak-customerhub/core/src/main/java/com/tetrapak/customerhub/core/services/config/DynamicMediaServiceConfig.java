package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Dynamic Media Service
 * @author Ruhee Sharma
 * @author Nitin Kumar
 */
@ObjectClassDefinition(name = "Dynamic Media Service Configuration",
        description = "Dynamic Media Service Configuration")
public @interface DynamicMediaServiceConfig {
   
    /**
     * Image Service Url
     * @return Image Service Url
     */
    @AttributeDefinition(name = "Dynamic Media Image Service Url",
            description = "Dynamic Media Image Service Url", type = AttributeType.STRING)
    String imageServiceUrl();

    /**
     * Video Service Url
     * @return Video Service Url
     */
    @AttributeDefinition(name = "Dynamic Media Video Service Url",
            description = "Dynamic Media Video Service Url", type = AttributeType.STRING)
    String videoServiceUrl();

    /**
     * Dynamic Media Conf Map
     * @return Dynamic Media Configuration Map
     */ 
    @AttributeDefinition(name = "Dynamic Media Service Configuration Map",
            description = "Dynamic Media Service Configuration Map", type = AttributeType.STRING)
    String[] dynamicMediaConfMap();
    
    /**
     * Dynamic Media Service Root Path
     * @return Dynamic Media Service Root Path
     */
    @AttributeDefinition(name = "Dynamic Media Service Root Path",
            description = "Dynamic Media Service Root Path", type = AttributeType.STRING)
    String rootPath();
}
