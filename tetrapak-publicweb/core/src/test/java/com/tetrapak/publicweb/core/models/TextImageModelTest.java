package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class TextImageModelTest.
 */
public class TextImageModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/textImage/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/textimage";

    /** The model. */
    private TextImageModel model;

    /** The resource. */
    private Resource resource;

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
        final Class<TextImageModel> modelClass = TextImageModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the ImageTextButton model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        final String[] methods = new String[] { "getAnchorId", "getAnchorTitle", "getSubTitle", "getTitle",
                "getDescription", "getImagePath", "getImageAltText", "getLinkText", "getLinkURL", "getPwTheme",
                "getPwButtonTheme", "getPwDisplay" };
        Util.testLoadAndGetters(methods, model, resource);
    }

    @Test
    public void testNotEmptyAssetName() {
        assertEquals("Petrol.pdf", model.getAssetName());
    }
}
