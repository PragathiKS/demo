package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BannerModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/banner-test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/customerhub/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/banner";

    /** The model. */
    private BannerModel model;

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {

        Class<BannerModel> modelClass = BannerModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the ImageTextBanner model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("textImage", model.getBannerType());
        assertEquals("h1", model.getHeadingTag());
        assertEquals("Subtitle", model.getSubtitle());
        assertEquals("Title", model.getTitle());
        assertEquals("Lorem Ipsum is simply dummy text of the printing and typesetting industry.", model.getText());
        assertEquals("Link Label", model.getLink().getLinkText());
        assertEquals("/content/tetrapak/customerhub/global/en.html", model.getLink().getLinkUrl());
        assertEquals("internal", model.getLink().getLinkType());
        assertEquals("/content/dam/tetrapak/customerhub/image.png", model.getFileReference());
        assertEquals("Image", model.getAlt());
        assertEquals("display-row-reversed", model.getPwDisplay());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("link", model.getPwButtonTheme());
        assertEquals("test", model.getAnchorId());
        assertEquals("title", model.getAnchorTitle());
        assertEquals("103,146,629,672", model.getImageCrop());
        assertEquals("en.html", model.getAssetName());
        assertEquals("gray", model.getPwCardTheme());

    }
}
