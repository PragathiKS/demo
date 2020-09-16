package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ImageModelTest {

    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/image/test-content.json";

    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en/sustainability";

    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/image";


    private ImageModel model;

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

        Class<ImageModel> modelClass = ImageModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        assert resource != null;
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     * @throws Exception exception
     */
    @Test
    public void testLoadAndGetters() throws Exception {
        assertEquals("/content/dam/tetrapak/publicweb/TetraPakLogo.png", model.getFileReference());
        assertEquals("alt", model.getAlt());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/home.html", model.getLinkURL());
        assertEquals("regular", model.getPwPadding());
        assertEquals("imageid", model.getAnchorId());
        assertEquals("image", model.getAnchorTitle());
        assertEquals("grayscale-lighter", model.getPwTheme());
    }
}
