package com.tetrapak.publicweb.core.mock;

import java.util.Collection;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationContent;
import com.day.cq.replication.ReplicationLog;
import com.day.cq.replication.ReplicationTransaction;

public class MockReplicationTransaction implements ReplicationTransaction {

    ReplicationAction action;

    public MockReplicationTransaction(final ReplicationAction action) {
        this.action = action;
    }

    @Override
    public ReplicationAction getAction() {
        return action;
    }

    @Override
    public ReplicationContent getContent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReplicationLog getLog() {
        // TODO Auto-generated method stub
        return new ReplicationLog() {

            @Override
            public void warn(final String fmt, final Object... args) {
                // TODO Auto-generated method stub

            }

            @Override
            public void warn(final String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setLevel(final Level level) {
                // TODO Auto-generated method stub

            }

            @Override
            public void info(final String fmt, final Object... args) {
                // TODO Auto-generated method stub

            }

            @Override
            public void info(final String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public Collection<String> getLines() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Level getLevel() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void error(final String fmt, final Object... args) {
                // TODO Auto-generated method stub

            }

            @Override
            public void error(final String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public void debug(final String fmt, final Object... args) {
                // TODO Auto-generated method stub

            }

            @Override
            public void debug(final String message) {
                // TODO Auto-generated method stub

            }
        };
    }

}
