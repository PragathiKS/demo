package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Search results configuration
 */
@ObjectClassDefinition(
        name = "SearchResultsConfiguration",
        description = "Tetra Laval - Search Results Configuration")
public @interface SearchResultsConfiguration {
    /**
     * Items per page
     *
     * @return Items per page
     */
    @AttributeDefinition(name = "Result items per page")
    int itemsPerPage();

    /**
     * Max result suggestions
     *
     * @return Max result suggestions
     */
    @AttributeDefinition(name = "Max Result Suggestions")
    int maxResultSuggestions();

}
