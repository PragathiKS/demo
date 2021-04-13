package com.trs.core.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer.JobResult;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImpl;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AssetDeactivationDeletionEventJobTest {
	
	@Mock
	TrsConfigurationService trsConfig;
	
	@Mock
    private Replicator replicator;
	
    @Mock
    private Job job;
    
	@Mock
    private ResourceResolverFactory resolverFactory;
	
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private AssetDeactivationDeletionEventJob assetDeactivationDeletionEventJobTest;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		TestUtils.setUpAssetPageOpsService(context, TestUtils.TEST_ASSET_PATH, replicator);
		TestUtils.setupTrsConfiguration(context, trsConfig);
		
		context.registerService(ResourceResolverFactory.class,resolverFactory);
		Map<String, Object> props = new HashMap<>();
		context.registerInjectActivateService(new AssetPageOpsServiceImpl(), props);
		assetDeactivationDeletionEventJobTest = context.registerInjectActivateService(new AssetDeactivationDeletionEventJob(), props);
		when(job.getProperty("path")).thenReturn(TestUtils.TEST_ASSET_PATH);
		
	}

	@Test
	final void testProcessDeactivation() {
		when(job.getProperty("replicationEventType")).thenReturn(ReplicationActionType.DEACTIVATE.toString());
		assertEquals(JobResult.OK, assetDeactivationDeletionEventJobTest.process(job),"testProcessDeactivation failed");
	}
	
	@Test
	final void testProcessDeletion() {
		when(job.getProperty("replicationEventType")).thenReturn(ReplicationActionType.DELETE.toString());
		assertEquals(JobResult.OK, assetDeactivationDeletionEventJobTest.process(job),"testProcessDeletion failed");
	}

}
