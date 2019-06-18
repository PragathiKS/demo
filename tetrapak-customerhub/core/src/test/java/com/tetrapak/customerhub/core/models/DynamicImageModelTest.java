package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.customerhub.core.services.DynamicMediaService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DynamicImageModelTest {
    
    private DynamicImageModel dynamicImageModel;
    private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/about-us/jcr:content/par/getstarted";
    
    private DynamicMediaService dynamicMediaService;
    
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(GET_STARTED_RESOURCE_JSON,
            COMPONENT_PATH);
    
    @Before
    public void setup() {
        
        dynamicMediaService = new MockDynamicMediaServiceImpl();
        aemContext.registerService(DynamicMediaService.class, dynamicMediaService);        
        aemContext.currentResource(COMPONENT_PATH);
        aemContext.request().setAttribute("imagePath", "/tetrapak/p2.png");
        aemContext.request().setAttribute("altText", "Image1 alt");
        dynamicImageModel = aemContext.request().adaptTo(DynamicImageModel.class);
    }
    
    @Test
    public void testGetStartedMessage() {        
        String rootPath = dynamicImageModel.getRootPath();
        String imageServiceUrl = dynamicImageModel.getImageServiceURL();
        String dynamicImage = dynamicImageModel.getAltText();
        String finalPath = dynamicImageModel.getFinalPath();
        Assert.assertEquals("Image1 alt", dynamicImage);
        assertNotNull(rootPath);
        assertNotNull(imageServiceUrl);
        Assert.assertEquals("/tetrapak/p2", finalPath);
        Assert.assertEquals(rootPath, "/tetrapak");
        Assert.assertEquals(imageServiceUrl, "http://s7g10.scene7.com/is/image");        
        String[] dynamicMediaConfMap = dynamicImageModel.getDynamicMediaConfiguration();
        assertTrue(dynamicMediaConfMap.toString().length() > 0);
    }
    
}
