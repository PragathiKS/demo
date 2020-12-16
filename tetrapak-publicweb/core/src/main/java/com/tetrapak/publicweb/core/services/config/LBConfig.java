package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * OSGi configuration for Lionbridge
 *
 * @author Sandip Kumar
 */
@ObjectClassDefinition(name = "LionBridge Configuration", description = "LionBridge Configuration")
public @interface LBConfig {
    
    /**
     * LionBridge Cron-job expression
     *
     * @return LionBridge Cron-job expression
     */
    @AttributeDefinition(name = "LionBridge Cron-job expression")
    String lbSchedulerExpression() default "0 */30 * ? * *";

    /**
     * DISABLE LionBridge SCHEDULER.
     *
     * @return disbaled or enabled
     */
    @AttributeDefinition(name = "LionBridge Scheduled Task", description = "Disable LionBridge Scheduled Task")
    boolean lbSchedulerDisable() default false;

}
