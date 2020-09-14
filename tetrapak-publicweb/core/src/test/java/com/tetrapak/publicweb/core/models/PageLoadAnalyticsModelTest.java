package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * The Class PageLoadAnalyticsModelTest.
 */
public class PageLoadAnalyticsModelTest {

    /**
     * The context.
     */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/pageContent/test-content.json";

    /**
     * The Constant TEST_CONTENT_ROOT.
     */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/solutions/turn-ingredients-into-products/product1";

    /**
     * The Constant RESOURCE.
     */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    /**
     * The model.
     */
    private PageLoadAnalyticsModel model;

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

        Class<PageLoadAnalyticsModel> modelClass = PageLoadAnalyticsModel.class;
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the PageLoadAnalytics model.
     *
     * @throws Exception the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        String[] methods = new String[]{"isProduction", "isStaging", "isDevelopment", "getDigitalData","getCurrentPageURL" , "getHreflangValues", "getCanonicalURL"};
        Util.testLoadAndGetters(methods, model, resource);
    }
}
