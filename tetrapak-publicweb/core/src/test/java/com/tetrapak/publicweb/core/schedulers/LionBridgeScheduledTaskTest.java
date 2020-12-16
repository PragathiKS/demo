package com.tetrapak.publicweb.core.schedulers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockLiveRelationshipManagerImpl;
import com.tetrapak.publicweb.core.mock.MockReplicatorImpl;
import com.tetrapak.publicweb.core.mock.MockRequestProcessorImpl;
import com.tetrapak.publicweb.core.mock.MockRequestResponseFactoryImpl;
import com.tetrapak.publicweb.core.mock.MockRolloutManagerImpl;
import com.tetrapak.publicweb.core.mock.MockScheduler;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.impl.CreateLiveCopyServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class LionBridgeScheduledTaskTest {

    @Rule
    public AemContext context = new AemContext();


    ResourceResolverFactory resolverFactory;

    @Mock
    private LiveRelationshipManager liveRelManager;
    
    @Mock
    private RolloutManager rolloutManager;
    
    /** The replicator. */
    private Replicator replicator;
    
    /** The create live copy service. */
    private CreateLiveCopyService createLiveCopyService;
    
    /** The english live copy base paths. */
    private String[] englishLiveCopyBasePaths = { "/content/tetrapak/publicweb/us/en" };

    /** The rollout configs. */
    private String[] rolloutConfigs = { "/libs/msm/wcm/rolloutconfigs/default" };

    /** The scheduler. */
    @Reference
    Scheduler scheduler;
    
    /** The request processor. */
    private SlingRequestProcessor requestProcessor;

    /** The request response factory. */
    private RequestResponseFactory requestResponseFactory;

    private final LionBridgeScheduledTask lionBridgeTask = new LionBridgeScheduledTask();

    private static final String HOME_PAGE = "/content/tetrapak/publicweb/lang-masters/en/home";
    
    private static final String CONTACT_PAGE = "/content/tetrapak/publicweb/lang-masters/en/contact-us";

    @Before
    public void setUp() throws Exception {

        context.load().json("/lbscheduler/test-lbnode.json", PWConstants.LB_TRANSLATED_PAGES_NODE);
        context.load().json("/lbscheduler/home.json", HOME_PAGE);
        context.load().json("/lbscheduler/home.json", CONTACT_PAGE);
        MockitoAnnotations.initMocks(this);

        replicator = new MockReplicatorImpl();
        rolloutManager = new MockRolloutManagerImpl();
        requestProcessor = new MockRequestProcessorImpl();
        requestResponseFactory = new MockRequestResponseFactoryImpl();
        context.registerService(Replicator.class, replicator);
        context.registerService(SlingRequestProcessor.class, requestProcessor);
        context.registerService(RolloutManager.class, rolloutManager);
        context.registerService(RequestResponseFactory.class, requestResponseFactory);
        liveRelManager = new MockLiveRelationshipManagerImpl();
        context.registerService(LiveRelationshipManager.class, liveRelManager);
        
        replicator = new MockReplicatorImpl();
        context.registerService(Replicator.class, replicator);
        
        createLiveCopyService = new CreateLiveCopyServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("enableConfig",true);
        configuraionServiceConfig.put("getEnglishLiveCopyBasePaths", englishLiveCopyBasePaths);
        configuraionServiceConfig.put("getRolloutConfigs", rolloutConfigs);
        context.registerInjectActivateService(createLiveCopyService, configuraionServiceConfig);

        scheduler = new MockScheduler(lionBridgeTask);
        context.registerService(Scheduler.class, scheduler);
    }

    @Test
    public void run() throws IOException {
        final Map<String, Object> config = new HashMap<String, Object>();
        config.put("lbchedulerExpression", "0 0 30 ? * * *");
        config.put("lbSchedulerDisable", false);

        MockOsgi.injectServices(lionBridgeTask, context.bundleContext());
        MockOsgi.activate(lionBridgeTask, context.bundleContext(), config);
        assertEquals("LionBridgeScheduledTask", "LionBridgeScheduledTask",
                "LionBridgeScheduledTask");
    }
}
