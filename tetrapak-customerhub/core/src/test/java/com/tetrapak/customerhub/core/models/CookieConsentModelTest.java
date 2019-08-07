package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for CookieConsentModel class.
 * 
 * @author tustusha
 */
public class CookieConsentModelTest {

	private CookieConsentModel cookieConsentModel;
	private static final String CONTENT_PATH = "/content/tetrapak/customerhub/content-components/en/jcr:content";
	private static final String RESOURCE_JSON = "cookieconsent.json";

	/**
	 * Setting the context for the class.
	 */
	@Rule
	public final AemContext aemContext = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,
			"/content/tetrapak/customerhub");

	/**
	 * Setup method for the class.
	 */
	@Before
	public void setup() {
		aemContext.currentResource(CONTENT_PATH);
		cookieConsentModel = aemContext.request().adaptTo(CookieConsentModel.class);
	}

	/**
	 * Test method to validate the methods of CookieConsent model class.
	 */
	@Test
	public void testMessage() {
		Assert.assertEquals(true, cookieConsentModel.isCookieConsentDisabled());
		Assert.assertEquals("Cookie consent text with RTE", cookieConsentModel.getCookieConsentText());
		Assert.assertEquals("Ok! I understand.", cookieConsentModel.getCookieConsentButtonText());
		Assert.assertEquals("en", cookieConsentModel.getLocale());
	}
}
