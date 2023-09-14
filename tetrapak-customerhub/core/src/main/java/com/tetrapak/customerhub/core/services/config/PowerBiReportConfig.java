package com.tetrapak.customerhub.core.services.config;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "EmbeddPowerBi Report Configuration",
        description = "EmbeddPowerBi Report Configuration")
public @interface PowerBiReportConfig {
   

     /**
     * PowerBI Service URL
     * @return PowerBI Service URL
     */
    @AttributeDefinition(name = "PowerBI Service URL",
            description = "PowerBI Service URL", type = AttributeType.STRING)
    String pbiserviceurl();

    /**
     * PowerBI EmbeddToken URL
     * @return PowerBI EmbeddToken URL
     */
    @AttributeDefinition(name = "PowerBI EmbeddToken URL",
            description = "PowerBI EmbeddToken URL", type = AttributeType.STRING)
    String pbiembedtokenurl();

    /**
     * PowerBI Resource URL
     * @return PowerBI Resource URL
     */
    @AttributeDefinition(name = "PowerBI Resource URL",
            description = "PowerBI Resource URL", type = AttributeType.STRING)
    String pbiresourceurl();
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

    @AttributeDefinition(name = "PowerBI DataSet ID for RLS Report",
            description = "PowerBI DataSet ID for RLS Report", type = AttributeType.STRING)
    String pbidatasetidrls();

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