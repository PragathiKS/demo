package com.tetrapak.publicweb.core.mock;

import java.util.Collection;
import java.util.Map;

import javax.jcr.RangeIterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveCopy;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutConfig;
import com.day.cq.wcm.msm.api.RolloutManager.Trigger;

public class MockLiveRelationshipManagerImpl implements LiveRelationshipManager {

    @Override
    public void addSkippedPages(final Page arg0, final String[] arg1, final boolean arg2) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelPropertyRelationship(final ResourceResolver resolver, final LiveRelationship relation,
            final String[] names, final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelRelationship(final ResourceResolver resolver, final LiveRelationship relation, final boolean deep,
            final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void detach(final Resource resource, final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void endRelationship(final Resource resource, final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public LiveRelationship establishRelationship(final Page arg0, final Page arg1, final boolean arg2,
            final boolean arg3, final RolloutConfig... arg4) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RangeIterator getChildren(final LiveRelationship relationship, final ResourceResolver resourceResolver)
            throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, LiveCopy> getLiveCopies() throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LiveCopy getLiveCopy(final Resource target) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LiveRelationship getLiveRelationship(final Resource target, final boolean advancedStatus)
            throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RangeIterator getLiveRelationships(final Resource source, final String targetPathFilter,
            final Trigger triggerFilter) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<LiveRelationship> getLiveRelationships(final Page arg0, final Trigger arg1, final String[] arg2,
            final boolean arg3) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<LiveRelationship> getLiveRelationships(final Resource arg0, final Trigger arg1,
            final String[] arg2, final boolean arg3) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Page> getSkippedSourcePages(final Page arg0) throws WCMException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLiveRelationship(final Resource resource) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInBlueprint(final Resource source) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLiveCopy(final Resource target) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSource(final Resource resource) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reenablePropertyRelationship(final ResourceResolver resolver, final LiveRelationship relation,
            final String[] names, final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void reenableRelationship(final ResourceResolver resolver, final LiveRelationship relation,
            final boolean autoSave) throws WCMException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSkippedPages(final Page arg0, final String[] arg1, final boolean arg2) throws WCMException {
        // TODO Auto-generated method stub

    }

}
