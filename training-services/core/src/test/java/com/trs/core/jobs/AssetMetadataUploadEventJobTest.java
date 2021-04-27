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

import com.day.cq.replication.Replicator;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImpl;
import com.trs.core.services.impl.TaxonomyServiceImpl;
import com.trs.core.utils.TestUtils;
import com.trs.core.utils.TrsConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class AssetMetadataUploadEventJobTest {
	
	@Mock
	TrsConfigurationService trsConfig;
	
	@Mock
    private Replicator replicator;
	
    @Mock
    private Job job;
    
	@Mock
    private ResourceResolverFactory resolverFactory;
	
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
//	@InjectMocks
    private AssetMetadataUploadEventJob assetMetadataUploadEventJob ;
	
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
		assetMetadataUploadEventJob = context.registerInjectActivateService(new AssetMetadataUploadEventJob(), props);
		
		when(job.getProperty("inputFilePath")).thenReturn(METADATA_IMPORT_JOB_FILE_PARENT_FOLDER+TrsConstants.FORWARD_SLASH+METADATA_IMPORT_JOB_FILE_NAME);
		
	}

	@Test
	public final void testProcess() {
		assertEquals(JobResult.OK, assetMetadataUploadEventJob.process(job),"Process method failed");
	}

}
