package com.trs.core.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.trs.core.exceptions.PageNotCreatedException;
import com.trs.core.reports.StatefulReport;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class AssetPageOpsServiceImplTest {

    @Mock
    TrsConfigurationService trsConfig;

    @Mock
    private Replicator replicator;

    @InjectMocks
    private final AssetPageOpsServiceImpl assetPageOpsServiceImpl = new AssetPageOpsServiceImpl();

    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @BeforeEach
    void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        TestUtils.setUpAssetPageOpsService(context, TestUtils.TEST_ASSET_PATH, replicator);
        TestUtils.setupTrsConfiguration(context, trsConfig);
    }

    @Test
    final void testCreateTrsPageResourceResolverStringStatefulReport() {

        StatefulReport statefulReport = new StatefulReport.Builder().reportWorkbook().reportWorkBooksheet().build();
        assetPageOpsServiceImpl.createTrsPage(context.resourceResolver(),
                "/content/dam/training-services/test/video2.mp4", statefulReport);
        assertEquals(1, statefulReport.getRowCounter(),
                "Counter should have value corresponding to expected number of pages created");

    }

    @Test
    final void testCreateTrsPageResourceResolverString() throws PageNotCreatedException {

        Map<String, String> responseMap = assetPageOpsServiceImpl.createTrsPage(context.resourceResolver(),
                "/content/dam/training-services/test/video2.mp4");
        assertEquals("/content/trs/en/test/video2", responseMap.get("pagePath"),
                "Page path should have been /content/trs/en/test/video2");

    }

    @Test
    final void testReplicationActionOnResource() {

        final ReplicationActionType actionType = ReplicationActionType.ACTIVATE;
        Session session = context.resourceResolver().adaptTo(Session.class);
        assetPageOpsServiceImpl.replicationActionOnResource(context.resourceResolver(), actionType,
                TestUtils.TEST_ASSET_PATH);
        try {
            verify(replicator, times(1)).replicate(eq(session), eq(actionType), eq(TestUtils.TEST_ASSET_PATH));
        } catch (ReplicationException e) {
            fail("testReplicationActionOnResource test case failed : ReplicationException");
        }

    }

    @Test
    final void testCreatePageCreationReport() {

        StatefulReport report = assetPageOpsServiceImpl.createPageCreationReport();
        assertNotNull(report, "testCreatePageCreationReport test case failed : Report object should not be null");
    }

}
