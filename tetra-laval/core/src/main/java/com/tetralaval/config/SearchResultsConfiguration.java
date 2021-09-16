package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "SearchResultsConfiguration",
        description = "Tetra Laval - Search Results Configuration")
public @interface SearchResultsConfiguration {

    @AttributeDefinition(name = "Result items per page")
    int itemsPerPage();

    @AttributeDefinition(name = "Max Result Suggestions")
    int maxResultSuggesions();

}
