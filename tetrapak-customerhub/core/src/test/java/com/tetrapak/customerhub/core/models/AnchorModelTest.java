package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnchorModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/anchor-content.json";

    private static final String RESOURCE_REF_CONTENT = "/anchor-content-reference.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/dashboard";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/anchor";

    private static final String REF_RESOURCE_PATH = "/content/tetrapak/customerhub/content-components/en/test-anchor/jcr:content/root/responsivegrid/richtext";


    private AnchorModel anchorModel;

    /**
     * The resource.
     */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(RESOURCE_REF_CONTENT, REF_RESOURCE_PATH);

        resource = context.currentResource(RESOURCE);
        assert resource != null;
        anchorModel = context.request().adaptTo(AnchorModel.class);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     * @throws Exception exception
     */
    @Test
    public void testLoadAndGetters() throws Exception {
        assertNotNull(anchorModel);
        assertEquals(anchorModel.getAnchorDetailList().size(), 3);
        assertEquals(anchorModel.getAnchorDetailList().get(0).getAnchorId(), "rteidFirst");
        assertEquals(anchorModel.getAnchorDetailList().get(1).getAnchorTitle(), "rte achor title secound");
        assertEquals(anchorModel.getAnchorDetailList().get(2).getAnchorId(), "anchorTest");
        assertEquals(anchorModel.getAnchorDetailList().get(2).getAnchorTitle(), "What is this?");
    }

}