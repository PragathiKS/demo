package com.tetrapak.commons.core.services.config;

import org.apache.sling.api.resource.observation.ResourceChangeListener;
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
            name = ResourceChangeListener.PATHS,
            description = "Configurable paths for event listener",
            type = AttributeType.STRING
    )
    String[] getPaths();

    @AttributeDefinition(
            name = ResourceChangeListener.CHANGES,
            description = "Event types",
            type = AttributeType.STRING
    )
    String[] getEventTypes();

}
