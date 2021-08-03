package com.tetralaval.services;

public interface SitemapSchedulerService {
    String getSchedulerName();

    Boolean isEnabled();

    String getCronExpression();

    void generateSitemap();
}
