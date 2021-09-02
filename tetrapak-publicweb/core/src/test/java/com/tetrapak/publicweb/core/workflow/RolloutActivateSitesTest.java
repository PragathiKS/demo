package com.tetrapak.publicweb.core.workflow;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.adobe.granite.workflow.exec.Workflow;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.tetrapak.publicweb.core.mock.MockLiveRelationshipManagerImpl;
import com.tetrapak.publicweb.core.mock.MockReplicatorImpl;
import com.tetrapak.publicweb.core.mock.MockRequestProcessorImpl;
import com.tetrapak.publicweb.core.mock.MockRequestResponseFactoryImpl;
import com.tetrapak.publicweb.core.mock.MockRolloutManagerImpl;
import com.tetrapak.publicweb.core.services.CreateLiveCopyService;
import com.tetrapak.publicweb.core.services.impl.CreateLiveCopyServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class RolloutActivateSitesTest.
 */
public class RolloutActivateSitesTest {

    /** The Workflow. */
    @Mock
    private Workflow workflow;

    /** The work item. */
    @Mock
    private WorkItem workItem;

    /** The workflow data. */
    @Mock
    private WorkflowData workflowData;

    /** The workflow session. */
    @Mock
    private WorkflowSession workflowSession;

    /** The param meta data map. */
    @Mock
    private MetaDataMap paramMetaDataMap;

    /** The replicator. */
    private Replicator replicator;

    /** The payload res. */
    private Resource payloadRes;

    /** The request processor. */
    private SlingRequestProcessor requestProcessor;

    /** The request response factory. */
    private RequestResponseFactory requestResponseFactory;

    /** The create live copy service. */
    private CreateLiveCopyService createLiveCopyService;

    /** The rollout manager. */
    private RolloutManager rolloutManager;

    /** The live rel manager. */
    private LiveRelationshipManager liveRelManager;

    /** The workflow process. */
    private RolloutActivateSites workflowProcess = new RolloutActivateSites();

    /** The aem context. */
    @Rule
    public final AemContext aemContext = new AemContext();

    /** The payload path. */
    private final String PAYLOAD_PATH = "/content/tetrapak/publicweb/lang-masters/en/home";
    
    /** The payload path. */
    private final String PARENT_PAYLOAD_PATH = "/content/tetrapak/publicweb/lang-masters/en";

    /** The payload resource content. */
    private final String PARENT_PAYLOAD_RESOURCE_CONTENT = "/workflow/en.json";

    /** The english live copy base paths. */
    private String[] englishLiveCopyBasePaths = { "/content/tetrapak/publicweb/us/en" };

    /** The rollout configs. */
    private String[] rolloutConfigs = { "/apps/msm/wcm/rolloutconfigs/default" };

    /**
     * Setup.
     */
    @Before
    public void setup() {
        replicator = new MockReplicatorImpl();
        rolloutManager = new MockRolloutManagerImpl();
        requestProcessor = new MockRequestProcessorImpl();
        requestResponseFactory = new MockRequestResponseFactoryImpl();
        aemContext.registerService(Replicator.class, replicator);
        aemContext.registerService(SlingRequestProcessor.class, requestProcessor);
        aemContext.registerService(RolloutManager.class, rolloutManager);
        aemContext.registerService(RequestResponseFactory.class, requestResponseFactory);
        liveRelManager = new MockLiveRelationshipManagerImpl();
        aemContext.registerService(LiveRelationshipManager.class, liveRelManager);

        createLiveCopyService = new CreateLiveCopyServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("enableConfig",true);
        configuraionServiceConfig.put("getEnglishLiveCopyBasePaths", englishLiveCopyBasePaths);
        configuraionServiceConfig.put("getRolloutConfigs", rolloutConfigs);

        aemContext.registerInjectActivateService(createLiveCopyService, configuraionServiceConfig);
        aemContext.registerService(WorkflowProcess.class, workflowProcess);
        MockOsgi.injectServices(workflowProcess, aemContext.bundleContext());
        final Map<String, Object> config = new HashMap<String, Object>();
        MockOsgi.activate(workflowProcess, aemContext.bundleContext(), config);
        // Set run modes
        aemContext.runMode("publish");
        
        aemContext.load().json(PARENT_PAYLOAD_RESOURCE_CONTENT, PARENT_PAYLOAD_PATH);
        MockitoAnnotations.initMocks(this);
        payloadRes = aemContext.currentResource(PAYLOAD_PATH);
        when(workItem.getWorkflowData()).thenReturn(workflowData);
    }

    /**
     * Test execute.
     * @throws WorkflowException 
     */
    @Test
    public void testExecute() throws WorkflowException{
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workItem.getWorkflow()).thenReturn(workflow);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(payloadRes.getResourceResolver());
        paramMetaDataMap = new SimpleMetaDataMap();
        paramMetaDataMap.put("PROCESS_ARGS", "PAYLOAD_PATH");

        createLiveCopyService = aemContext.getService(CreateLiveCopyService.class);
        workflowProcess = (RolloutActivateSites) aemContext.getService(WorkflowProcess.class);
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
    }

}
