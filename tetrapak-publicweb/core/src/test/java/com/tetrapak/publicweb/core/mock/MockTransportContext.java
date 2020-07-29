package com.tetrapak.publicweb.core.mock;

import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.TransportContext;

public class MockTransportContext implements TransportContext {

    AgentConfig config;

    @Override
    public Discardable getAttribute(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AgentConfig getConfig() {
        return config;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Discardable setAttribute(final String name, final Discardable attr) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setConfig(final AgentConfig config) {
        this.config = config;
    }

}
