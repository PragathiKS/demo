package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Rebuilding Kits list Api Service Configuration", description = "Rebuilding Kits list Api Service Configuration")
public @interface RebuildingKitsApiServiceConfig {

	@AttributeDefinition(name = "Number of records to fetch", description = "Number of records to fetch", type = AttributeType.INTEGER)
	int noOfRecords() default 750;

}
