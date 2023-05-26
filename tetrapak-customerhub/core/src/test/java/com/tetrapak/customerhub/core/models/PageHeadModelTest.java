package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PageHeadModelTest {

    @Rule
    public AemContext context = new AemContext();

    private static final String RESOURCE_CONTENT = "/allContent.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/dashboard";
    
    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    private PageHeadModel pageHeadModel;

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

        resource = context.currentResource(RESOURCE);
        assert resource != null;
        pageHeadModel = context.request().adaptTo(PageHeadModel.class);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     * @throws Exception exception
     */
    @Test
    public void testLoadAndGetters() throws Exception {
        assertNotNull(pageHeadModel);
        assertEquals(pageHeadModel.getCookieTokenServletUrl(), RESOURCE + CustomerHubConstants.ONETRUST_TOKEN_SERVLET_URL);
    }

}