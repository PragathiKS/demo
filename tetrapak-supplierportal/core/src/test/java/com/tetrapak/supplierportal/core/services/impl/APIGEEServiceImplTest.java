package com.tetrapak.supplierportal.core.services.impl;

import static com.tetrapak.supplierportal.core.constants.SupplierPortalConstants.STATUS_CODE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.gson.JsonObject;
import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * 
 * @author Sunil Kumar Yadav(sunmanak)
 *
 */
public class APIGEEServiceImplTest {

	private static final String APIGEE_SERVICE_URL = "https://api-mig.tetrapak.com";
	private static final String APIGEE_MAPPINGS = "token-generator:bin/customerhub/token-generator";

	private static final String SERVLET_RESOURCE_JSON = "allContent.json";
	private static final String SERVLET_RESOURCE_PATH = "";

	private APIGEEServiceImpl apigeeService = new APIGEEServiceImpl();

	@Rule
	public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
			SERVLET_RESOURCE_PATH);

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		Map<String, Object> _config = new HashMap<>();
		_config.put("apigeeServiceUrl", APIGEE_SERVICE_URL);
		_config.put("apigeeClientID", "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ");
		_config.put("apigeeClientSecret", "jX38HGX7Ze4j6vvZ");
		_config.put("apiMappings", APIGEE_MAPPINGS);
		aemContext.registerInjectActivateService(apigeeService, _config);
	}

	@Test
	public void testAPIGEEService() {
		Assert.assertEquals("API GEE url", APIGEE_SERVICE_URL, apigeeService.getApigeeServiceUrl());
		Assert.assertEquals("API GEE mapping", APIGEE_MAPPINGS, apigeeService.getApiMappings()[0]);
	}

	@Test
	public void testRetrieveAPIGEEToken() throws IOException {
		JsonObject jsonResponse = apigeeService.retrieveAPIGEEToken("test");
		Assert.assertNotNull(jsonResponse);
		Assert.assertEquals(200, jsonResponse.get(STATUS_CODE).getAsInt());
	}

}
