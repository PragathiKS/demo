package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.day.cq.wcm.api.PageManager;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class TeaserModelTest.
 */
public class TeaserModelTest {

	/** The context. */
	@Rule
	public AemContext context = new AemContext();

	/** The context. */
	public AemContext context2 = new AemContext();

	/** The page manager. */
	@Mock
	private PageManager pageManager;

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/teaser/test-content.json";

	/** The Constant ASSET_CONTENT. */
	private static final String PAGE_CONTENT = "/teaser/test-content-three.json";

	/** The Constant PAGE_CONTENT_ROOT. */
	private static final String PAGE_CONTENT_ROOT = "/content/tetrapak/public-web/global/en/products";

	/** The Constant RESOURCE_CONTENT_TWO. */
	private static final String RESOURCE_CONTENT_TWO = "/teaser/test-content-two.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant TEST_CONTENT_ROOT_TWO. */
	private static final String TEST_CONTENT_ROOT_TWO = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/teaser";

	/** The Constant RESOURCE_TWO. */
	private static final String RESOURCE_TWO = TEST_CONTENT_ROOT_TWO + "/jcr:content/teaser";

	/** The model. */
	private TeaserModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

		Class<TeaserModel> modelClass = TeaserModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);
		context2.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT_TWO);
		context2.load().json(PAGE_CONTENT, PAGE_CONTENT_ROOT);
		context2.addModelsForClasses(modelClass);
		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the Teaser model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getContentType", "getResource", "getPwButtonTheme", "getTeaserList" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}

	/**
	 * Simple test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleTest() throws Exception {

		resource = context2.currentResource(RESOURCE_TWO);
		model = resource.adaptTo(TeaserModel.class);
		assertNotNull(model.getTeaserList());
	}

}
