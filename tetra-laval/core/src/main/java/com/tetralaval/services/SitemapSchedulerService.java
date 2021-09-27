package com.tetralaval.services;

/**
 * SitemapSchedulerService interface
 */
public interface SitemapSchedulerService {
    /**
     * schedulerName getter
     * @return schedulerName
     */
    String getSchedulerName();

    /**
     * enabled getter
     * @return enabled
     */
    Boolean isEnabled();

    /**
     * cronExpression getter
     * @return cronExpression
     */
    String getCronExpression();

    /**
     * Generate sitemap method
     */
    void generateSitemap();
}
