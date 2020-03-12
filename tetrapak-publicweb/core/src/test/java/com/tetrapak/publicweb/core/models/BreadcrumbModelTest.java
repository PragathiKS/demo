package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.day.cq.wcm.api.PageManager;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class BreadcrumbModelTest.
 */
public class BreadcrumbModelTest {

	/** The context. */
	@Rule
	public AemContext context = new AemContext();

	/** The page manager. */
	@Mock
	private PageManager pageManager;

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/breadcrmb/test-content.json";

	/** The Constant HOMEPAGE_CONTENT. */
	private static final String HOMEPAGE_CONTENT = "/breadcrmb/test-content-two.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en/error/404";

	/** The Constant HOMEPAGE_CONTENT_ROOT. */
	private static final String HOMEPAGE_CONTENT_ROOT = "/content/tetrapak/public-web/global";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/breadcrmb";

	/** The model. */
	private BreadcrumbModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {

		Class<BreadcrumbModel> modelClass = BreadcrumbModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.load().json(HOMEPAGE_CONTENT, HOMEPAGE_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);
		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the BreadcrumbModel model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getBreadcrumbHomeLabelI18n", "getBreadcrumbHomePath",
				"getBreadcrumbSubpages" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}

}
