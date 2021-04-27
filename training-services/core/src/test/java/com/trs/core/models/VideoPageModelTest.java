package com.trs.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trs.core.services.TrsConfigurationService;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class VideoPageModelTest {
	
	@Mock
	TrsConfigurationService trsConfig;

	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private VideoPageModel videoPageModel;
	
	private Resource resource;
	
	Class<VideoPageModel> modelClass = VideoPageModel.class;
	
	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		TestUtils.setupTrsConfiguration(context, trsConfig);
        // load the resources for each object
        context.load().json("/com/trs/core/services/impl/videoPage.json", "/content/trs/en/test/video2");
        context.addModelsForClasses(modelClass);
        resource = context.currentResource("/content/trs/en/test/video2/jcr:content/root/responsivegrid/video");
        assert resource != null;
        videoPageModel = resource.adaptTo(modelClass);
	}

	@Test
	final void testGetS7assetFile() {
		assertEquals("tetrapakdev/MLV00298 - A3 Speed - Preparing Packaging Material Splice", videoPageModel.getS7assetFile(),"Unexpected value");
	}

	@Test
	final void testSetS7assetFile() {
		videoPageModel.setS7assetFile("tetrapakdev/MLV00298");
		assertEquals("tetrapakdev/MLV00298", videoPageModel.getS7assetFile(),"Unexpected value");
	}

	@Test
	final void testGetDmDomain() {
		assertEquals("https://s7g10.scene7.com", videoPageModel.getDmDomain(),"Unexpected value");
	}

	@Test
	final void testSetDmDomain() {
		videoPageModel.setDmDomain("https://s7g11.scene7.com");
		assertEquals("https://s7g11.scene7.com", videoPageModel.getDmDomain(),"Unexpected value");
	}

}
