package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "TetraPak Customer Hub RK Liability Conditions Configuration", description = "TetraPak Customer Hub RK Liability Conditions Configuration")
public @interface RKLiabilityConditionsConfig {

    @AttributeDefinition(name = "PDF folder mapping Generic Lists path", description = "Enter the path of the generic list in which the mapping between language and the pdf path is captured", type = AttributeType.STRING)
    String pdfFolderMappingGLPath();
}
