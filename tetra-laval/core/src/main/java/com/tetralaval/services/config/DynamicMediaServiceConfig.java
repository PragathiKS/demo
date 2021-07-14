package com.tetralaval.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Tetra Laval Dynamic Media Service Configuration",
        description = "Tetra Laval Dynamic Media Service Configuration")
public @interface DynamicMediaServiceConfig {

    /**
     * Image Service Url
     * @return Image Service Url
     */
    @AttributeDefinition(name = "Dynamic Media Image Service Url",
            description = "Dynamic Media Image Service Url", type = AttributeType.STRING)
    String imageServiceUrl() default "https://s7g10.scene7.com/is/image";

    /**
     * Video Service Url
     * @return Video Service Url
     */
    @AttributeDefinition(name = "Dynamic Media Video Service Url",
            description = "Dynamic Media Video Service Url", type = AttributeType.STRING)
    String videoServiceUrl() default "https://s7g10.scene7.com/is/content";

    /**
     * Dynamic Media Conf Map
     * @return Dynamic Media Configuration Map
     */ 
    @AttributeDefinition(name = "Dynamic Media Service Configuration Map",
            description = "Dynamic Media Service Configuration Map", type = AttributeType.STRING)
    String[] dynamicMediaConfMap() default {"header-desktop=158,24", "header-mobileL=138,31", "header-mobileP=138,31"};
}
