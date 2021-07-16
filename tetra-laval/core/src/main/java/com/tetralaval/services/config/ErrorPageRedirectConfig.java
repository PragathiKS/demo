package com.tetralaval.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Tetra Laval Error Page Redirect Configuration",
        description = "Tetra Laval Error Page Redirect Configuration")
public @interface ErrorPageRedirectConfig {

    @AttributeDefinition(name = "Error Page path", description = "Error Page path", type = AttributeType.STRING)
    String getErrorPagePath() default "/content/tetra-laval/home/404";

}