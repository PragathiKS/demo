package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Azure Table Storage Service Configuration
 * @author Swati Lamba
 */
@ObjectClassDefinition(name = "Azure Table Storage Service Configuration", description = "Azure Table Storage Service Configuration")
public @interface AzureTableStorageServiceConfig {

    /**
     * DefaultEndpointsProtocol
     * @return DefaultEndpointsProtocol http or https
     */
    @AttributeDefinition(name = "DefaultEndpointsProtocol", description = "DefaultEndpointsProtocol", type = AttributeType.STRING)
    String defaultEndpointsProtocol();

    /**
     * Account Name
     * @return Account Name
     */
    @AttributeDefinition(name = "Account Name", description = "Account Name in Azure", type = AttributeType.STRING)
    String accountName();

    /**
     * Account Secret Key
     * @return Account Secret Key
     */
    @AttributeDefinition(name = "Account Key", description = "Account Secret Key", type = AttributeType.STRING)
    String accountKey();
    
    /**
     * Azure Table in which the user preference data would be saved
     * 
     * @return Azure Table name in which the user preference data would be saved
     */
    @AttributeDefinition(name = "Table name", description = "Azure Table name in which the user preference data would be saved",
            type = AttributeType.STRING)
    String tableName();
}
