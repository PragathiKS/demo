package com.tetrapak.publicweb.core.replication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationContent;
import com.day.cq.replication.ReplicationResult;
import com.day.cq.replication.ReplicationTransaction;
import com.day.cq.replication.TransportContext;
import com.tetrapak.publicweb.core.mock.MockAgentConfig;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.mock.MockReplicationTransaction;
import com.tetrapak.publicweb.core.mock.MockTransportContext;
import com.tetrapak.publicweb.core.services.CDNCacheInvalidationService;
import com.tetrapak.publicweb.core.services.impl.CDNCacheInvalidationServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class CDNCachePurgeCustomTransportHandlerTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private TransportContext transportContext;

    @Mock
    private ReplicationTransaction replicationTransaction;

    @Mock
    private ReplicationContent replicationContent;

    private MockAgentConfig config = null;

    @Mock
    private ReplicationAction replicationAction;

    @InjectMocks
    private CDNCachePurgeCustomTransportHandler underTest = new CDNCachePurgeCustomTransportHandler();

    private CDNCacheInvalidationService cdnCacheInvalidationService;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        cdnCacheInvalidationService = new CDNCacheInvalidationServiceImpl();
        final Map<String, Object> config = new HashMap<>();
        config.put("contentPathForCDNCacheInvalidation", "/content/tetrapak/publicweb");
        config.put("damPathForCDNCacheInvalidation", "/content/dam/tetrapak");
        config.put("domainForCDN", "https://www-dev.tetrapak.com");

        context.registerService(CDNCacheInvalidationService.class, cdnCacheInvalidationService);
        MockOsgi.activate(context.getService(CDNCacheInvalidationService.class), context.bundleContext(), config);

        this.config = new MockAgentConfig("cdn-serviceinsight://api.cdnetworks.com/ccm/purge/ItemIdReceiver");
        this.config.setTransportPassword("8Dq_k*R7T4+f{Wi");
        this.config.setTransportUser("tppwcdnuser");
    }

    @Test
    public void canHandle_success() {
        assertTrue(underTest.canHandle(config));
    }

    @Test
    public void canHandle_failure() {
        final AgentConfig httpConfig = new MockAgentConfig("https://api.cdnetworks.com/ccm/purge/ItemIdReceiver");
        assertFalse(underTest.canHandle(httpConfig));
    }

    @Test
    public void canHandle_nullTransportURI() {
        final MockAgentConfig config = new MockAgentConfig(null);
        assertFalse(underTest.canHandle(config));
    }

    @Test
    public void deliver_activate() throws Exception {
        underTest = MockHelper.getTransportHandler(context, CDNCachePurgeCustomTransportHandler.class);
        final MockTransportContext transportContext = new MockTransportContext();
        transportContext.setConfig(config);
        final ReplicationTransaction replicationTransaction = new MockReplicationTransaction(
                new ReplicationAction(ReplicationActionType.ACTIVATE, "/content/tetrapak/publicweb/gb/en"));
        // when(replicationTransaction.getAction()).thenReturn(replicationAction);
        // when(replicationAction.getType()).thenReturn(ReplicationActionType.TEST);
        assertEquals(ReplicationResult.OK, underTest.deliver(transportContext, replicationTransaction));
    }

    @Test
    public void deliver_deactivate() throws Exception {
        underTest = MockHelper.getTransportHandler(context, CDNCachePurgeCustomTransportHandler.class);
        final MockTransportContext transportContext = new MockTransportContext();
        transportContext.setConfig(config);
        final ReplicationTransaction replicationTransaction = new MockReplicationTransaction(
                new ReplicationAction(ReplicationActionType.DEACTIVATE, "/content/tetrapak/publicweb/gb/en"));
        // when(replicationTransaction.getAction()).thenReturn(replicationAction);
        // when(replicationAction.getType()).thenReturn(ReplicationActionType.TEST);
        assertEquals(ReplicationResult.OK, underTest.deliver(transportContext, replicationTransaction));
    }

    @Test
    public void deliver_otherAction() throws Exception {
        underTest = MockHelper.getTransportHandler(context, CDNCachePurgeCustomTransportHandler.class);
        final MockTransportContext transportContext = new MockTransportContext();
        transportContext.setConfig(config);
        final ReplicationTransaction replicationTransaction = new MockReplicationTransaction(
                new ReplicationAction(ReplicationActionType.DELETE, "/content/tetrapak/publicweb/gb/en"));
        // when(replicationTransaction.getAction()).thenReturn(replicationAction);
        // when(replicationAction.getType()).thenReturn(ReplicationActionType.TEST);
        assertEquals(ReplicationResult.OK, underTest.deliver(transportContext, replicationTransaction));
    }
}
