package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SiteImproveConfigurationModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/siteImprove/site-improve.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";

    /** The Constant ANOTHER_TEST_CONTENT_ROOT. */
    /** The Constant TEXTVIDEO_RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/siteimproveconfig";

    /** The model. */
    private SiteImproveConfigurationModel model;

    /**
     * Initial setup.
     *
     * @param context the new up
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        final Class<SiteImproveConfigurationModel> modelClass = SiteImproveConfigurationModel.class;
        final MockSlingHttpServletRequest request = context.request();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
        model = request.adaptTo(modelClass);
    }

    /**
     * Test site improve script path.
     */
    @Test
    public void testSiteImproveScriptPath() {
        assertEquals("//siteimproveanalytics.com/js/siteanalyze_72177.js", model.getSiteImproveScriptPath());
    }
}
