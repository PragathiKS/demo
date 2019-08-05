package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DynamicImageModelTest {
    
    private DynamicImageModel dynamicImageModel;
    private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";
    private static final String dam_JSON = "dam.json";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/about-us/jcr:content/par/getstarted";
    
    private DynamicMediaService dynamicMediaService;
    
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(GET_STARTED_RESOURCE_JSON,
            COMPONENT_PATH);
    
    @Before
    public void setup() {
        aemContext.load().json("/" + dam_JSON, "/content/dam/customerhub/p2.png");
        dynamicMediaService = new MockDynamicMediaServiceImpl();
        aemContext.registerService(DynamicMediaService.class, dynamicMediaService);        
        aemContext.currentResource(COMPONENT_PATH);
        aemContext.request().setAttribute("imagePath", "/content/dam/customerhub/p2.png");
        aemContext.request().setAttribute("altText", "Image1 alt");
        aemContext.request().setAttribute("imageCrop", "390,57,947,473");
        aemContext.request().setAttribute("dwidth", "500");
        aemContext.request().setAttribute("dheight", "600");
        aemContext.request().setAttribute("mwidthl", "100");
        aemContext.request().setAttribute("mheightl", "150");
        aemContext.request().setAttribute("mwidthp", "250");
        aemContext.request().setAttribute("mheightp", "100");
        dynamicImageModel = aemContext.request().adaptTo(DynamicImageModel.class);
    }
    
    @Test
    public void testGetStartedMessage() {        
        String rootPath = dynamicImageModel.getRootPath();
        String imageServiceUrl = dynamicImageModel.getImageServiceURL();
        String dynamicImage = dynamicImageModel.getAltText();
        String finalPath = dynamicImageModel.getFinalPath();
        Assert.assertEquals("alt text","Image1 alt", dynamicImage);
        assertNotNull("root path is not null",rootPath);
        assertNotNull("image service URL is not null",imageServiceUrl);
        Assert.assertEquals("final path","/tetrapak/p2", finalPath);
        Assert.assertEquals("root path",rootPath, "/tetrapak");
        Assert.assertEquals("image service URL",imageServiceUrl, "http://s7g10.scene7.com/is/image");
        String[] dynamicMediaConfMap = dynamicImageModel.getDynamicMediaConfiguration();
        assertTrue("configuration map should not be empty",dynamicMediaConfMap.toString().length() > 0);
        Assert.assertEquals("default image URL","/content/dam/customerhub/cow-blue-background.png", dynamicImageModel.getDefaultImageUrl());
        Assert.assertEquals("default image URL","", dynamicImageModel.getDesktopLargeUrl());
        Assert.assertEquals("default image URL","http://s7g10.scene7.com/is/image/tetrapak/p2?wid=500&hei=600", dynamicImageModel.getDesktopUrl());
        Assert.assertEquals("default image URL","http://s7g10.scene7.com/is/image/tetrapak/p2?wid=100&hei=150&cropn=0.30115830115830117,0.10382513661202186,0.4301158301158301,0.7577413479052824", dynamicImageModel.getMobileLandscapeUrl());
        Assert.assertEquals("default image URL","http://s7g10.scene7.com/is/image/tetrapak/p2?wid=250&hei=100&cropn=0.30115830115830117,0.10382513661202186,0.4301158301158301,0.7577413479052824", dynamicImageModel.getMobilePortraitUrl());
        Assert.assertEquals("default image URL","/content/dam/customerhub/p2.png", dynamicImageModel.getImagePath());

    }
    
}
