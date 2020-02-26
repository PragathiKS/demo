package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * The Class SearchResultsModelTest.
 */
@ExtendWith(AemContextExtension.class)
public class SearchResultsModelTest {
	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/searchResults/test-content.json";

	/** The Constant TAG_CONTENT. */
	private static final String TAG_CONTENT = "/searchResults/test-content1.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/searchResults";

	/** The Constant TEST_CONTENT_ROOT_1. */
	private static final String TEST_CONTENT_ROOT_1 = "/content/cq:tags/we-retail";

	/** The model. */
	private SearchResultsModel model;

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

		Class<SearchResultsModel> modelClass = SearchResultsModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.load().json(TAG_CONTENT, TEST_CONTENT_ROOT_1);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the SearchResults model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getSearchBoxPlaceholder", "getResultsText", "getNoResultsText",
				"getNoFilterMatches", "getResultsPerPage", "getFirstTabLinkText", "getTabs", "getFilterTitle",
				"getTagsMap" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}
}
