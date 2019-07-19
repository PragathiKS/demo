package com.tetrapak.customerhub.core.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.beans.GetStartedBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * Test class for GetStartedModel class.
 */
public class GetStartedModelTest {

	private GetStartedModel getStartedModel = null;
	private static final String GET_STARTED_CONTENT_ROOT = "/content/tetrapak/customerhub/global/en/"
			+ "about-my-tetra-pak/jcr:content/par/getstarted";
	private static final String GET_STARTED_RESOURCE_JSON = "getstarted.json";

	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(GET_STARTED_RESOURCE_JSON,
			GET_STARTED_CONTENT_ROOT);

	/**
	 * Setup method for the class.
	 */
	@Before
	public void setup() {
		Resource getStartedResource = aemContext.currentResource(GET_STARTED_CONTENT_ROOT);
		getStartedModel = getStartedResource.adaptTo(GetStartedModel.class);
	}

	/**
	 * Test method to validate the methods of GetStarted model class.
	 */
	@Test
	public void testGetStartedMessage() {
		String heading = getStartedModel.getHeadingI18n();
		assertNotNull("Heading should not be null", heading);
		assertTrue("Heading is not empty", heading.length() > 0);
		Assert.assertEquals("getStarted", getStartedModel.getHeadingI18n());
		Assert.assertEquals("abc", getStartedModel.getClassName());
		assertFalse(getStartedModel.getGetStartedList().isEmpty());
		List<GetStartedBean> list = getStartedModel.getGetStartedList();
		GetStartedBean bean = list.get(0);
		Assert.assertEquals("One place for your business", bean.getTitleI18n());
		Assert.assertEquals(
				"Processing solutions and equipment for dairy, cheese, ice cream, beverages and prepared food.",
				bean.getDescriptionI18n());
		Assert.assertEquals("Image1 alt", bean.getImageAltI18n());
		Assert.assertEquals("/content/dam/customerhub/p2.PNG", bean.getImagePath());
	}

}
