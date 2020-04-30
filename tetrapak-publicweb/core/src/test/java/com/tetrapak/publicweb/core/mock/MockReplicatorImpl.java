package com.tetrapak.publicweb.core.mock;

import java.util.Iterator;
import java.util.List;

import javax.jcr.Session;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationContentFilter;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;

public class MockReplicatorImpl implements Replicator {

    @Override
    public void replicate(final Session arg0, final ReplicationActionType arg1, final String arg2)
            throws ReplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void replicate(final Session arg0, final ReplicationActionType arg1, final String arg2,
            final ReplicationOptions arg3) throws ReplicationException {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void checkPermission(final Session arg0, final ReplicationActionType arg1, final String arg2)
            throws ReplicationException {
        // TODO Auto-generated method stub

    }
    
    @Override
    public List<ReplicationContentFilter> createContentFilterChain(final ReplicationAction arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ReplicationStatus getReplicationStatus(final Session arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Iterator<String> getActivatedPaths(Session session,String path) throws ReplicationException{
    	return null;
    }
    
    @Override
    public void replicate(Session session,ReplicationActionType type,String[] paths, ReplicationOptions options) throws ReplicationException{
    	
    }

}
