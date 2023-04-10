package com.tetrapak.publicweb.core.services;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;

/**
 * The Interface CreateLiveCopyService.
 */
public interface CreateLiveCopyService {

    /**
     * Creates the live copy.
     *
     * @param resourceResolver
     *            the resource resolver
     * @param srcPath
     *            the src path
     * @param rolloutManager
     *            the rollout manager
     * @param liveRelManager
     *            the live rel manager
     * @param language
     *            the language
     * @param isDeep
     *            the is deep
     * @throws RepositoryException 
     * @throws WCMException 
     * @throws PersistenceException 
     */
    public void createLiveCopy(ResourceResolver resourceResolver, String srcPath, RolloutManager rolloutManager,
            LiveRelationshipManager liveRelManager, String language, boolean isDeep, boolean flowComingFromLBScheduler) throws PersistenceException, WCMException, RepositoryException, IOException, ServletException;

}