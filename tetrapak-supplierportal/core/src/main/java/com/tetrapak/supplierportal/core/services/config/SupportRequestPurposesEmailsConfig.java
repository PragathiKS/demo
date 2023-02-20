package com.tetrapak.supplierportal.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for SupportRequest Purposes Email
 * 
 */
@ObjectClassDefinition(name = "SupportRequest Purposes Email Configuration", description = "SupportRequest Purposes Email Configuration")
public @interface SupportRequestPurposesEmailsConfig {

	/**
	 * On Boardiing Maintanance Email
	 * 
	 * @return On Boardiing Maintanance Email
	 */
	@AttributeDefinition(name = "On Boardiing Maintanance Email", description = "On Boardiing Maintanance Email", type = AttributeType.STRING)
	String[] getOBoardingMaintananceEmail();

	/**
	 * Sourcing Contracting Email
	 * 
	 * @return Sourcing Contracting Email
	 */
	@AttributeDefinition(name = "Sourcing Contracting Email", description = "Sourcing Contracting Email", type = AttributeType.STRING)
	String[] getSourcingContractingEmail();

	/**
	 * Catalogues Email
	 * 
	 * @return Catalogues Email
	 */
	@AttributeDefinition(name = "Catalogues Email", description = "Catalogues Email", type = AttributeType.STRING)
	String[] getCataloguesEmail();

	/**
	 * Dynamic Media Service Root Path
	 * 
	 * @return Dynamic Media Service Root Path
	 */
	@AttributeDefinition(name = "Other Information Email", description = "Other Information Email", type = AttributeType.STRING)
	String[] getOtherEmail();
}
