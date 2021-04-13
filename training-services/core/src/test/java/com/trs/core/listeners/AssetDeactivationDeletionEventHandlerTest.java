/**
 * 
 */
package com.trs.core.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.event.Event;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.trs.core.jobs.AssetDeactivationDeletionEventJob;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImpl;
import com.trs.core.utils.TestUtils;
import com.trs.core.utils.TrsConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author ankpatha
 *
 */
@ExtendWith(AemContextExtension.class)
class AssetDeactivationDeletionEventHandlerTest {
	
	@Mock
	TrsConfigurationService trsConfig;
	
	@Mock
    private Replicator replicator;
	
    @Mock
    private Job job;
    
    @Mock
    JobManager jobManager;
    
	@Mock
    private ResourceResolverFactory resolverFactory;
	
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private AssetDeactivationDeletionEventHandler assetDeactivationDeletionEventHandler;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		TestUtils.setUpAssetPageOpsService(context, TestUtils.TEST_ASSET_PATH, replicator);
		TestUtils.setupTrsConfiguration(context, trsConfig);
		
		context.registerService(ResourceResolverFactory.class,resolverFactory);
		Map<String, Object> props = new HashMap<>();
		context.registerInjectActivateService(new AssetPageOpsServiceImpl(), props);
		context.registerService(JobManager.class,jobManager);
		assetDeactivationDeletionEventHandler = context.registerInjectActivateService(new AssetDeactivationDeletionEventHandler(), props);
		context.runMode("author");
	}

	/**
	 * Test method for {@link com.trs.core.listeners.AssetDeactivationDeletionEventHandler#handleEvent(org.osgi.service.event.Event)}.
	 */
	@Test
	final void testHandleEvent() {
		final Map<String, Object> eventParams = new HashMap<>();
		eventParams.put("type", ReplicationActionType.DEACTIVATE.toString());
		eventParams.put("paths", new String[] {TestUtils.TEST_ASSET_PATH});

		final Event event = new Event(ReplicationAction.EVENT_TOPIC, eventParams);
		assetDeactivationDeletionEventHandler.handleEvent(event);
		verify(jobManager, times(1)).addJob(Mockito.anyString(), Mockito.anyMap());
	}

}
