package com.tetrapak.publicweb.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;

public interface CreateLiveCopyService {

    public void createLiveCopy(ResourceResolver resourceResolver, String srcPath, RolloutManager rolloutManager,
            LiveRelationshipManager liveRelManager);

}
