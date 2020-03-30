package com.tetrapak.publicweb.core.models;


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
	private static final String CONTENT_PATH = "/content/tetrapak/publicweb/lang-masters/en";
	private static final String HOME_PAGE = "/content/tetrapak/publicweb/lang-masters/en/home";
	private static final String RESOURCE_JSON = "/cookieConsent/test-content.json";

	@Rule
	public AemContext aemContext = new AemContext();

	/**
	 * Setup method for the class.
	 */
	@Before
	public void setup() {
		aemContext.load().json(RESOURCE_JSON, CONTENT_PATH);
		aemContext.currentResource(CONTENT_PATH);
		aemContext.request().setPathInfo(HOME_PAGE);
		cookieConsentModel = aemContext.request().adaptTo(CookieConsentModel.class);
	}

	/**
	 * Test method to validate the methods of CookieConsent model class.
	 */
	@Test
	public void testMessage() {
		//Assert.assertEquals("enabled",true, cookieConsentModel.isCookieConsentDisabled());
		//Assert.assertEquals("cookie consent text","Cookie consent text with RTE", cookieConsentModel.getCookieConsentText());
		//Assert.assertEquals("button text","Ok! I understand.", cookieConsentModel.getCookieConsentButtonText());
		//Assert.assertEquals("locale","en", cookieConsentModel.getLocale());
	}
}
