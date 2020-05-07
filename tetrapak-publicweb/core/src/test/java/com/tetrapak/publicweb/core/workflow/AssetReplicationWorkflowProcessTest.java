package com.tetrapak.publicweb.core.workflow;

import static org.mockito.Mockito.when;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.metadata.SimpleMetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.services.AssetImportService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AssetReplicationWorkflowProcessTest {


    /** The resolver factory. */
    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private Replicator replicator;

    @Mock
    private Resource existingResource;

    @Mock
    private AssetManager assetManager;

    @Mock
    private AssetImportService assetImportService;

    @Mock
    private WorkItem workItem;

    /** The site search servlet. */
    @InjectMocks
    private AssetReplicationWorkflowProcess workflowProcess = new AssetReplicationWorkflowProcess();

    @Rule
    public final AemContext aemContext = new AemContext();

    private String PAYLOAD_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage.jpg/jcr:content/renditions/original";
    private String ASSET_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage.jpg";
    private String ASSET_RESOURCE_CONTENT = "/workflow/test-asset.json";

    @Mock
    private Throwable replicationException;

    @Mock
    private WorkflowData workflowData;

    @Mock
    private WorkflowSession workflowSession;

    @Mock
    private MetaDataMap paramMetaDataMap;

    /** The model class. */
    Class<Node> nodeClass = Node.class;
    
    Asset asset;
    List<String> paths;

    @Mock
    Node node;

    @Before
    public void setup() throws RepositoryException {

        MockitoAnnotations.initMocks(this);
        aemContext.load().json(ASSET_RESOURCE_CONTENT,ASSET_PATH );
        aemContext.addModelsForClasses(nodeClass);
        Resource assetResource = aemContext.currentResource(ASSET_PATH);
        asset = assetResource.adaptTo(Asset.class);
        paramMetaDataMap = new SimpleMetaDataMap();
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(assetResource.getResourceResolver());
    }

    @Test
    public void testProcessWithArgument() {
        paramMetaDataMap.put("PROCESS_ARGS", "/content/dam/tetrapak/publicweb/pxp");
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
    }

    @Test
    public void testProcesswWithoutArgument() {
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
    }
}
