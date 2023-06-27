package com.tetrapak.customerhub.core.services.config;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "EmbeddPowerBi Report Configuration",
        description = "EmbeddPowerBi Report Configuration")
public @interface PowerBiReportConfig {
   
    /**
     * Azure TenantID
     * @return Azure TenantID
     */
    @AttributeDefinition(name = "AzureID TenantID",
            description = "AzureID TenantID", type = AttributeType.STRING)
    String azureidtenantid();
    /**
     * PowerBI Client ID
     * @return PowerBI Client ID
     */
    @AttributeDefinition(name = "PowerBI Client ID",
            description = "PowerBI Client ID", type = AttributeType.STRING)
    String pbicid();
    /**
     * PowerBI Client Secret
     * @return PowerBI Client Secret
     */ 
    @AttributeDefinition(name = "PowerBI Client Secret",
            description = "PowerBI Client Secret", type = AttributeType.STRING)
    String pbics();
    
    /**
     * PowerBI DataSet ID
     * @return PowerBI DataSet ID
     */
    @AttributeDefinition(name = "PowerBI DataSet ID",
            description = "PowerBI DataSet ID", type = AttributeType.STRING)
    String pbidatasetid();
     /**
     * PowerBI Report ID
     * @return PowerBI Report ID
     */
    @AttributeDefinition(name = "PowerBI Report ID",
            description = "PowerBI Report ID", type = AttributeType.STRING)
    String pbireportid();
     /**
     * PowerBI Workspace ID
     * @return PowerBI Workspace ID
     */
    @AttributeDefinition(name = "PowerBI Workspace ID",
            description = "PowerBI Workspace ID", type = AttributeType.STRING)
    String pbiworkspaceid();
}