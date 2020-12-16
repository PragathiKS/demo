package com.tetrapak.publicweb.core.schedulers;

import java.util.Calendar;
import java.util.Objects;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.config.LBConfig;
import com.tetrapak.publicweb.core.utils.GlobalUtil;
import com.tetrapak.publicweb.core.utils.PageUtil;

/**
 * The Class LionBridgeScheduledTask.
 */
@Designate(ocd = LBConfig.class)
@Component(immediate = true, service = LionBridgeScheduledTask.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class LionBridgeScheduledTask implements Runnable {

    /** The scheduler ID. */
    private int schedulerID;

    /** The Constant CQ_CT_TRANSLATED. */
    private static final String CQ_CT_TRANSLATED = "cq:ctTranslated";

    /** The scheduler. */
    @Reference
    private Scheduler scheduler;

    /** The resolverFactory. */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** The create live copy service. */
    @Reference
    private CreateLiveCopyService createLiveCopyService;

    /** The rollout manager. */
    @Reference
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    @Reference
    private LiveRelationshipManager liveRelManager;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LionBridgeScheduledTask.class);

    /**
     * Run.
     */
    @Override
    public void run() {
        LOGGER.debug("{{LionBridgeScheduledTask started}}");
        try (final ResourceResolver resolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory)) {
            Resource lbTranslationRes = resolver.getResource(PWConstants.LB_TRANSLATED_PAGES_NODE);
            if (Objects.nonNull(lbTranslationRes) && lbTranslationRes.getValueMap().containsKey("lbtranslatedpages")) {
                String[] translatedPages = (String[]) lbTranslationRes.getValueMap().get("lbtranslatedpages");
                if (Objects.nonNull(translatedPages)) {
                    for (String translatedPage : translatedPages) {
                        if (Boolean.TRUE.equals(isRolloutRequired(resolver, translatedPage))) {
                            String language = PageUtil
                                    .getLanguageCodeFromResource(resolver.getResource(translatedPage));
                            createLiveCopyService.createLiveCopy(resolver, translatedPage, rolloutManager,
                                    liveRelManager, language, false);
                        }
                    }
                }
            }
        } finally {
            LOGGER.debug("{{LionBridgeScheduledTask ended}}");
        }

    }

    /**
     * Checks if is rollout required.
     *
     * @param resolver
     *            the resolver
     * @param translatedPage
     *            the translated page
     * @return the boolean
     */
    Boolean isRolloutRequired(final ResourceResolver resolver, String translatedPage) {
        Boolean isRolloutRequired = false;
        Resource translatedPageRes = resolver.getResource(translatedPage + "/jcr:content");
        if (Objects.nonNull(translatedPageRes) && translatedPageRes.getValueMap().containsKey(CQ_CT_TRANSLATED)) {
            Calendar ctTranslated = translatedPageRes.getValueMap().get(CQ_CT_TRANSLATED, Calendar.class);
            Long timeDiff = Calendar.getInstance().getTimeInMillis() - ctTranslated.getTimeInMillis();
            if (timeDiff >= 600000) {
                isRolloutRequired = true;
            }
        }
        return isRolloutRequired;
    }

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    protected void activate(final LBConfig config) {
        schedulerID = PWConstants.LB_SCHEDULER_ID.hashCode();
        addScheduler(config);
    }

    /**
     * Modified.
     *
     * @param config
     *            the config
     */
    @Modified
    protected void modified(final LBConfig config) {
        removeScheduler();
        schedulerID = PWConstants.LB_SCHEDULER_ID.hashCode();
        addScheduler(config);
    }

    /**
     * Deactivate.
     *
     * @param config
     *            the config
     */
    @Deactivate
    protected void deactivate(final LBConfig config) {
        removeScheduler();
    }

    /**
     * Add a scheduler based on the scheduler ID.
     *
     * @param config
     *            the config
     */
    private void addScheduler(final LBConfig config) {
        if (!config.lbSchedulerDisable()) {
            final ScheduleOptions sopts = scheduler.EXPR(config.lbSchedulerExpression());
            sopts.name(String.valueOf(schedulerID));
            sopts.canRunConcurrently(false);
            scheduler.schedule(this, sopts);
            LOGGER.debug("LionBridgeScheduledTask added succesfully");
        } else {
            LOGGER.debug("LionBridgeScheduledTask is Disabled, no scheduler job created");
        }
    }

    /**
     * Remove a scheduler based on the scheduler ID.
     */
    private void removeScheduler() {
        LOGGER.debug("Removing LionBridgeScheduledTask Job '{}'", schedulerID);
        scheduler.unschedule(String.valueOf(schedulerID));
    }

}
