package com.trs.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class AssetMetadataHelperModelTest {
	
	public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
	
	private AssetMetadataHelperModel assetMetadataHelperModelTestModel;

	@BeforeEach
	void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
        // load the resources for each object
        context.load().json("/com/trs/core/services/impl/videoFolder.json", "/content/dam/training-services/test");
        context.addModelsForClasses(AssetMetadataHelperModel.class);
        
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(TestUtils.TEST_ASSET_PATH);
        context.request().setQueryString("item="+TestUtils.TEST_ASSET_PATH);
        request.setResource(context.resourceResolver().getResource(TestUtils.TEST_ASSET_PATH));
        Resource resource = context.currentResource(TestUtils.TEST_ASSET_PATH);
        assert resource != null;
        assetMetadataHelperModelTestModel = request.adaptTo(AssetMetadataHelperModel.class);
	}

	@Test
	final void testGetVideoPagePublicURL() {
		assertEquals("http://anytimelearning.tetrapak.com/content/trs/en/test/video2.html", assetMetadataHelperModelTestModel.getVideoPagePublicURL(),"Wrong Public URL for video page");
	}

	@Test
	final void testSetVideoPagePublicURL() {
		assetMetadataHelperModelTestModel.setVideoPagePublicURL("http://anytimelearning.tetrapak.com/content/trs/en/test/video3.html");
		assertEquals("http://anytimelearning.tetrapak.com/content/trs/en/test/video3.html", assetMetadataHelperModelTestModel.getVideoPagePublicURL(),"Wrong Public URL for video page");
	}

	@Test
	final void testGetVideoPageAuthoringURL() {
		assertEquals("/editor.html/content/trs/en/test/video2.html", assetMetadataHelperModelTestModel.getVideoPageAuthoringURL(),"Wrong Authoring URL for video page");
	}

	@Test
	final void testSetVideoPageAuthoringURL() {
		assetMetadataHelperModelTestModel.setVideoPageAuthoringURL("/editor.html/content/trs/en/test/video3.html");
		assertEquals("/editor.html/content/trs/en/test/video3.html", assetMetadataHelperModelTestModel.getVideoPageAuthoringURL(),"Wrong Authoring URL for video page");
	}

}
