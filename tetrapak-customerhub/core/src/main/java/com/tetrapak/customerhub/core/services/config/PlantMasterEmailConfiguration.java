package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface PlantMasterEmailConfiguration.
 */
@ObjectClassDefinition(name = "PlantMaster Email Configuration", description = "PlantMaster Email Configuration")
public @interface PlantMasterEmailConfiguration {

    /**
     * Plant master recipient addresses.
     *
     * @return the string[]
     */
    @AttributeDefinition(
            name = "PlantMaster Recipient Email Address",
            description = "PlantMaster Recipient Email Address",
            type = AttributeType.STRING)
    String[] plantMasterRecipientAddresses();

    /**
     * Plant master email template path.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "PlantMaster Email Template Path",
            description = "PlantMaster Email Template Path",
            type = AttributeType.STRING)
    String plantMasterEmailTemplatePath();

    /**
     * Checks if is plant master email service enabled.
     *
     * @return true, if is plant master email service enabled
     */
    @AttributeDefinition(
            name = "Enable email feature for PlantMaster Trainings",
            description = "Enable email feature for PlantMaster Trainings",
            type = AttributeType.BOOLEAN)
    boolean isPlantMasterEmailServiceEnabled();
}
