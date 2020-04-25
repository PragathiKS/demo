package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class MegaMenuConfigurationModelTest.
 */
public class MegaMenuConfigurationSolutionModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/megamenu/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig";

    /** The model. */
    private MegaMenuConfigurationModel model;

    /**
     * The setup method.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        final Class<MegaMenuConfigurationModel> modelClass = MegaMenuConfigurationModel.class;
        // load the resources for each object
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        final Resource resource = context.currentResource(RESOURCE_PATH);
        model = resource.adaptTo(modelClass);
    }

    /**
     * Test subtitles and url.
     */
    @Test
    public void testSubtitlesAndUrl() {
        assertEquals("Tetra pak end to end solution", model.getTopSectionSubtitle());
        assertEquals("/content/tetrapak/public-web/language-masters/en/solutions", model.getTopSectionUrl());
        assertEquals("Food categories", model.getBottomSectionSubtitle());
        assertEquals("/content/tetrapak/public-web/language-masters/en/food", model.getBottomSectionUrl());
    }

    /**
     * Test hide food categories.
     */
    @Test
    public void testHideFoodCategories() {
        assertTrue(model.getHideFoodCategories());
    }

    /**
     * Test end to end solution section.
     */
    @Test
    public void testEndToEndSolutionSection() {
        assertEquals("Lorem ipsum", model.getEndToEndSolutionSection().get(0).getDescription());
        assertEquals("/content/dam/tetrapak/processing.jpg",
                model.getEndToEndSolutionSection().get(0).getFileReference());
        assertEquals("Processing", model.getEndToEndSolutionSection().get(0).getAlt());
        assertEquals("/content/tetrapak/public-web/lang-masters/en.html",
                model.getEndToEndSolutionSection().get(0).getPath());
        assertEquals("Processing", model.getEndToEndSolutionSection().get(0).getTitle());
    }

    /**
     * Test link section.
     */
    @Test
    public void testLinkSection() {
        assertEquals("Aseptic solutions", model.getLinkSection().get(0).getLinkText());
        assertEquals("/content/tetrapak/public-web/language-masters/en/aseptic.html",
                model.getLinkSection().get(0).getLinkUrl());
    }

    /**
     * Test food category section.
     */
    @Test
    public void testFoodCategorySection() {
        assertEquals("/content/dam/tetrapak/cheese.jpg", model.getFoodCategorySection().get(0).getFileReference());
        assertEquals("Cheese", model.getFoodCategorySection().get(0).getAlt());
        assertEquals("/content/tetrapak/public-web/lang-masters/en/food-categories/cheese.html",
                model.getFoodCategorySection().get(0).getPath());
        assertEquals("Cheese", model.getFoodCategorySection().get(0).getTitle());
    }
}
