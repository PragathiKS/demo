package com.tetrapak.commons.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for TetraPak Dispatcher Flush Service
 *
 * @author Nitin Kumar
 */
@ObjectClassDefinition(name = "TetraPak Dispatcher Flush Service Configuration",
        description = "TetraPak Dispatcher Flush Service Configuration")
public @interface DispatcherFlushConfig {

    @AttributeDefinition(
            name = "absPath",
            description = "event.topics",
            type = AttributeType.STRING
    )
    String absPath();

    @AttributeDefinition(
            name = "dispatcherPath",
            description = "dispatcher Path for which cache needs to be cleared",
            type = AttributeType.STRING
    )
    String dispatcherPath();
}
