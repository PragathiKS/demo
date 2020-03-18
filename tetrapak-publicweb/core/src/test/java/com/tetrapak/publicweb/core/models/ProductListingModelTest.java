package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class ProductListingModelTest {

	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/productlisting/test-content.json";

	/** The Constant TAG_CONTENT. */
	private static final String TAG_CONTENT = "/searchResults/test-content1.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/productListing";
	/** The Constant TEST_CONTENT_ROOT_1. */
	private static final String TEST_CONTENT_ROOT_1 = "/content/cq:tags/we-retail";

	/** The model. */
	private ProductListingModel model;

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

		Class<ProductListingModel> modelClass = ProductListingModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.load().json(TAG_CONTENT, TEST_CONTENT_ROOT_1);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the ProductListing model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getTitleI18n", "getProductRootPath", "getTabLinks",
				"getFirstTabLinkTextI18n", "getPwPadding", "getPwTheme", "getTabs", "getTabLinks" };
		Util.testLoadAndGetters(methods, model, resource);
	}
}
