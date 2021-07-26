package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface BaiduMapServiceConfig.
 */
@ObjectClassDefinition(name = "Public Web Baidu Map Configuration", description = "Baidu Map Service Configuration")
public @interface BaiduMapServiceConfig {

    /**
     * Baidu Map Key.
     *
     * @return Baidu Map Key
     */
    @AttributeDefinition(
            name = "Baidu Map Key",
            description = "Baidu Map Key",
            type = AttributeType.STRING)
    String baiduMapKey();
}
