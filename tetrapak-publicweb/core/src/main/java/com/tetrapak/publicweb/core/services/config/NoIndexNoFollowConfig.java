package com.tetrapak.publicweb.core.services.config;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "No Index No follow Service Configuration",
        description = "No Index No follow Service Configuration")


public @interface  NoIndexNoFollowConfig {

    /**
     * application name mapping.
     *
     * @return the string[]
     */
    @AttributeDefinition(
            name = "Application names for which 'No Index No' Follow properties are false",
            description = "Application names for which 'No Index No Follow' properties are false",
            type = AttributeType.STRING)
    String[] applicationName();
}
