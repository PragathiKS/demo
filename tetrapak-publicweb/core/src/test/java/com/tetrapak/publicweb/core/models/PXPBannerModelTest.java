package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.constants.PWConstants;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class PXPBannerModelTest.
 */
public class PXPBannerModelTest {

    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant PRODUCTS_DATA.
     */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant EN_LANGUAGE_RESOURCE_CONTENT. */
    private static final String EN_LANGUAGE_RESOURCE_CONTENT = "/product/en.json";
    
    /**
     * The Constant EN_LANGUAGE_CONTENT_ROOT.
     */
    private static final String EN_LANGUAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en";
    
    /** The Constant TEST_RESOURCE_CONTENT1. */
    private static final String TEST_RESOURCE_CONTENT = "/pxpbanner/test-content.json";

    /**
     * The Constant FILLING_MACHINE_CONTENT_ROOT.
     */
    private static final String FILLING_MACHINE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/filling-machine";
    
    /** The model class. */
    Class<PXPBannerModel> modelClass = PXPBannerModel.class;
    
    /** The model. */
    private PXPBannerModel model;
    
    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = FILLING_MACHINE_CONTENT_ROOT + "/jcr:content/pxpbanner";

    /** The resource. */
    private Resource resource;

    /**
     * Sets the up.
     *
     * @throws Exception             the exception
     */
    @Before
    public void setUp() throws Exception {

        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(EN_LANGUAGE_RESOURCE_CONTENT, EN_LANGUAGE_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT, FILLING_MACHINE_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
    }

    /**
     * Test model, resource and all getters of the ImageTextBanner model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testBannerMethods() throws Exception {
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
        assertEquals("PXPBanner", "Versatility of shapes, sizes and closures", model.getTitle());
        assertEquals("PXPBanner", "Tetra Pak® A3/Flex", model.getSubTitle());
        assertEquals("PXPBanner",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/A3-Flex-no-DIMC-Benefits.png",
                model.getImagePath());
        assertEquals("PXPBanner",
                "<ul><li>Easy conversion between different family package shapes and sizes</li><li>Easy adaptation to many different closures</li><li>High food safety (unique aseptic process FDA filed)</li></ul>",
                model.getDescription());

        assertEquals("PXPBanner","h1", model.getHeadingTag());
        assertEquals("PXPBanner", "Anchor title", model.getAnchorTitle());
        assertEquals("PXPBanner", "anchor123", model.getAnchorId());
        assertEquals("PXPBanner", "grayscale-white", model.getPwTheme());
        assertEquals("PXPBanner", "display-row", model.getPwDisplay());
        assertEquals("PXPBanner", "Tetra Pak® A3/Flex", model.getImageAltText());
    }
}
