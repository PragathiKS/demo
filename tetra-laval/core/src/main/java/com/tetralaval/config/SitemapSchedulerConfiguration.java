package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Sitemap scheduler configuration
 */
@ObjectClassDefinition(
        name = "SitemapSchedulerConfiguration",
        description = "Sitemap scheduler configuration for Tetra Laval"
)
public @interface SitemapSchedulerConfiguration {
    /**
     * Scheduler name
     *
     * @return Scheduler name
     */
    @AttributeDefinition(
            name = "Scheduler name",
            description = "Name of the scheduler",
            type = AttributeType.STRING)
    String schedulerName();

    /**
     * Enabled status
     *
     * @return Enabled status
     */
    @AttributeDefinition(
            name = "Enabled",
            description = "True, if scheduler service is enabled",
            type = AttributeType.BOOLEAN)
    boolean enabled();

    /**
     * Cron expression
     *
     * @return Cron expression
     */
    @AttributeDefinition(
            name = "Cron Expression",
            description = "Cron expression used by the scheduler",
            type = AttributeType.STRING)
    String cronExpression();
}
