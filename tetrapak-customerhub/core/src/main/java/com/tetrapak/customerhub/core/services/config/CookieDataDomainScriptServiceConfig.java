package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "OneTrust Cookie Data Domain Script Service Configuration for Customerhub",
        description = "OneTrust Cookie Data Domain Script Service Configuration for Customerhub")
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
