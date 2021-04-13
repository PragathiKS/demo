package com.trs.core.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
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

import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.Replicator;
import com.trs.core.jobs.AssetMetadataUploadEventJob;
import com.trs.core.jobs.AssetMetadataUploadEventJobTest;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImpl;
import com.trs.core.services.impl.TaxonomyServiceImpl;
import com.trs.core.utils.TestUtils;
import com.trs.core.utils.TrsConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AssetMetadataUploadEventHandlerTest {
	
	@Mock
	TrsConfigurationService trsConfig;
	
	@Mock
    private Replicator replicator;
	
    @Mock
    private Job job;
    
    @Mock
    JobManager jobManager;
    
    @Mock
    private AssetManager assetManager;
    
	@Mock
    private ResourceResolverFactory resolverFactory;
	
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private AssetMetadataUploadEventHandler assetMetadataUploadEventHandler;
	
	private static final String METADATA_IMPORT_JOB_FILE_PARENT_FOLDER = "/var/dam/asyncjobs/421f22e5-265c-4434-9d0a-4aa186f79562";
	private static final String METADATA_IMPORT_JOB_FILE_NAME = "trs-metadata.csv";

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		TestUtils.setUpAssetPageOpsService(context, TestUtils.TEST_ASSET_PATH, replicator);
		TestUtils.setUpTaxonomyService(context);
		TestUtils.setupTrsConfiguration(context, trsConfig);
		context.load().json(AssetMetadataUploadEventJobTest.class.getResourceAsStream("/com/trs/core/services/impl/metadataImportJobFolder.json"), METADATA_IMPORT_JOB_FILE_PARENT_FOLDER);
		context.load().binaryFile(AssetMetadataUploadEventJobTest.class.getResourceAsStream("/com/trs/core/services/impl/trs-metadata.csv"), METADATA_IMPORT_JOB_FILE_PARENT_FOLDER + TrsConstants.FORWARD_SLASH + METADATA_IMPORT_JOB_FILE_NAME);
		
		context.registerService(ResourceResolverFactory.class,resolverFactory);
		Map<String, Object> props = new HashMap<>();
		context.registerInjectActivateService(new AssetPageOpsServiceImpl(), props);
		context.registerInjectActivateService(new TaxonomyServiceImpl(), props);
		context.registerInjectActivateService(new AssetMetadataUploadEventJob(), props);
		context.registerService(JobManager.class,jobManager);
		context.registerService(AssetManager.class,assetManager);
	//	jobManager = context.registerInjectActivateService(Mockito.mock(JobManager.class),props);
		assetMetadataUploadEventHandler = context.registerInjectActivateService(new AssetMetadataUploadEventHandler(), props);
	}

	@Test
	final void testHandleEvent() {
		
		final Map<String, Object> eventParams = new HashMap<>();
		eventParams.put("inputFile", METADATA_IMPORT_JOB_FILE_PARENT_FOLDER+TrsConstants.FORWARD_SLASH+METADATA_IMPORT_JOB_FILE_NAME);

		final Event event = new Event("org/apache/sling/event/notification/job/FINISHED", eventParams);
		assetMetadataUploadEventHandler.handleEvent(event);
		verify(jobManager, times(1)).addJob(Mockito.anyString(), Mockito.anyMap());
	}

}
