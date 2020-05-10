package com.tetrapak.publicweb.core.workflow;

import static org.mockito.Mockito.when;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
import com.day.cq.replication.Replicator;

import io.wcm.testing.mock.aem.junit.AemContext;

public class AssetReplicationWorkflowProcessTest {

    @Mock
    private WorkItem workItem;

    @Mock
    private WorkflowData workflowData;

    @Mock
    private WorkflowSession workflowSession;

    @Mock
    private MetaDataMap paramMetaDataMap;

    @Mock
    private Session session;

    @Mock
    Replicator replicator;

    Resource assetResource;
    Resource withoutPropAssetResource;

    /** The site search servlet. */
    @InjectMocks
    private final AssetReplicationWorkflowProcess workflowProcess = new AssetReplicationWorkflowProcess();

    @Rule
    public final AemContext aemContext = new AemContext();

    private final String PAYLOAD_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage.jpg/jcr:content/renditions/original";
    private final String NON_PROP_PAYLOAD_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage2.jpg/jcr:content/renditions/original";

    private final String ASSET_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage.jpg";
    private final String NON_PROP_ASSET_PATH = "/content/dam/tetrapak/publicweb/pxp/category4/product3/images/myimage2.jpg";

    private final String ASSET_RESOURCE_CONTENT = "/workflow/test-asset.json";
    private final String ASSET_WITHOUTPROP_RESOURCE_CONTENT = "/workflow/test-asset2.json";

    @Before
    public void setup() throws RepositoryException {
        aemContext.load().json(ASSET_RESOURCE_CONTENT, ASSET_PATH);
        aemContext.load().json(ASSET_WITHOUTPROP_RESOURCE_CONTENT, NON_PROP_ASSET_PATH);

        MockitoAnnotations.initMocks(this);

        assetResource = aemContext.currentResource(ASSET_PATH);
        withoutPropAssetResource = aemContext.currentResource(NON_PROP_ASSET_PATH);
        when(workItem.getWorkflowData()).thenReturn(workflowData);
        when(workflowSession.adaptTo(Session.class)).thenReturn(session);
    }

    @Test
    public void testProcessWithArgument() {
        paramMetaDataMap = new SimpleMetaDataMap();
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(assetResource.getResourceResolver());
        paramMetaDataMap.put("PROCESS_ARGS", "/content/dam/tetrapak/publicweb/pxp");
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);

    }

    @Test
    public void testProcessWithnonPXPPath() {
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(assetResource.getResourceResolver());

        paramMetaDataMap = new SimpleMetaDataMap();
        paramMetaDataMap.put("PROCESS_ARGS", "/content/dam/tetrapak/publicweb/test/pxp");
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);

    }

    @Test
    public void testProcessWithNoProperty() {
        when(workflowData.getPayload()).thenReturn(NON_PROP_PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class))
                .thenReturn(withoutPropAssetResource.getResourceResolver());

        paramMetaDataMap = new SimpleMetaDataMap();
        paramMetaDataMap.put("PROCESS_ARGS", "/content/dam/tetrapak/publicweb/test/pxp");
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);

    }

    @Test
    public void testProcesswWithoutArgument() {
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(assetResource.getResourceResolver());

        paramMetaDataMap = new SimpleMetaDataMap();
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
    }

    @Test
    public void testNullResourceResolver() {
        when(workflowData.getPayload()).thenReturn(PAYLOAD_PATH);
        when(workflowSession.adaptTo(ResourceResolver.class)).thenReturn(null);

        paramMetaDataMap = new SimpleMetaDataMap();
        workflowProcess.execute(workItem, workflowSession, paramMetaDataMap);
    }
}
