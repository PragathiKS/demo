package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TextVideoModel class.
 *
 * @author tustusha
 */
public class TextVideoModelTest {

    private TextVideoModel textVideoModel;

    private DynamicMediaService dynamicMediaService;

    private static final String TEXT_VIDEO_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/dashboard/jcr:content/par/textvideo";
    private static final String TEXT_VIDEO_RESOURCE_JSON = "textvideo.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEXT_VIDEO_RESOURCE_JSON,
            TEXT_VIDEO_CONTENT_ROOT);

    /**
     * Setup method for the class.
     */
    @Before
    public void setup() {
        dynamicMediaService = new MockDynamicMediaServiceImpl();
        aemContext.registerService(DynamicMediaService.class, dynamicMediaService);
        Resource resource = aemContext.currentResource(TEXT_VIDEO_CONTENT_ROOT);
        textVideoModel = resource.adaptTo(TextVideoModel.class);
    }

    /**
     * Test method for TextVideo model.
     */
    @Test
    public void testTextVideoModel() {
        String title = textVideoModel.getTitle();
        assertNotNull("Title is not null", title);
        assertTrue("Title is not empty", title.length() > 0);
        Assert.assertEquals("title", "Turn your products into success", title);

        String description = textVideoModel.getDescription();
        assertNotNull("Description should not be null", description);
        assertTrue("Description is not empty", description.length() > 0);
        Assert.assertEquals("description", "Food, beverages or cosmetics? No matter what product,"
                + " we provide you with the processing and packaging solutions that"
                + " suit your needs. We collaborate with you to develop recipes and"
                + " design production processes with smart environmental thinking."
                + " We help you turning ideas into products that your customer love.", description);

        String linkText = textVideoModel.getLinkTextI18n();
        assertNotNull("Link text should not be null", linkText);
        assertTrue("Link text is not empty", linkText.length() > 0);
        Assert.assertEquals("linkText", "Discover what we can do for your products", linkText);

        String linkURL = textVideoModel.getLinkURL();
        assertNotNull("Link URL should not be null", linkURL);
        assertTrue("Link URL is not empty", linkURL.length() > 0);
        Assert.assertEquals("linkUrl", "https://google.com", linkURL);

        Boolean isExternal = textVideoModel.isExternal();
        assertNotNull("isExternal field should not be null", isExternal);
        Assert.assertEquals("isExternal", true, isExternal);

        String videoSource = textVideoModel.getVideoSource();
        assertNotNull("Video source should not be null", videoSource);
        assertTrue("Video source is not empty", videoSource.length() > 0);
        Assert.assertEquals("video source", "dam", videoSource);

        String youTubeVideoId = textVideoModel.getYoutubeVideoID();
        assertNotNull("Youtube ID should not be null", youTubeVideoId);
        assertTrue("Youtube ID is not empty", youTubeVideoId.length() > 0);
        Assert.assertEquals("you tube video ID", "ZofQqevIIZA", youTubeVideoId);

        String youTubeEmbedURL = textVideoModel.getYoutubeEmbedURL();
        assertNotNull("Youtube embed URL should not be null", youTubeEmbedURL);
        assertTrue("Youtube embed URL is not empty", youTubeEmbedURL.length() > 0);
        Assert.assertEquals("you tube embed url", "https://www.youtube.com/embed/ZofQqevIIZA", youTubeEmbedURL);

        String damVideoPath = textVideoModel.getDamVideoPath();
        assertNotNull("DAM video path should not be null", damVideoPath);
        assertTrue("DAM video path is not empty", damVideoPath.length() > 0);
        Assert.assertEquals("dam video path", "http://s7g10.scene7.com/is/content/tetrapak/SampleVideo_1280x720_5mb", damVideoPath);

        String thumbnailPath = textVideoModel.getThumbnailPath();
        assertNotNull("Thumbnail path should not be null", thumbnailPath);
        assertTrue("Thumbnail path is not empty", thumbnailPath.length() > 0);
        Assert.assertEquals("thumbnail path", "/content/dam/customerhub/get_started-3.PNG", thumbnailPath);

        String textAlignment = textVideoModel.getTextAlignment();
        assertNotNull("Text alignment should not be null", textAlignment);
        assertTrue("Text alignment is not empty", textAlignment.length() > 0);
        Assert.assertEquals("text alignment", "right", textAlignment);

    }

}
