package com.tetrapak.publicweb.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThumbnailConfigModelTest {

    private static final String TEST_RESOURCE_CONTENT_WITH_CONFIG = "/thumbnailConfig/test-content.json";

    private static final String TEST_RESOURCE_CONTENT_WITHOUT_CONFIG = "/thumbnailConfig/test-content-without-config.json";

    private static final String CONTENT_ROOT_WITH_CONFIG = "/content/tetrapak/publicweb/en";

    private static final String CONTENT_ROOT_WITH_NO_CONFIG = "/content/tetrapak/publicweb/de";

    private static final String TEST_PAGE_NODE_WITH_THUMBNAIL = "page-with-thumbnail";

    private static final String TEST_PAGE_NODE_WITHOUT_THUMBNAIL = "page-without-thumbnail";

    @Rule
    public AemContext context = new AemContext();

    @Before
    public void setUp() throws Exception {
        context.load().json(TEST_RESOURCE_CONTENT_WITH_CONFIG, CONTENT_ROOT_WITH_CONFIG);
        context.load().json(TEST_RESOURCE_CONTENT_WITHOUT_CONFIG, CONTENT_ROOT_WITH_NO_CONFIG);
    }

    @Test
    public void testValidThumbnailFromPageProperties() {
        Resource resource = context.resourceResolver().getResource(CONTENT_ROOT_WITH_CONFIG + "/" + TEST_PAGE_NODE_WITH_THUMBNAIL);
        ThumbnailConfigModel model = resource.adaptTo(ThumbnailConfigModel.class);
        assertTrue(model.isValid());
    }

    @Test
    public void testValidThumbnailFromLangConfiguration() {
        Resource resource = context.resourceResolver().getResource(CONTENT_ROOT_WITH_CONFIG + "/" + TEST_PAGE_NODE_WITHOUT_THUMBNAIL);
        ThumbnailConfigModel model = resource.adaptTo(ThumbnailConfigModel.class);
        assertTrue(model.isValid());
    }

    @Test
    public void testIfNotValidWhenNoConfigFound() {
        Resource resource = context.resourceResolver().getResource(CONTENT_ROOT_WITH_NO_CONFIG + "/" + TEST_PAGE_NODE_WITHOUT_THUMBNAIL);
        ThumbnailConfigModel model = resource.adaptTo(ThumbnailConfigModel.class);
        assertFalse(model.isValid());
    }
}
