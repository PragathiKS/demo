package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class VideoModelTest.
 */
public class VideoModelTest {

    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant RESOURCE_CONTENT.
     */
    private static final String RESOURCE_CONTENT = "/video/test-content.json";

    /** The Constant DAM_VIDEO_DATA. */
    private static final String DAM_VIDEO_DATA = "/textvideo/test-content1.json";

    /**
     * The Constant TEST_CONTENT_ROOT.
     */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant DAM_VIDEO_CONTENT_ROOT. */
    private static final String DAM_VIDEO_CONTENT_ROOT = "/content/dam/publicweb";

    /**
     * The Constant VIDEO_RESOURCE.
     */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/video";

    /** The model. */
    private VideoModel model;

    /**
     * The resource.
     */
    private Resource resource;

    /** The dynamic media service. */
    private DynamicMediaService dynamicMediaService;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        // Dynamic Media Serivce
        dynamicMediaService = new DynamicMediaServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);

        final Class<VideoModel> modelClass = VideoModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(DAM_VIDEO_DATA, DAM_VIDEO_CONTENT_ROOT);
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
        assertEquals("vid123", model.getAnchorId());
        assertEquals("video", model.getAnchorTitle());
        assertEquals("youtube", model.getVideoSource());
        assertEquals("qwe123", model.getYoutubeVideoID());
        assertEquals("https://s7g10.scene7.com/is/content/tetrapak/Video", model.getDamVideoPath());
        assertEquals("/content/dam/publicweb/Video.mp4", model.getOriginalDamVideoPath());
        assertEquals("/content/dam/publicweb/asset.jpg", model.getThumbnailPath());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("https://www.youtube.com/embed/qwe123?enablejsapi=1", model.getYoutubeEmbedURL());
        assertEquals("Poster image alt text", model.getPosterImageAltText());
    }

    @Test
    public void testVideoName() {
        assertEquals("Video", model.getVideoName());
    }
}
