package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface RebuildingKitsEmailConfiguration.
 */
@ObjectClassDefinition(name = "RebuildingKits Email Configuration", description = "RebuildingKits Email Configuration")
public @interface RebuildingKitsEmailConfiguration {

    /**
     * RebuildingKits recipient addresses.
     *
     * @return the string[]
     */
    @AttributeDefinition(
            name = "RebuildingKits Recipient Email Address",
            description = "RebuildingKits Recipient Email Address",
            type = AttributeType.STRING)
    String[] rebuildingKitsRecipientAddresses();

    /**
     * Plant master email template path.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "RebuildingKits Email Template Path",
            description = "RebuildingKits Email Template Path",
            type = AttributeType.STRING)
    String rebuildingKitsEmailTemplatePath();

    /**
     * Checks if is rebuildingKits email service enabled.
     *
     * @return true, if is rebuildingKits email service enabled
     */
    @AttributeDefinition(
            name = "Enable email feature for RebuildingKits ",
            description = "Enable email feature for RebuildingKits",
            type = AttributeType.BOOLEAN)
    boolean isRebuildingKitsEmailServiceEnabled();
}
