package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for PXP
 *
 * @author Sandip Kumar
 */
@ObjectClassDefinition(name = "Public Web PXP Import Configuration", description = "Public Web PXP Import Configuration")
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
    @AttributeDefinition(name = "Disable Full Feed Scheduled Task", description = "Disable Full Feed Scheduled Task")
    boolean fullFeedSchedulerDisable() default false;

    /**
     * BEARER TOKEN REFRESH TIME
     *
     * @return bearer token refreh time
     */
    @AttributeDefinition(name = "Refresh Bearer Token Time (in milliseconds)", description = "Refresh Bearer Token Time")
    int schedulerRefreshTokenTime() default 3000000;
   
    /**
     * PXP DAM ROOT PATH
     *
     * @return dam root path
     */
    @AttributeDefinition(name = "DAM root Path")
    String damRootPath() default "/content/dam/tetrapak/publicweb/pxp";

    /**
     * PXP Video Types
     *
     * @return video types
     */
    @AttributeDefinition(name = "Video Type Mapping", description = "types of video in PXP.Add comma seprated.")
    String videoTypes() default "mp4,webm,mpg,mp2,mpeg,mpe,mpv,ogg,m4p,m4v,avi,wmv,mov,qt,flv,swf,avchd";

}
