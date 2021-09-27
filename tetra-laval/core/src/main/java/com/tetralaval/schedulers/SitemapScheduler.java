package com.tetralaval.schedulers;

import com.tetralaval.config.SitemapSchedulerConfiguration;
import com.tetralaval.services.SitemapSchedulerService;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SitemapScheduler
 */
@Component(immediate = true, service = SitemapScheduler.class)
public class SitemapScheduler implements Runnable {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapScheduler.class);

    /** schedulerId */
    private int schedulerId;

    /** scheduler */
    @Reference
    private Scheduler scheduler;

    /** sitemapSchedulerService */
    @Reference
    private SitemapSchedulerService sitemapSchedulerService;

    /**
     * Activate method
     */
    @Activate
    protected void activate() {
        schedulerId = getSchedulerId();
        addScheduler();

        LOGGER.info("SitemapScheduler was activated");
    }

    /**
     * Modified method
     */
    @Modified
    protected void modified() {
        removeScheduler();
        schedulerId = getSchedulerId();
        addScheduler();

        LOGGER.info("SitemapScheduler was modified");
    }

    /**
     * Deactivate method
     * @param config
     */
    @Deactivate
    protected void deactivate(SitemapSchedulerConfiguration config) {
        removeScheduler();

        LOGGER.info("SitemapScheduler was deactivated");
    }

    /**
     * schedulerId getter
     * @return schedulerId
     */
    private int getSchedulerId() {
        return sitemapSchedulerService.getSchedulerName().hashCode();
    }

    /**
     * removeScheduler method
     */
    private void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));

        LOGGER.info("SitemapScheduler was removed");
    }

    /**
     * addScheduler method
     */
    private void addScheduler() {
        if(sitemapSchedulerService.isEnabled()) {
            ScheduleOptions scheduleOptions = scheduler.EXPR(sitemapSchedulerService.getCronExpression());

            scheduleOptions.name(sitemapSchedulerService.getSchedulerName());
            scheduleOptions.canRunConcurrently(false);

            scheduler.schedule(this, scheduleOptions);

            LOGGER.info("SitemapScheduler was added");
        } else {
            LOGGER.info("SitemapScheduler is disabled");
        }
    }

    /**
     * run method
     */
    @Override
    public void run() {
        sitemapSchedulerService.generateSitemap();
    }
}
