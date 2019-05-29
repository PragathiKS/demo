package com.tetrapak.customerhub.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for TetraPak Customer Hub Dispatcher Flush Service
 *
 * @author Swati Lamba
 */
@ObjectClassDefinition(name = "TetraPak Customer Hub Specific Dispatcher Flush Service Configuration", description = "TetraPak Customer Hub Dispatcher Flush Service Configuration")
public @interface CuhuDispatcherFlushConfig {

	/**
	 * Content Path for the path under which the flush to be worked
	 * 
	 * @return The content path under which the custom flush will work
	 */
	@AttributeDefinition(name = "Content Path", description = "Enter the content path under which the custom flush will work", type = AttributeType.STRING)
	String contentPath();

	/**
	 * Enable the customer hub custom Dispatcher flush
	 * 
	 * @return Enable Custom Flush
	 */
	@AttributeDefinition(name = "Enable Custom Flush", description = "Enable the customer hub custom Dispatcher flush", type = AttributeType.BOOLEAN)
	String enableCustomFlush();

	/**
	 * Comma separated list of country-locale on which the customer hub portal will be localized  eg. eu-fr,us-en
	 * 
	 * @return Country-Locale List
	 */
	@AttributeDefinition(name = "Country-Locale List", description = "Comma separated list of country-locale on which the customer hub portal will be localized eg. eu-fr,us-en", type = AttributeType.STRING)
	String getCountryLocaleList();

}
