package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.tetrapak.publicweb.core.constants.PWConstants;

/**
 * OSGi configuration for PXP
 *
 * @author Sandip Kumar
 */
@ObjectClassDefinition(name = "Public Web PXP Delta Feed Configuration", description = "Public Web PXP Delta Feed Configuration")
public @interface DeltaPXPConfig {
    
    /**
     * DELTA Feed Cron-job expression
     *
     * @return DELTA Feed Cron-job expression
     */
    @AttributeDefinition(name = "Delta Feed Cron-job expression")
    String deltaFeedSchedulerExpression() default "0 0 0 ? * * *";

    /**
     * DISABLE DELTA FEED SCHEDULER.
     *
     * @return API GEE Service Url
     */
    @AttributeDefinition(name = "Delta Full Feed Scheduled Task", description = "Disable Delta Feed Scheduled Task")
    boolean deltaFeedSchedulerDisable() default false;

    /**
     * BEARER TOKEN REFRESH TIME
     *
     * @return bearer token refreh time
     */
    @AttributeDefinition(name = "Refresh Bearer Token Time (in milliseconds)", description = "Refresh Bearer Token Time")
    int schedulerRefreshTokenTime() default PWConstants.FIFTY_MIN_IN_MILLI_SECONDS;

}
