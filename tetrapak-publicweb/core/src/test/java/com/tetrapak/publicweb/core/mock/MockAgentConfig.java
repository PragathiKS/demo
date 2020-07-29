package com.tetrapak.publicweb.core.mock;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.ValueMap;

import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.AgentConfigGroup;

public class MockAgentConfig implements AgentConfig {

    private final String transportUri;
    private String transportUser;
    private String transportPassword;
    private String testUri;

    public MockAgentConfig(final String transposrtUri) {
        this.transportUri = transposrtUri;
    }

    @Override
    public boolean aliasUpdate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean allowsExpiredCertificates() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void checkValid() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAgentId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAgentUserId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAllTransportURIs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getBatchMaxSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getBatchWaitTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public AgentConfigGroup getConfigGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getConfigPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLogLevel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxRetries() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ValueMap getProperties() {
        return new ValueMap() {

            @Override
            public Collection<Object> values() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int size() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Object remove(final Object arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void putAll(final Map<? extends String, ? extends Object> arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public Object put(final String arg0, final Object arg1) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Set<String> keySet() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isEmpty() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object get(final Object arg0) {
                if (arg0.equals("testUri")) {
                    return "/api/wserp/check-exist-by-domain-name?domainName=www-dev.tetrapak.com";
                }
                return null;
            }

            @Override
            public Set<Entry<String, Object>> entrySet() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsValue(final Object arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean containsKey(final Object arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void clear() {
                // TODO Auto-generated method stub

            }

            @Override
            public <T> T get(final String name, final T defaultValue) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <T> T get(final String name, final Class<T> type) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    public long getRetryDelay() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getSSLConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSerializationType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTransportPassword() {
        return transportPassword;
    }

    @Override
    public String getTransportURI() {
        return transportUri;
    }

    @Override
    public String getTransportUser() {
        return transportUser;
    }

    @Override
    public boolean isBatchMode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInMaintenanceMode() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOAuthEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSpecific() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTriggeredOnDistribute() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTriggeredOnModification() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTriggeredOnOffTime() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTriggeredOnReceive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean noStatusUpdate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean noVersions() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean usedForReverseReplication() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setTransportUser(final String transportUser) {
        this.transportUser = transportUser;
    }

    public void setTransportPassword(final String transportPassword) {
        this.transportPassword = transportPassword;
    }

    public void setTestUri(final String testUri) {
        this.testUri = testUri;
    }

}
