package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class ArticleContainerModelTest.
 */
public class ArticleContainerModelTest {

	/** The context. */
	@Rule
	public AemContext context = new AemContext();

	/** The context 2. */
	@Rule
	public AemContext context2 = new AemContext();

	/** The context 3. */
	@Rule
	public AemContext context3 = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/articlecontainer/test-content.json";

	/** The Constant RESOURCE_CONTENT_TWO. */
	private static final String RESOURCE_CONTENT_TWO = "/articlecontainer/test-content-two.json";

	/** The Constant RESOURCE_CONTENT_THREE. */
	private static final String RESOURCE_CONTENT_THREE = "/articlecontainer/test-content-three.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/articlecontainer";

	/** The model. */
	private ArticleContainerModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

		Class<ArticleContainerModel> modelClass = ArticleContainerModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context2.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT);
		context3.load().json(RESOURCE_CONTENT_THREE, TEST_CONTENT_ROOT);
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
		String[] methods = new String[] { "getTotalColumns", "getTitleI18n", "getTitleAlignment", "getPwTheme" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}

	/**
	 * Simple test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleTest() throws Exception {

		resource = context2.currentResource(RESOURCE);
		model = resource.adaptTo(ArticleContainerModel.class);
		assertNotNull(model.getTotalColumns());
	}

	/**
	 * Simple test 2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleTest2() throws Exception {

		resource = context3.currentResource(RESOURCE);
		model = resource.adaptTo(ArticleContainerModel.class);
		assertNotNull(model.getTotalColumns());
	}
}
