package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * OSGi configuration for PXP
 *
 * @author Sandip Kumar
 */
@ObjectClassDefinition(name = "Public Web PXP Full Feed Configuration", description = "Public Web PXP Full Feed Configuration")
public @interface PXPConfig {

    /**
     * Full Feed Cron-job expression
     *
     * @return Full Feed Cron-job expression
     */
    @AttributeDefinition(name = "Full Feed Cron-job expression")
    String fullFeedSchedulerExpression() default "0 0 0 ? * SUN *";

    /**
     * DISABLE FULL FEED SCHEDULER.
     *
     * @return API GEE Service Url
     */
    @AttributeDefinition(name = "Disable Full Feed Scheduled Task", description = "Disable Delta Feed Scheduled Task")
    boolean fullFeedSchedulerDisable() default false;

    /**
     * BEARER TOKEN REFRESH TIME
     *
     * @return bearer token refreh time
     */
    @AttributeDefinition(name = "Refresh Bearer Token Time (in milliseconds)", description = "Refresh Bearer Token Time")
    int schedulerRefreshTokenTime() default PWConstants.FIFTY_MIN_IN_MILLI_SECONDS;

}
