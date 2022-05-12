package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class TextVideoModelTest.
 */
public class TextVideoModelTest {

    @Rule
    public AemContext context = new AemContext();
    
    @Rule
    public AemContext context1 = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/textvideo/test-content.json";

    /** The Constant ANOTHER_RESOURCE_CONTENT. */
    private static final String ANOTHER_RESOURCE_CONTENT = "/textvideo/text-video-content.json";

    /** The Constant DAM_VIDEO_DATA. */
    private static final String DAM_VIDEO_DATA = "/textvideo/test-content1.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant ANOTHER_TEST_CONTENT_ROOT. */
    private static final String ANOTHER_TEST_CONTENT_ROOT = "/content/publicweb/en/home";

    /** The Constant DAM_VIDEO_CONTENT_ROOT. */
    private static final String DAM_VIDEO_CONTENT_ROOT = "/content/dam/publicweb";

    /** The Constant TEXTVIDEO_RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/textvideo";

    /** The Constant ANOTHER_RESOURCE_PATH. */
    private static final String ANOTHER_RESOURCE_PATH = ANOTHER_TEST_CONTENT_ROOT + "/jcr:content/textvideo";

    /** The model. */
    private TextVideoModel model;

    /** The text video model. */
    private TextVideoModel textVideoModel;

    /** The resource. */
    private Resource resource;

    /** The dynamic media service. */
    private DynamicMediaService dynamicMediaService;

    /**
     * Sets the up.
     *
     * @param context the new up
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        loadContextOne();
        loadContextTwo();
    }

    /**
     * Load context one.
     */
    private void loadContextOne() {
        // Dynamic Media Serivce
        dynamicMediaService = new DynamicMediaServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);

        final Class<TextVideoModel> modelClass = TextVideoModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.load().json(DAM_VIDEO_DATA, DAM_VIDEO_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

       MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE_PATH);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
        resource = context.currentResource(RESOURCE_PATH);
        model = request.adaptTo(modelClass);
    }

    /**
     * Load context two.
     */
    private void loadContextTwo() {
        final Class<TextVideoModel> modelClass = TextVideoModel.class;
        // load the resources for each object
        context1.load().json(ANOTHER_RESOURCE_CONTENT, ANOTHER_TEST_CONTENT_ROOT);
        context1.addModelsForClasses(modelClass);
        
        MockSlingHttpServletRequest request1 = context1.request();        
        request1.setPathInfo(ANOTHER_RESOURCE_PATH);
        request1.setResource(context1.resourceResolver().getResource(ANOTHER_RESOURCE_PATH));
        resource = context1.currentResource(ANOTHER_RESOURCE_PATH);
        textVideoModel = request1.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     */
    @Test
    public void simpleLoadAndGettersTest() {
        assertEquals("anchor123", model.getAnchorId());
        assertEquals("Anchor title", model.getAnchorTitle());
        assertEquals("Subtitle", model.getSubTitle());
        assertEquals("text video", model.getTitle());
        assertEquals("description for the text video to see how it behaves", model.getDescription());
        assertEquals("Test", model.getLinkTexti18n());
        assertEquals("/content/dam/publicweb/Petrol.pdf", model.getLinkURL());
        assertEquals("youtube", model.getVideoSource());
        assertEquals("1X7zhVCIUtg", model.getYoutubeVideoID());
        assertEquals("https://s7g10.scene7.com/is/content/tetrapak/Video", model.getDamVideoPath());
        assertEquals("/content/dam/publicweb/Video.mp4", model.getOriginalDamVideoPath());
        assertEquals("/content/dam/publicweb/asset.jpg", model.getThumbnailPath());
        assertEquals("/content/dam/publicweb/asset.jpg", model.getThumbnailAltText());
        assertEquals("secondary", model.getPwButtonTheme());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("regular", model.getPwPadding());
        assertEquals("display-row", model.getPwDisplay());
        assertEquals("true",model.getEnableSoftcoversion());
    }

    /**
     * Test not empty asset name.
     */
    @Test
    public void testNotEmptyAssetName() {
        assertEquals("Petrol.pdf", model.getAssetName());
    }

    /**
     * Test not empty video name.
     */
    @Test
    public void testNotEmptyVideoName() {
        assertEquals("Video", model.getVideoName());
    }

    /**
     * Test empty asset name.
     */
    @Test
    public void testEmptyAssetName() {
        assertEquals(StringUtils.EMPTY, textVideoModel.getAssetName());
    }

    /**
     * Test empty video name.
     */
    @Test
    public void testEmptyVideoName() {
        assertEquals(StringUtils.EMPTY, textVideoModel.getVideoName());
    }
}
