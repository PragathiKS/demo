package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
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

        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE_PATH);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
        resource = context.currentResource(RESOURCE_PATH);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the ImageTextButton model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("anchor123", model.getAnchorId());
        assertEquals("Anchor title", model.getAnchorTitle());
        assertEquals("Subtitle", model.getSubTitle());
        assertEquals("h1", model.getHeadingTag());
        assertEquals("Title", model.getTitle());
        assertEquals("Description", model.getDescription());
        assertEquals("/content/dam/publicweb/asset.jpg", model.getImagePath());
        assertEquals("Desktop Image Alt Text", model.getImageAltText());
        assertEquals("Test", model.getLinkText());
        assertEquals("/content/dam/publicweb/Petrol.pdf", model.getLinkURL());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("secondary", model.getPwButtonTheme());
        assertEquals("display-row", model.getPwDisplay());
        assertEquals("Petrol.pdf", model.getAssetName());
        
    }

}
