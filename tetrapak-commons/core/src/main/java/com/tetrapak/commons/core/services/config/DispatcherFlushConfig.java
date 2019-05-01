package com.tetrapak.commons.core.services.config;

import org.osgi.service.event.EventConstants;
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
            name = EventConstants.EVENT_TOPIC,
            description = "event.topics",
            type = AttributeType.STRING
    )
    String[] getEvents();

    @AttributeDefinition(
            name = EventConstants.EVENT_FILTER,
            description = "event.filter",
            type = AttributeType.STRING
    )
    String[] getEventFilter();

    @AttributeDefinition(
            name = "dispatcherPath",
            description = "dispatcher Path",
            type = AttributeType.STRING
    )
    String getDispatcherPath();

}
