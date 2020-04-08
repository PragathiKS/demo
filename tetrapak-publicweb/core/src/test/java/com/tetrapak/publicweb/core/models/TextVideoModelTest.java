package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class TextVideoModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/textvideo/test-content.json";

    /** The Constant ANOTHER_RESOURCE_CONTENT. */
    private static final String ANOTHER_RESOURCE_CONTENT = "/textvideo/text-video-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant ANOTHER_TEST_CONTENT_ROOT. */
    private static final String ANOTHER_TEST_CONTENT_ROOT = "/content/publicweb/en/home";

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

    private void loadContextOne() {
        final Class<TextVideoModel> modelClass = TextVideoModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(RESOURCE_PATH);
        model = resource.adaptTo(modelClass);
    }

    private void loadContextTwo() {
        final Class<TextVideoModel> modelClass = TextVideoModel.class;
        // load the resources for each object
        context.load().json(ANOTHER_RESOURCE_CONTENT, ANOTHER_TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        resource = context.currentResource(ANOTHER_RESOURCE_PATH);
        textVideoModel = resource.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the accordion model
     *
     */
    @Test
    public void simpleLoadAndGettersTest() {
        final String[] methods = new String[] { "getAnchorId", "getAnchorTitle", "getSubTitle", "getTitle",
                "getDescription", "getLinkTexti18n", "getLinkURL", "getVideoSource",
                "getYoutubeVideoID", "getDamVideoPath", "getThumbnailPath", "getThumbnailAltText",
                "getPwTheme", "getPwButtonTheme", "getPwLinkTheme", "getPwPadding", "getPwDisplay" };
        Util.testLoadAndGetters(methods, model, resource);
    }

    @Test
    public void testNotEmptyAssetName() {
        assertEquals("Petrol.pdf", model.getAssetName());
    }

    @Test
    public void testNotEmptyVideoName() {
        assertEquals("Video.mp4", model.getVideoName());
    }

    @Test
    public void testEmptyAssetName() {
        assertEquals(StringUtils.EMPTY, textVideoModel.getAssetName());
    }

    @Test
    public void testEmptyVideoName() {
        assertEquals(StringUtils.EMPTY, textVideoModel.getVideoName());
    }
}
