package com.tetrapak.publicweb.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface FindMyOfficeService.
 */
@ObjectClassDefinition(name = "Find My Office Configuration", description = "Find My Office Service Configuration")
public @interface FindMyOfficeService {

    /**
     * Gets the countries content fragment root path.
     *
     * @return the countries content fragment root path
     */
    @AttributeDefinition(
            name = "Countries Content Fragment Root Path",
            description = "countries Content Fragment Root Path")
    String getCountriesContentFragmentRootPath() default "/content/dam/tetrapak/findMyOffice/contentFragments/countries";

    /**
     * Gets the offices content fragment root path.
     *
     * @return the offices content fragment root path
     */
    @AttributeDefinition(
            name = "Offices Content Fragment Root Path",
            description = "Offices Content Fragment Root Path")
    String getOfficesContentFragmentRootPath() default "/content/dam/tetrapak/findMyOffice/contentFragments/offices";

}
