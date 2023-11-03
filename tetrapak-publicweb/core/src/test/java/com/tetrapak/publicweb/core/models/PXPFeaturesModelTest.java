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

public class PXPFeaturesModelTest {
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
    private static final String TEST_RESOURCE_CONTENT = "/pxpfeatures/test-content.json";

    /**
     * The Constant FILLING_MACHINE_CONTENT_ROOT.
     */
    private static final String FILLING_MACHINE_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-master/en/filling-machine";

    /** The model class. */
    Class<PXPFeaturesModel> modelClass = PXPFeaturesModel.class;

    /** The model. */
    private PXPFeaturesModel model;

    /**
     * The Constant PXP_FEATURES.
     */
    private static final String RESOURCE = FILLING_MACHINE_CONTENT_ROOT + "/jcr:content/pxpfeatures";

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
        context.addModelsForClasses(modelClass);
    }

    /**
     * Test PXP features methods.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testPXPFeaturesMethods() throws Exception {
        resource = context.currentResource(RESOURCE);
        model = resource.adaptTo(modelClass);
        assertEquals("PXPFeatures", "Anchor title", model.getAnchorTitle());
        assertEquals("PXPFeatures", "anchor123", model.getAnchorId());
        assertEquals("PXPFeatures", "grayscale-white", model.getPwTheme());
        assertEquals("PXPFeatures", "display-row", model.getPwDisplay());
        assertEquals("PXPFeatures", "Features", model.getHeading());
        assertEquals("PXPFeatures",
                "<p>There's no need to halt production in order to refill with packaging material or strips since both are replaced automatically. Automation also ensures that splices between packaging material reels and strip applications are of consistent, repeatable quality.</p>",
                model.getTabs().get(0).getDescription());
        assertEquals("PXPFeatures", "Automatic material supply", model.getTabs().get(0).getSubTitle());
        assertEquals("PXPFeatures", "videoText", model.getTabs().get(0).getTabType());
        assertEquals("PXPFeatures", "Automatic material supply", model.getTabs().get(0).getThumbnailAltText());
        assertEquals("PXPFeatures",
                "/content/dam/tetrapak/publicweb/pxp/fillingmachines/equipment1272/image/thumbnail-3881_0000.jpg",
                model.getTabs().get(0).getThumbnailPath());

    }
}