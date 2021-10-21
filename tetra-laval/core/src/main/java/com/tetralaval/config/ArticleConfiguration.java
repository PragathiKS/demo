package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Article page configuration
 */
@ObjectClassDefinition(
        name = "ArticleConfiguration",
        description = "Article Page configuration for Tetra Laval"
)
public @interface ArticleConfiguration {
    /**
     * Article type
     *
     * @return Article type
     */
    @AttributeDefinition(
            name = "Type",
            description = "Type of the article",
            type = AttributeType.STRING)
    String[] type();

}
