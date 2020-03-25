package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

public class HeaderModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/header/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    /** The model. */
    private HeaderModel model;

    /** The resource. */
    private Resource resource;

    @Mock
    private HeaderConfigurationModel headerConfig;

    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        Class<HeaderModel> modelClass = HeaderModel.class;
        MockSlingHttpServletRequest request = context.request();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the ArticleContainer model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        String[] methods = new String[] { "getLogoLink", "getLogoLinkTarget", "getLogoAlt", "getLoginLabel",
                "getLoginLink", "getContactLink", "getContactLinkTarget", "getContactText", "getLoginLabel",
                "getMegaMenuLinksList" };
        Util.testLoadAndGetters(methods, model, resource);
    }
}
