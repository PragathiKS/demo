package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class VideoModelTest {

    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/video/test-content.json";

    /**
     * The Constant TEST_CONTENT_ROOT.
     */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /**
     * The Constant VIDEO_RESOURCE.
     */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/video";

    private VideoModel model;

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

        Class<VideoModel> modelClass = VideoModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     * @throws Exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        String[] methods = new String[]{"getTitle", "getDescription", "getLinkTexti18n", "getLinkURL",
                "getTargetBlank", "getVideoSource", "getYoutubeVideoID", "getYoutubeEmbedURL", "getDamVideoPath",
                "getThumbnailPath", "getTextAlignment", "getPwTheme", "getPwButtonTheme", "getPwPadding",
                "getPwDisplay","getAnchorId", "getAnchorTitle"};
        Util.testLoadAndGetters(methods, model, resource);
    }

    @Test
    public void testVideoName() {
        assertEquals("Video.mp4", model.getVideoName());
    }
}
