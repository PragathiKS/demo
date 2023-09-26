package com.tetrapak.supplierportal.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

public class UrlServiceImplTest {
	
	  private static final String SERVLET_RESOURCE_JSON = "allContent.json";
	    private static final String SERVLET_RESOURCE_PATH = "";

	    private UrlServiceImpl urlService = new UrlServiceImpl();

	    @Rule
	    public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
	            SERVLET_RESOURCE_PATH);

	    @Before
	    public void setup() {
	        Map<String, Object> _config = new HashMap<>();
	        _config.put("fontsUrl", "/tmp/resources/fonts/");
	        _config.put("imagesUrl", "/tmp/resources/images/");
	        aemContext.registerInjectActivateService(urlService, _config);
	    }

	    @Test
	    public void testUrlService() {
	        Assert.assertEquals("fonts URL", "/tmp/resources/fonts/", urlService.getFontsUrl());
	        Assert.assertEquals("images URL", "/tmp/resources/images/", urlService.getImagesUrl());
	    }

}
