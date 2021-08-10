package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "ArticleConfiguration",
        description = "Article Page configuration for Tetra Laval"
)
public @interface ArticleConfiguration {

    @AttributeDefinition(
            name = "Type",
            description = "Type of the article",
            type = AttributeType.STRING)
    String[] type();

}
