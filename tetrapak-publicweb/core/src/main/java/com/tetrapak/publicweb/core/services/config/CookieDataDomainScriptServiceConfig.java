package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "OneTrust Cookie Data Domain Script Service Configuration",
        description = "OneTrust Cookie Data Domain Script Service Configuration")
public @interface CookieDataDomainScriptServiceConfig {
    /**
     * Site Name
     * @return Site Name
     */
    @AttributeDefinition(name = "Site Name",
            description = "Site Name : Site Name Abbreviation", type = AttributeType.STRING)
    String[] siteNameConfMap() default "publicweb:pw";

    /**
     * Data Domain Script Conf Map
     * @return Data Domain Script Conf Map
     */ 
    @AttributeDefinition(name = "Data Domain Script Service Configuration Map",
            description = "Sitename:OneTrust script", type = AttributeType.STRING)
    String[] dataDomainScriptConfMap();
}
