package com.tetrapak.publicweb.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DynamicMediaServiceImplTest {

	private DynamicMediaServiceImpl dynamicMediaServiceImpl = new DynamicMediaServiceImpl();

	@Rule
	public final AemContext aemContext = new AemContext();

	@Before
	public void setup() {
		Map<String, Object> _config = new HashMap<>();
		_config.put("imageServiceUrl", "/tmp/resources/fonts/");
		_config.put("rootPath", "/tmp/resources/images/");
		_config.put("dynamicMediaConfMap", "/tmp/resources/images/");
		aemContext.registerInjectActivateService(dynamicMediaServiceImpl, _config);
	}

	@Test
	public void testUrlService() {
		Assert.assertEquals("Image Service Url", "/tmp/resources/fonts/", dynamicMediaServiceImpl.getImageServiceUrl());
		Assert.assertEquals("Root Path", "/tmp/resources/images/", dynamicMediaServiceImpl.getRootPath());
		Assert.assertEquals("Dynamic Media Conf Map", "/tmp/resources/images/",
				dynamicMediaServiceImpl.getDynamicMediaConfMap()[0]);
	}

}
