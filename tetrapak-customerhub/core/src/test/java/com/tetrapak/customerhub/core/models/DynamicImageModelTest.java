package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.customerhub.core.services.DynamicMediaService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicImageModelTest {

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext
            .getAemContextWithJcrMock(GET_STARTED_RESOURCE_JSON, COMPONENT_PATH);
    private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";
    private static final String dam_JSON = "dam.json";
    private static final String dam_scene7_JSON = "dam_scene7.json";
    private static final String COMPONENT_PATH = "/content/tetrapak/customerhub/global/en/about-us/jcr:content/par/getstarted";
    private DynamicImageModel dynamicImageModel;
    private DynamicMediaService dynamicMediaService;

    @Before
    public void setup() {
        aemContext.load().json("/" + dam_JSON, "/content/dam/customerhub/p2.test.png");
        aemContext.load().json("/" + dam_scene7_JSON, "/content/dam/customerhub/paperboards_inks/Paper_Inks_Chart.png");
        dynamicMediaService = new MockDynamicMediaServiceImpl();
        aemContext.registerService(DynamicMediaService.class, dynamicMediaService);
        aemContext.currentResource(COMPONENT_PATH);
    }

    @Test
    public void testGetStartedMessage() {
        aemContext.request().setAttribute("imagePath", "/content/dam/customerhub/p2.test.png");
        aemContext.request().setAttribute("altText", "Image1 alt");
        aemContext.request().setAttribute("imageCrop", "390,57,947,473");
        aemContext.request().setAttribute("dwidth", "500");
        aemContext.request().setAttribute("dheight", "600");
        aemContext.request().setAttribute("mwidthl", "100");
        aemContext.request().setAttribute("mheightl", "150");
        aemContext.request().setAttribute("mwidthp", "250");
        aemContext.request().setAttribute("mheightp", "100");
        dynamicImageModel = aemContext.request().adaptTo(DynamicImageModel.class);

        String rootPath = dynamicImageModel.getRootPath();
        String imageServiceUrl = dynamicImageModel.getImageServiceURL();
        String dynamicImage = dynamicImageModel.getAltText();
        String finalPath = dynamicImageModel.getFinalPath();

        assertEquals("alt text", "Image1 alt", dynamicImage);
        assertNotNull("root path is not null", rootPath);
        assertNotNull("image service URL is not null", imageServiceUrl);
        assertEquals("final path", "/tetrapak/p2.test", finalPath);
        assertEquals("root path", "/tetrapak", rootPath);
        assertEquals("image service URL", "http://s7g10.scene7.com/is/image", imageServiceUrl);
        String[] dynamicMediaConfMap = dynamicImageModel.getDynamicMediaConfiguration();
        assertTrue("configuration map should not be empty", dynamicMediaConfMap.toString().length() > 0);
        assertEquals("default image URL", "/content/dam/customerhub/cow-blue-background.png",
                dynamicImageModel.getDefaultImageUrl());
        assertEquals("default image URL", "", dynamicImageModel.getDesktopLargeUrl());
        assertEquals("default image URL", "http://s7g10.scene7.com/is/image/tetrapak/p2.test?wid=500&hei=600",
                dynamicImageModel.getDesktopUrl());
        assertEquals("default image URL",
                "http://s7g10.scene7.com/is/image/tetrapak/p2.test?wid=100&hei=150&cropn=0.3046875,0.12179487179487179,0.43515625,0.8888888888888888",
                dynamicImageModel.getMobileLandscapeUrl());
        assertEquals("default image URL",
                "http://s7g10.scene7.com/is/image/tetrapak/p2.test?wid=250&hei=100&cropn=0.3046875,0.12179487179487179,0.43515625,0.8888888888888888",
                dynamicImageModel.getMobilePortraitUrl());
        assertEquals("default image URL", "/content/dam/customerhub/p2.test.png", dynamicImageModel.getImagePath());
    }

    @Test
    public void testScene7testImage() {
        aemContext.currentResource(COMPONENT_PATH);
        aemContext.request()
                .setAttribute("imagePath", "/content/dam/customerhub/paperboards_inks/Paper_Inks_Chart.png");
        aemContext.request().setAttribute("altText", "Image1 alt");
        aemContext.request().setAttribute("dwidth", "500");
        aemContext.request().setAttribute("dheight", "600");
        dynamicImageModel = aemContext.request().adaptTo(DynamicImageModel.class);

        assertEquals("http://s7g10.scene7.com/is/image/tetrapak/Paper_Inks_Chart-1?wid=500&hei=600",
                dynamicImageModel.getDesktopUrl());
        assertEquals("/tetrapak/Paper_Inks_Chart-1", dynamicImageModel.getFinalPath());
    }

}
