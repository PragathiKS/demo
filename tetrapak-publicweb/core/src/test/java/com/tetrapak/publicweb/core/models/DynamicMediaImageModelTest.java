package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.mock.MockDynamicMediaServiceImpl;
import com.tetrapak.publicweb.core.services.DynamicMediaService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DynamicMediaImageModelTest {
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/carousel/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/carousel";

	/** The model. */
	private DynamicMediaImageModel model;

	/** The resource. */
	private Resource resource;

	private DynamicMediaService dynamicMediaService;

	/**
	 * Sets the up.
	 *
	 * @param context the new up
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		dynamicMediaService = new MockDynamicMediaServiceImpl();
		context.registerService(DynamicMediaService.class, dynamicMediaService);
		context.request().setAttribute("imagePath", "/content/dam/tetrapak/p2.png");
		context.request().setAttribute("mobileImagePath", "/content/dam/tetrapak/p2.png");
		context.request().setAttribute("mobileCroppingOption", "right");
		Class<DynamicMediaImageModel> modelClass = DynamicMediaImageModel.class;
		MockSlingHttpServletRequest request = context.request();
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);
		context.request().setPathInfo(TEST_CONTENT_ROOT);
		request.setResource(context.resourceResolver().getResource(RESOURCE));
		resource = context.currentResource(RESOURCE);
		model = request.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the ArticleContainer model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getDynamicMediaConfiguration", "getMobilePortraitUrl",
				"getMobileLandscapeUrl", "getTabletPortraitUrl", "getTabletLandscapeUrl", "getDesktopUrl" };
		Util.testLoadAndGetters(methods, model, resource);
	}
	
}
