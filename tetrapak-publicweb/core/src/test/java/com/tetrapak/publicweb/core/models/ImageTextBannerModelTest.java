package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class ImageTextBannerModelTest {

	@Rule
	public AemContext context = new AemContext();

	/** The Constant RESOURCE_CONTENT. */
	private static final String RESOURCE_CONTENT = "/imagetextbanner/test-content.json";

	/** The Constant TEST_CONTENT_ROOT. */
	private static final String TEST_CONTENT_ROOT = "/content/publicweb/en";

	/** The Constant RESOURCE. */
	private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content/imagetextbanner";

	/** The model. */
	private ImageTextBannerModel model;

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

		Class<ImageTextBannerModel> modelClass = ImageTextBannerModel.class;
		// load the resources for each object
		context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
		context.addModelsForClasses(modelClass);

		resource = context.currentResource(RESOURCE);
		model = resource.adaptTo(modelClass);
	}

	/**
	 * Test model, resource and all getters of the ImageTextBanner model.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void simpleLoadAndGettersTest() throws Exception {
		String[] methods = new String[] { "getBannerSubtitleI18n", "getTitleI18n", "getDesktopImage",
				"getDesktopImageAltI18n", "getMobileImage", "getMobileCroppingOption", "getBannerDescriptionI18n",
				"getContentAlignment", "getAnalyticsLinkSection", "getContrastLayer", "getBannerCtaTextI18n",
				"getBannerCtaPath", "getLinkType", "getTargetBlank", "getBannerCtaTooltipI18n", "getIsHeaderBanner",
				"getTargetSoftConversion", "getSoftConversionTitle", "getSoftConversionDescription",
				"getSoftConversionHeadline", "getSoftConversionLastStep", "getSoftConversionDocPath" };
		Util.testLoadAndGetters(methods, model, resource);
	}
}
