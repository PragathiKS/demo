package com.tetrapak.supplierportal.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
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
		Assert.assertEquals("Purpose of contact", "Purpose of contact", model.getPurposeOfContactLabel());
		Assert.assertEquals("Please select a purpouse", "Please select a purpouse", model.getPurposeErrorMsgLabel());
		Assert.assertEquals("How can we help you?", "How can we help you?", model.getQueryTitle());
		Assert.assertEquals("Describe your query", "Describe your query", model.getQuerySubtitle());
		Assert.assertEquals("error", "error", model.getQueryErrorMsgLabel());
		Assert.assertEquals("error", "error", model.getFileErrorMsg());
		Assert.assertEquals("add query here", "add query here", model.getQueryPlaceholder());
		Assert.assertEquals("Add File", "Add File", model.getDragAndDropButtonLabel());
		Assert.assertEquals("Remove", "Remove", model.getDragAndDropRemoveFileLabel());
		Assert.assertEquals("Personal Detail", "Personal Detail", model.getDetailsTitle());
		Assert.assertEquals("Name", "Name", model.getNameLabel());
		Assert.assertEquals("Email Address", "Email Address", model.getEmailLabel());
		Assert.assertEquals("error", "error", model.getCompanyErrorMsg());
		Assert.assertEquals("Country", "Company", model.getCompanyLabel());
		Assert.assertEquals("Country", "Country", model.getCountryLabel());
		Assert.assertEquals("error", "error", model.getCountryErrorMsg());
		Assert.assertEquals("City", "City", model.getCityLabel());
		Assert.assertEquals("error", "error", model.getCityErrorMsg());
		Assert.assertEquals("Phone", "Phone", model.getPhoneLabel());
		Assert.assertEquals("error", "error", model.getPhoneErrorMsg());
		Assert.assertEquals("Other information", "Other information", model.getOtherTitle());
		Assert.assertEquals("Other Info Desc", "Other Info Desc", model.getOtherDescriptionLabel());
		Assert.assertEquals("Ariba Network ID (optional)", "Ariba Network ID (optional)", model.getAribaNetworkLabel());
		Assert.assertEquals("Ariba account administrator email (optional)",
				"Ariba account administrator email (optional)", model.getAribaEmailLabel());
		Assert.assertEquals("error", "error", model.getAribaEmailErrorMsg());
		Assert.assertEquals("Tetra Pak contant email (optional)", "Tetra Pak contant email (optional)",
				model.getTpEmailLabel());
		Assert.assertEquals("error", "error", model.getTpEmailErrorMsg());
		Assert.assertEquals("Send", "Send", model.getSendButtonLabel());
		Assert.assertEquals("Thank you!", "Thank you!", model.getThankyouTitleLabel());
		Assert.assertEquals("Thankyou SubTitle", "Thankyou SubTitle", model.getThankyouSubtitleLabel());
		Assert.assertEquals("Home", "Home", model.getHomeButtonLabel());
		Assert.assertEquals("On Boardiing Maintanance", "On Boardiing Maintanance", model.getOnboardingMaintanance());
		Assert.assertEquals("subtitle", "subtitle", model.getOnboardingMaintananceSubtitle());
		Assert.assertEquals("Sourcing Contracting", "Sourcing Contracting", model.getSourcingContracting());
		Assert.assertEquals("Support subtitle", "Support subtitle", model.getSourcingContractingSubtitle());
		Assert.assertEquals("Catalogues", "Catalogues", model.getCatalogues());
		Assert.assertEquals("Subtitle catalogues", "Subtitle catalogues", model.getCataloguesSubtitle());

	}
}
