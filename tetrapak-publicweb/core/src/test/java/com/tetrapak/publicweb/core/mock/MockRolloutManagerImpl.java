package com.tetrapak.publicweb.core.mock;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.api.RolloutManager;

public class MockRolloutManagerImpl implements RolloutManager {

    @Override
    public boolean isExcludedNode(Node arg0) throws RepositoryException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExcludedNodeType(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExcludedPageProperty(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExcludedParagraphProperty(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExcludedProperty(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExcludedProperty(boolean arg0, String arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReservedProperty(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void rollout(RolloutParams arg0) throws WCMException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rollout(ResourceResolver arg0, LiveRelationship arg1, boolean arg2) throws WCMException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rollout(ResourceResolver arg0, LiveRelationship arg1, boolean arg2, boolean arg3) throws WCMException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateRolloutInfo(Node arg0, boolean arg1, boolean arg2) throws WCMException {
        // TODO Auto-generated method stub
        
    }

}
