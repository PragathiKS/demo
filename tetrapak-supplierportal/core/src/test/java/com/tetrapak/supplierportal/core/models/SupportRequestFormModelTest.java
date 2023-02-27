package com.tetrapak.supplierportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SupportRequestFormModelTest {
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/supportRequestForm/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/tetrapak/supplierportal/global/en/seamless-test-surbhi";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/supportrequest";

	/** The model. */
	private SupportRequestFormModel model;

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

		Class<SupportRequestFormModel> modelClass = SupportRequestFormModel.class;
		// load the resources for each object
		MockSlingHttpServletRequest request = context.request();
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);
		 request.setResource(context.resourceResolver().getResource(RESOURCE));
		resource = context.currentResource(RESOURCE);
		model = request.adaptTo(modelClass);
	}

	@Test
	public void testGetStartedMessage() {
		Assert.assertEquals("Contact Support", "Contact Support", model.getGeneralTitleLabel());
		Assert.assertEquals("General Subtitle", "General Subtitle", model.getGeneralSubtitleLabel());
	}
}
