package com.tetrapak.publicweb.core.models;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import io.wcm.testing.mock.aem.junit.AemContext;

public class MediaLinkModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/mediaLink/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/mediaLink";

    /** The model. */
    private MediaLinkModel model;

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

        Class<MediaLinkModel> modelClass = MediaLinkModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        model = request.adaptTo(modelClass);
    }

    @Test
    public void testValue() {
        assertEquals("Heading", model.getHeading());
        assertEquals("headingDesc", model.getHeadingDesc());
        assertEquals("mediaLink01", model.getAnchorId());
        assertEquals("Anchor  title", model.getAnchorTitle());
        assertEquals("grayscale-white", model.getPwTheme());
        assertEquals("title 1", model.getColumnOneTitle());
        assertEquals("columnOneDesc", model.getColumnOneDesc());
        assertEquals("title 2", model.getColumnTwoTitle());
        assertEquals("columnTwoDesc", model.getColumnTwoDesc());
        assertEquals("title 3", model.getColumnThreeTitle());
        assertEquals("columnThreeDesc", model.getColumnThreeDesc());
        assertEquals("Link Label 3", model.getColumnThreeList().get(0).getLinkLabel());
        assertEquals("Link Label 2", model.getColumnTwoList().get(0).getLinkLabel());
        assertEquals("Link Label 1", model.getColumnOneList().get(0).getLinkLabel());
        assertEquals("/content/tetrapak/publicweb/lang-master/en/test.html",
                model.getColumnOneList().get(0).getLinkUrl());
        assertEquals("/content/tetrapak/publicweb/lang-master/en/home.html",
                model.getColumnTwoList().get(0).getLinkUrl());
        assertEquals("/content/tetrapak/publicweb/lang-master/en/solution.html",
                model.getColumnThreeList().get(0).getLinkUrl());
        assertEquals("linkDesc", model.getColumnOneList().get(0).getLinkDesc());
        assertEquals("linkDesc", model.getColumnTwoList().get(0).getLinkDesc());
        assertEquals("linkDesc", model.getColumnThreeList().get(0).getLinkDesc());
    }
}
