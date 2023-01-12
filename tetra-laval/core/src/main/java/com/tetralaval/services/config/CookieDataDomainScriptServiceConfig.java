package com.tetralaval.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "OneTrust Cookie Data Domain Script Service Configuration",
        description = "OneTrust Cookie Data Domain Script Service Configuration for Tetra Laval")
public @interface CookieDataDomainScriptServiceConfig {
    /**
     * Cookie Domain Script Configuration
     * @return Cookie Domain script config
     */ 
    @AttributeDefinition(name = "Cookie Domain and Script Configuration",
            description = "Enter value in format of " +
                    "\"sitename\"={\"siteAbbrevation\":\"abbreviation\",\"domainScript\":\"oneTrusdomainscript\"}",
            type = AttributeType.STRING)
    String[] cookieDomainScriptConfig();
}
