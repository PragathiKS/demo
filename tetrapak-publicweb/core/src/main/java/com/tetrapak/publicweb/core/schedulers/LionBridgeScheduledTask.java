package com.tetrapak.publicweb.core.schedulers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
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
import com.tetrapak.publicweb.core.utils.ResourceUtil;

/**
 * The Class LionBridgeScheduledTask.
 */
@Designate(ocd = LBConfig.class)
@Component(immediate = true, service = LionBridgeScheduledTask.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class LionBridgeScheduledTask implements Runnable {

    /** The scheduler ID. */
    private int schedulerID;

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
            if (Objects.nonNull(lbTranslationRes) && lbTranslationRes.hasChildren()) {
                Iterator<Resource> lbChildResources = lbTranslationRes.getChildren().iterator();
                List<String> deleteResourcesList = new ArrayList<>();
                while (lbChildResources.hasNext()) {
                    Resource lbChildResource = lbChildResources.next();
                    Long lbResourceTime = Long
                            .valueOf(lbChildResource.getName().replace("lbtranslatedpages-", StringUtils.EMPTY));
                    Long timeDiff = Calendar.getInstance().getTimeInMillis() - lbResourceTime;
                    if (timeDiff >= 600000) {
                        createRolloutAndActivate(resolver, lbChildResource);
                        deleteResourcesList.add(lbChildResource.getPath());
                    }

                }
                if (!deleteResourcesList.isEmpty()) {
                    deleteResources(resolver, deleteResourcesList);
                }
            }
        } finally {
            LOGGER.debug("{{LionBridgeScheduledTask ended}}");
        }

    }

    /**
     * Createrollout and activate.
     *
     * @param resolver
     *            the resolver
     * @param lbChildResource
     *            the lb child resource
     * @param lbTranslationRes
     *            the lb translation res
     */
    public void createRolloutAndActivate(final ResourceResolver resolver, final Resource lbChildResource) {
        if (lbChildResource.getValueMap().containsKey(PWConstants.LB_TRANSLATED_PROP)) {
            String translatedPage = lbChildResource.getValueMap().get(PWConstants.LB_TRANSLATED_PROP).toString();
            String language = PageUtil.getLanguageCodeFromResource(resolver.getResource(translatedPage));
            createLiveCopyService.createLiveCopy(resolver, translatedPage, rolloutManager, liveRelManager, language,
                    false);

        }
    }

    /**
     * Update lionbridge resource.
     *
     * @param resolver
     *            the resolver
     * @param translationInProgressPages
     *            the translation in progress pages
     */
    public void deleteResources(final ResourceResolver resolver, final List<String> deleteResourcesList) {
        for (String deleteResources : deleteResourcesList) {
            ResourceUtil.deleteResource(resolver, deleteResources);
        }
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
