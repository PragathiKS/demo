package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * The Class BreadcrumbModelTest.
 */
public class BreadcrumbModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/breadcrumb-test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/package-design/primary";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/check/checkdisableLink/jcr:content";

    /** The model. */
    private BreadcrumbModel model;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        final Class<BreadcrumbModel> modelClass = BreadcrumbModel.class;
        final MockSlingHttpServletRequest request = context.request();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        context.request().setPathInfo(TEST_CONTENT_ROOT + "/check/checkdisableLink");
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the BreadcrumbModel model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("/content/tetrapak/customerhub/global/en/package-design/primary.html",
                model.getBreadcrumbSubpages().get("Primary"));
        assertEquals(null, model.getBreadcrumbSubpages().get("check"));
        assertEquals("/content/tetrapak/customerhub/global/en/package-design/primary/check/checkdisableLink.html",
                model.getBreadcrumbSubpages().get("Check disable link"));
        assertEquals("There should have been out put as 2",2, model.getCurrentPageParentIndex());
        assertEquals("/content/tetrapak/customerhub/global/en/dashboard.html", model.getHomePagePath());
        assertNull(model.getHomePageTitle());
    }

}
