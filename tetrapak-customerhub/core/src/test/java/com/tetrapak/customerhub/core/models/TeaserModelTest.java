package com.tetrapak.customerhub.core.models;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * The Class TeaserModelTest.
 */
public class TeaserModelTest {

    @Rule
    public AemContext context = new AemContext();
  

    /** The Constant RESOURCE_CONTENT_MANUAL. */
    private static final String RESOURCE_CONTENT_MANUAL = "/teaser-test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/dashboard/test-page";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/teaser";

    /** The Constant RESOURCE_TWO. */
    private static final String RESOURCE_TWO = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/teaser";

    /** The model. */
    private TeaserModel model;

    Class<TeaserModel> modelClass = TeaserModel.class;

    /** The resource. */
    @Mock
    Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.addModelsForClasses(modelClass);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMethodsManual() throws Exception {

        context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);

        assertEquals("Heading Test", model.getHeading());
        assertEquals("Description Test", model.getDescription());
        assertEquals("View All Test", model.getViewAllLink().getLinkText());
        assertEquals("grayscale-white", model.getPwTheme());

        assertEquals("Title 2 test", model.getTeaserList().get(1).getTitle());
        assertEquals("Body 2 test", model.getTeaserList().get(1).getDescription());
        assertEquals("/content/dam/we-retail/en/experiences/arctic-surfing-in-lofoten/camp-fire.jpg",
                model.getTeaserList().get(1).getFileReference());
        assertEquals("Alt 2 test", model.getTeaserList().get(1).getAlt());
        assertEquals("Link 2 Text test", model.getTeaserList().get(1).getLink().getLinkText());
        assertEquals("https://test2.com", model.getTeaserList().get(1).getLink().getLinkUrl());
    }

    /**
     * Test asset name.
     *
     * @throws Exception the exception
     */
    @Test
    public void testAssetName() throws Exception {
        context.load().json(RESOURCE_CONTENT_MANUAL, TEST_CONTENT_ROOT);
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE_TWO);
        request.setResource(context.resourceResolver().getResource(RESOURCE_TWO));
        resource = context.currentResource(RESOURCE_TWO);
        model = request.adaptTo(modelClass);
        assertEquals("abc.pdf", model.getTeaserList().get(0).getAssetName());
    }
}