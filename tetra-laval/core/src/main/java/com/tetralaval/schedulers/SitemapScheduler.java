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

@Component(immediate = true, service = SitemapScheduler.class)
public class SitemapScheduler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SitemapScheduler.class);

    private int schedulerId;

    @Reference
    private Scheduler scheduler;

    @Reference
    private SitemapSchedulerService sitemapSchedulerService;

    @Activate
    protected void activate() {
        schedulerId = getSchedulerId();
        addScheduler();

        log.info("SitemapScheduler was activated");
    }

    @Modified
    protected void modified() {
        removeScheduler();
        schedulerId = getSchedulerId();
        addScheduler();

        log.info("SitemapScheduler was modified");
    }

    @Deactivate
    protected void deactivate(SitemapSchedulerConfiguration config) {
        removeScheduler();

        log.info("SitemapScheduler was deactivated");
    }

    private int getSchedulerId() {
        return sitemapSchedulerService.getSchedulerName().hashCode();
    }

    private void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));

        log.info("SitemapScheduler was removed");
    }

    private void addScheduler() {
        if(sitemapSchedulerService.isEnabled()) {
            ScheduleOptions scheduleOptions = scheduler.EXPR(sitemapSchedulerService.getCronExpression());

            scheduleOptions.name(sitemapSchedulerService.getSchedulerName());
            scheduleOptions.canRunConcurrently(false);

            scheduler.schedule(this, scheduleOptions);

            log.info("SitemapScheduler was added");
        } else {
            log.info("SitemapScheduler is disabled");
        }
    }

    @Override
    public void run() {
        sitemapSchedulerService.generateSitemap();
    }
}
