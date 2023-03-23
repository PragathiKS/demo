package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration class for Preferred languages
 * 
 * @author semathura
 *
 */

@ObjectClassDefinition(name = "Preferred Languages Configuration", description = "Preferred Languages Configuration")
public @interface PreferredLanguagesConfiguration {

    @AttributeDefinition(name = "Path", description = "Path of preferred languages content fragment")
    String path() default "/content/dam/tetrapak/customerhub/contentfragment/preferred-languages";

}
