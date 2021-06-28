package com.trs.core.services;

import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.replication.ReplicationActionType;
import com.trs.core.exceptions.PageNotCreatedException;
import com.trs.core.reports.StatefulReport;

/**
 * 
 * This service exposes following operations :
 * 
 * 1. Creation of page corresponding to video asset passed.
 * 
 * 2. Replication of page
 * 
 */

public interface AssetPageOpsService {
    
    String PAGE_PUBLIC_URL_JCR_PROPERTY="dam:pagePublishUrl";
    String PAGE_AUTHOR_URL_JCR_PROPERTY="dam:pageAuthorUrl";

    Map<String, String> createTrsPage(ResourceResolver resolver, String path)
            throws PageNotCreatedException;

    StatefulReport createPageCreationReport();

    void createTrsPage(ResourceResolver resolver, String path, StatefulReport pageCreationReport);

    void replicationActionOnResource(ResourceResolver resolver, ReplicationActionType action, String resourcePath);

    Map<String, String> getPagePathForAsset(String path);
}
