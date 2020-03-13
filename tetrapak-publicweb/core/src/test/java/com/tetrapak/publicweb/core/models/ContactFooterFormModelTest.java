package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class ContactFooterFormModelTest.
 */
public class ContactFooterFormModelTest {

	/** The context. */
	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/contactFooterForm/test-content.json";

	/** The Constant RESOURCE_CONTENT_TWO. */
	private static final String RESOURCE_CONTENT_TWO = "/articlecontainer/test-content-two.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant TEST_CONTENT_ROOT_TWO. */
	private static final String TEST_CONTENT_ROOT_TWO = "/etc/acs-commons/lists/countries";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/contactFooterForm";

	/** The model. */
	private ContactFooterFormModel model;

	/** The resource. */
	private Resource resource;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		Class<ContactFooterFormModel> modelClass = ContactFooterFormModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT_TWO);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the ContactFooterForm model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getTitleI18n", "getDescription", "getImage", "getImageAltI18n",
				"getHelpText", "getPrivacyPolicyText", "getThankYouHeadline", "getThankYouMessage", "getLinkTextI18n",
				"getLinkPath", "getFirstNameLabel", "getLastNameLabel", "getPhoneNumberLabel", "getEmailAddressLabel",
				"getPositionLabel", "getCompanyLabel", "getContactUsLabel", "getMessageLabel", "getPreviousButtonLabel",
				"getNextButtonLabel", "getSubmitButtonLabel", "getHideContactFooterForm" };
		UtilTest.testLoadAndGetters(methods, model, resource);
	}
}
