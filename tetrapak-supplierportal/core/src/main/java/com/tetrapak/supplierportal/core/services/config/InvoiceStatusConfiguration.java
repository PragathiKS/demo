package com.tetrapak.supplierportal.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Invoice Status
 *
 * @author Sunil Kumar Yadav
 */
@ObjectClassDefinition(name = "SupplierPortal Invoice Status Configuration", description = "SupplierPortal Invoice Status Configuration")
public @interface InvoiceStatusConfiguration {

	 /**
     * API GEE Service Url
     *
     * @return API GEE Service Url
     */
   @AttributeDefinition(name = "Invoice Status Code Mappings", description = "Invoice Status Code Mappings", type = AttributeType.STRING)
   public String[] invoiceStatusCodeMappings();
}
