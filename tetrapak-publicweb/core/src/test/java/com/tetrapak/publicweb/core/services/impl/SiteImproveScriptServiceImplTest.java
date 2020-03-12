package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class SiteImproveScriptServiceImplTest.
 */
public class SiteImproveScriptServiceImplTest {

	/** The script service impl. */
	private SiteImproveScriptServiceImpl scriptServiceImpl = new SiteImproveScriptServiceImpl();

	/** The aem context. */
	@Rule
	public final AemContext aemContext = new AemContext();

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		Map<String, Object> _config = new HashMap<>();
		_config.put("siteImproveScriptUrl", "//se1.siteimprove.com/js/siteanalyze_72177.js");
		aemContext.registerInjectActivateService(scriptServiceImpl, _config);
	}

	/**
	 * Test url service.
	 */
	@Test
	public void testUrlService() {
		Assert.assertEquals("Image Service Url", "//se1.siteimprove.com/js/siteanalyze_72177.js",
				scriptServiceImpl.getSiteImproveScriptUrl());
	}
}
