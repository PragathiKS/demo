package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class TextVideoModelTest {
	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/textvideo/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant TEXTVIDEO_RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/textvideo";

	private TextVideoModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @param context the new up
	 * @throws Exception the exception
	 */
	@BeforeEach
	public void setUp(AemContext context) throws Exception {

		Class<TextVideoModel> modelClass = TextVideoModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the accordion model
	 * 
	 * @throws Exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getTitle", "getDescription", "getLinkTexti18n", "getLinkURL",
				"getTargetBlank", "getVideoSource", "getYoutubeVideoID", "getYoutubeEmbedURL", "getDamVideoPath",
				"getThumbnailPath", "getTextAlignment", "getPwTheme", "getPwButtonTheme",
				"getPwPadding", "getPwDisplay" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}
}
