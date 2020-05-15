package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class LandingPageModelTest {
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/pageContent/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/";

	/** The model. */
	private LandingPageModel model;

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

		Class<LandingPageModel> modelClass = LandingPageModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the PageContent model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
	    assertEquals("title", model.getTitle());
	    assertEquals("Vanity Description", model.getVanityDescription());
	    assertEquals("Cta Text i18n Key", model.getCtaTexti18nKey());
	    assertEquals(true, model.isOpenInNewWindow());
	    assertEquals(true, model.getShowImage());
	    assertEquals("/content/dam/publicweb/1320x500-test1.png", model.getArticleImagePath());
	    assertEquals("Article Image Alt I18n", model.getArticleImageAltI18n());
	}
}
