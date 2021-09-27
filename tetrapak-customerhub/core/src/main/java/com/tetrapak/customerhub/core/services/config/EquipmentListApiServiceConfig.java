package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Equipment List Api Service Configuration", description = "Equipment List Api Service Configuration")
public @interface EquipmentListApiServiceConfig {

    /**
     * API GEE Service Url
     *
     * @return API GEE Service Url
     */
    @AttributeDefinition(name = "Number of records to fetch", description = "Number of records to fetch", type = AttributeType.INTEGER)
    int noOfRecords() default 750;
}
