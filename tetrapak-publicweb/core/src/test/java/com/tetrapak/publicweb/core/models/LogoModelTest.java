package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class LogoModelTest {
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/logo/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en/404";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/header/logo";

	/** The model. */
	private LogoModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @param context the new up
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

		Class<LogoModel> modelClass = LogoModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the ArticleContainer model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getImagePath", "getImageAltI18n", "getImageLink", "getLinkType", "getCType",
				"getResource" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}
}
