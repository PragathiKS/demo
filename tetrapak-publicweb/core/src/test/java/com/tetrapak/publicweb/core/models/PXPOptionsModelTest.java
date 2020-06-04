package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PXPOptionsModelTest {
    /** The context. */
    @Rule
    public AemContext context = new AemContext();

    /**
     * The Constant PRODUCTS_DATA.
     */
    private static final String PRODUCTS_DATA = "/product/test-Products.json";

    /** The Constant DAM_VIDEO_DATA. */
    private static final String DAM_VIDEO_DATA = "/product/test-content4.json";

    /** The Constant EN_LANGUAGE_RESOURCE_CONTENT. */
    private static final String EN_LANGUAGE_RESOURCE_CONTENT = "/product/en.json";

    /**
     * The Constant EN_LANGUAGE_CONTENT_ROOT.
     */
    private static final String EN_LANGUAGE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en";

    /** The Constant TEST_RESOURCE_CONTENT1. */
    private static final String TEST_RESOURCE_CONTENT = "/pxpoptions/test-content.json";

    /**
     * The Constant FILLING_MACHINE_CONTENT_ROOT.
     */
    private static final String FILLING_MACHINE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/filling-machine";

    /** The Constant DAM_VIDEO_CONTENT_ROOT. */
    private static final String DAM_VIDEO_CONTENT_ROOT = "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/video";

    /** The model class. */
    Class<PXPOptionsModel> modelClass = PXPOptionsModel.class;

    /** The model. */
    private PXPOptionsModel model;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = FILLING_MACHINE_CONTENT_ROOT + "/jcr:content/pxpoptions";

    /** The resource. */
    private Resource resource;

    private DynamicMediaService dynamicMediaService;

    /** THE DYNAMIC MEDIA CONF. */
    String[] dynamicMediaConfMap = {
            "getstarted-desktop=1440\\,300,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1" };

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
        configuraionServiceConfig.put("rootPath", "/tetrapak");
        configuraionServiceConfig.put("dynamicMediaConfMap", dynamicMediaConfMap);
        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);

        // Set run modes
        context.runMode("publish");

        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        context.load().json(EN_LANGUAGE_RESOURCE_CONTENT, EN_LANGUAGE_CONTENT_ROOT);
        context.load().json(TEST_RESOURCE_CONTENT, FILLING_MACHINE_CONTENT_ROOT);
        context.load().json(DAM_VIDEO_DATA, DAM_VIDEO_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
    }

    /**
     * Test PXP options methods.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPXPOptionsMethods() throws Exception {
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
        assertEquals("PXPOptions", "Anchor title", model.getAnchorTitle());
        assertEquals("PXPOptions", "anchor123", model.getAnchorId());
        assertEquals("PXPOptions", "grayscale-white", model.getPwTheme());
        assertEquals("PXPOptions", "display-row", model.getPwDisplay());
        assertEquals("PXPOptions", "Options", model.getHeading());
        assertEquals("PXPOptions",
                "<p>The unit consists of a control cabinet, a heating element to sterilize air (or an inert gas) and a nozzle which injects the sterile air (or inert gas) to the product to create froth. When the froth settles down, it leaves a residual headspace which enables the content to be shaken and mixed. Using this method, products can be filled by weight so the choice of volumes is wider</p><p> </p>",
                model.getTabs().get(0).getDescription());
        assertEquals("PXPOptions", "Headspace by Injection", model.getTabs().get(0).getSubTitle());
        assertEquals("PXPOptions", "videoText", model.getTabs().get(0).getTabType());
        assertEquals("PXPOptions", "Headspace by Injection", model.getTabs().get(0).getThumbnailAltText());
        assertEquals("PXPOptions",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/thumbnail-4322_0003.jpg",
                model.getTabs().get(0).getThumbnailPath());
        assertEquals("PXPOptions", "a3_option_headspace_for_injection_mp4_1350227217",
                model.getTabs().get(0).getVideoName());
        assertEquals("PXPOptions", "damVideo", model.getTabs().get(0).getVideoSource());
        assertEquals("PXPOptions",
                "https://s7g10.scene7.com/is/content/tetrapak/a3_option_headspace_for_injection_mp4_1350227217",
                model.getTabs().get(0).getDamVideoPath());

    }
}
