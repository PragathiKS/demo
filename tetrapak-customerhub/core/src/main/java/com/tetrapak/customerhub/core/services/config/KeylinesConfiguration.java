package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration class for Keylines
 * 
 * @author selennys
 *
 */

@ObjectClassDefinition(name = "Keylines Configuration", description = "Keylines Configuration")
public @interface KeylinesConfiguration {

    @AttributeDefinition(name = "Path", description = "Path of keyline assets", type = AttributeType.STRING, defaultValue = "/content/dam/tetrapak/media-box/global/en/keylines")
    String path();

}
