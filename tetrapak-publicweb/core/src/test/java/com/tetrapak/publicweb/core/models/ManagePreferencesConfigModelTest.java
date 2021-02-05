package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * @author ojaswarn
 * The Class ManagePreferencesConfigModelTest.
 */
public class ManagePreferencesConfigModelTest {
	
	/** The context. */
	@Rule
	public AemContext context = new AemContext();
	
	/** The Constant TEST_CONTENT. */
	private static final String TEST_CONTENT = "/managePreference/test-content.json";
	
	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/lang-masters/en";
	
	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = TEST_CONTENT_ROOT+"/jcr:content/root/responsivegrid/managepreferenceform";
	
	/** The model class. */
	Class<ManagePreferencesConfigModel> modelClass = ManagePreferencesConfigModel.class;

	/** The model. */
	private ManagePreferencesConfigModel model;
	
	/** The resource. */
	private Resource resource;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp () {
		context.load().json(TEST_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);
		resource = context.currentResource(RESOURCE_PATH);
		model = resource.adaptTo(ManagePreferencesConfigModel.class);
	}

	/**
	 * Test model not null.
	 */
	@Test
	public void testModelNotNull() {
		assertNotNull("Model Not null",model);
	}

}
