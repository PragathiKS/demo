package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nitin Kumar
 */
public class APIGEEServiceImplTest {

    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String SERVLET_RESOURCE_PATH = "";

    private APIGEEServiceImpl apigeeService = new APIGEEServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH);

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("apigeeServiceUrl", "https://api-mig.tetrapak.com");
        _config.put("apigeeClientID", "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ");
        _config.put("apigeeClientSecret", "jX38HGX7Ze4j6vvZ");
        _config.put("apiMappings", "token-generator:bin/customerhub/token-generator");
        aemContext.registerInjectActivateService(apigeeService, _config);
    }

    @Test
    public void testAPIGEEService() {
        Assert.assertEquals("API GEE url", "https://api-mig.tetrapak.com", apigeeService.getApigeeServiceUrl());
        Assert.assertEquals("API GEE id", "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ", apigeeService.getApigeeClientID());
        Assert.assertEquals("API GEE client secret", "jX38HGX7Ze4j6vvZ", apigeeService.getApigeeClientSecret());
        Assert.assertEquals("API GEE mapping", "token-generator:bin/customerhub/token-generator", apigeeService.getApiMappings()[0]);
    }
}
