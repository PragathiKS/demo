package com.trs.core.workflow.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.day.cq.replication.Replicator;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.AssetPageOpsServiceImpl;
import com.trs.core.services.impl.TaxonomyServiceImpl;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AssetPageCreationWfStepTest {

	@Mock
	private WorkItem workItem;

	@Mock
	private WorkflowData workflowData;

	@Mock
	private WorkflowSession workflowSession;

	@Mock
	private MetaDataMap paramMetaDataMap;

	@Mock
	private ResourceResolverFactory resolverFactory;

	@Mock
	TrsConfigurationService trsConfig;

	@Mock
	private Replicator replicator;

	private AssetPageCreationWfStep workflowProcess;

	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		TestUtils.setUpAssetPageOpsService(context, TestUtils.TEST_ASSET_PATH, replicator);
		TestUtils.setUpTaxonomyService(context);
		TestUtils.setupTrsConfiguration(context, trsConfig);

		context.registerService(ResourceResolverFactory.class, resolverFactory);
		Map<String, Object> props = new HashMap<>();
		context.registerInjectActivateService(new AssetPageOpsServiceImpl(), props);
		context.registerInjectActivateService(new TaxonomyServiceImpl(), props);
		workflowProcess = context.registerInjectActivateService(new AssetPageCreationWfStep(), props);

		paramMetaDataMap = new SimpleMetaDataMap();
		Mockito.when(workflowData.getPayload()).thenReturn(TestUtils.TEST_ASSET_PATH);
		Mockito.when(workflowData.getPayloadType()).thenReturn("JCR_PATH");
		
		Mockito.when(workItem.getWorkflowData()).thenReturn(workflowData);
	}

	@Test
	final void testExecute() throws WorkflowException {
		 workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
		 assertNotNull(context.resourceResolver().getResource("/content/trs/test/video2"),"Resource should not be null");
	}

}
