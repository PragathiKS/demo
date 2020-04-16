package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DynamicImageModelTest {
	@Rule
	public AemContext aemContext = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/carousel/test-content.json";
	
	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_IMAGE = "/assets/logo_tetra_pak_white.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";
	
	/** The Constant DAM_IMAGE. */
	private static final String DAM_IMAGE = "/content/dam/tetrapak/p2.png";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/carousel";
	
	/** THE DYNAMIC MEDIA CONF. */
	String[] dynamicMediaConfMap = {
	    "getstarted-desktop=1440\\,300,getstarted-mobileL=414\\,259\\,0.333\\,0\\,0.333\\,1,getstarted-mobileP=414\\,259\\,0.333\\,0\\,0.333\\,1" };

	/** The model. */
	private DynamicImageModel dynamicImageModel;


	private DynamicMediaService dynamicMediaService;

	@Before
	    public void setup() {
	        //aemContext.load().json("/" + dam_JSON, "/content/dam/customerhub/p2.png");
	      	aemContext.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
	      	aemContext.load().json(RESOURCE_IMAGE, DAM_IMAGE);
	        dynamicMediaService = new DynamicMediaServiceImpl();
	        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
	        configuraionServiceConfig.put("rootPath", "/tetrapak");
	        configuraionServiceConfig.put("dynamicMediaConfMap", dynamicMediaConfMap);
	        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
	        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
	        aemContext.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);
	        aemContext.registerService(DynamicMediaService.class, dynamicMediaService);        
	        aemContext.request().setPathInfo(RESOURCE);
	        aemContext.request().setAttribute("imagePath", "/content/dam/tetrapak/p2.png");
	        aemContext.request().setAttribute("altText", "Image1 alt");
	        aemContext.request().setAttribute("imageCrop", "390,57,947,473");
	        aemContext.request().setAttribute("dwidth", "500");
	        aemContext.request().setAttribute("dheight", "600");
	        aemContext.request().setAttribute("mwidthl", "100");
	        aemContext.request().setAttribute("mheightl", "150");
	        aemContext.request().setAttribute("mwidthp", "250");
	        aemContext.request().setAttribute("mheightp", "100");
	        aemContext.request().setResource(aemContext.resourceResolver().getResource(RESOURCE));
	        dynamicImageModel = aemContext.request().adaptTo(DynamicImageModel.class);
	    }
	    
	    @Test
	    public void testGetStartedMessage() {        
	        String rootPath = dynamicImageModel.getRootPath();
	        String imageServiceUrl = dynamicImageModel.getImageServiceURL();
	        String videoServiceUrl = dynamicImageModel.getVideoServiceUrl();
	        String dynamicImage = dynamicImageModel.getAltText();
	        String finalPath = dynamicImageModel.getFinalPath();
	        Assert.assertEquals("alt text","Image1 alt", dynamicImage);
	        assertNotNull("root path is not null",rootPath);
	        assertNotNull("image service URL is not null",imageServiceUrl);
	        Assert.assertEquals("final path","/tetrapak/p2", finalPath);
	        Assert.assertEquals("root path",rootPath, "/tetrapak");
	        Assert.assertEquals("image service URL",imageServiceUrl, "https://s7g10.scene7.com/is/image");
	        Assert.assertEquals("image service URL",videoServiceUrl, "https://s7g10.scene7.com/is/content");
	        String[] dynamicMediaConfMap = dynamicImageModel.getDynamicMediaConfiguration();
	        assertTrue("configuration map should not be empty",dynamicMediaConfMap.toString().length() > 0);
	        Assert.assertEquals("default image URL","/content/dam/customerhub/cow-blue-background.png", dynamicImageModel.getDefaultImageUrl());
	        Assert.assertEquals("default image URL","https://s7g10.scene7.com/is/image/tetrapak/p2?scl=1", dynamicImageModel.getDesktopLargeUrl());
	        Assert.assertEquals("default image URL","https://s7g10.scene7.com/is/image/tetrapak/p2?wid=500&hei=600&fmt=png-alpha&resMode=bisharp", dynamicImageModel.getDesktopUrl());
	        Assert.assertEquals("default image URL","https://s7g10.scene7.com/is/image/tetrapak/p2?wid=100&hei=150&cropn=2.4375,2.28,3.48125,16.64&fmt=png-alpha&resMode=bisharp", dynamicImageModel.getMobileLandscapeUrl());
	        Assert.assertEquals("default image URL","https://s7g10.scene7.com/is/image/tetrapak/p2?wid=250&hei=100&cropn=2.4375,2.28,3.48125,16.64&fmt=png-alpha&resMode=bisharp", dynamicImageModel.getMobilePortraitUrl());
	        Assert.assertEquals("default image URL","/content/dam/tetrapak/p2.png", dynamicImageModel.getImagePath());

	    }
	
}
