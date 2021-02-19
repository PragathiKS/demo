package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.day.cq.wcm.msm.api.LiveActionFactory;

/**
 * OSGi configuration for Live Action
 *
 * @author Sandip Kumar
 */
@ObjectClassDefinition(name = "Public Web New Reference Update MSM Configuration", description = "Public Web New Reference Update MSM Configuration")
public @interface PWLiveActionFactoryConfig {
    
    /**
     * LionBridge Cron-job expression
     *
     * @return LionBridge Cron-job expression
     */
    @AttributeDefinition(name = LiveActionFactory.LIVE_ACTION_NAME)
    String actionName() default "pwUpdateReferrences";


}
