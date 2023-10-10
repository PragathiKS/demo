package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "TetraPak Customer eBiz RK details page Configuration",
        description = "TetraPak Customer Hub RK details page Configuration")
public @interface RebuildingKitsDetailsConfig {
    @AttributeDefinition(name = "ebiz Generic Lists Path", description = "Enter the path of the generic list in which the mapping " +
            "between languages and the eBiz url is captured",
            type = AttributeType.STRING) String eBizUrlMappingPath();

    @AttributeDefinition(name = "ebiz Parts External Path",
            description = "Enter the path of the parts external path", type = AttributeType.STRING) String partServiceUrl();
}
