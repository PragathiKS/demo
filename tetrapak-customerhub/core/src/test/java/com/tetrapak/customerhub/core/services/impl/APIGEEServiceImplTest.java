package com.tetrapak.customerhub.core.services.impl;

import com.microsoft.azure.storage.table.TableOperation;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.services.UserPreferenceService;
import com.tetrapak.customerhub.core.servlets.SaveOnboardingStatusServlet;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Nitin Kumar
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TableOperation.class, APIGEEServiceImpl.class})
public class APIGEEServiceImplTest {

    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String SERVLET_RESOURCE_PATH = "";

    APIGEEServiceImpl apigeeService = new APIGEEServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("apigeeServiceUrl", "https://api-mig.tetrapak.com");
        _config.put("apigeeClientID", "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ");
        _config.put("apigeeClientSecret", "jX38HGX7Ze4j6vvZ");
        _config.put("apiMappings", "token-generator:bin/customerhub/token-generator");
        apigeeService = aemContext.registerInjectActivateService(apigeeService, _config);
    }

    @Test
    public void testAPIGEEService(){
        Assert.assertEquals("url", "https://api-mig.tetrapak.com", apigeeService.getApigeeServiceUrl());
        Assert.assertEquals("id", "KHEnJskMGGogWrJAD3OyUI3VwerCLSDQ", apigeeService.getApigeeClientID());
        Assert.assertEquals("secret", "jX38HGX7Ze4j6vvZ", apigeeService.getApigeeClientSecret());
        Assert.assertEquals("mapping", "token-generator:bin/customerhub/token-generator", apigeeService.getApiMappings()[0]);
    }

    private <T> List<GenericServiceType<T>> getMultipleMockedService() {
        GenericServiceType<UserPreferenceService> userPreferenceGenericServiceType = new GenericServiceType<>();
        userPreferenceGenericServiceType.setClazzType(UserPreferenceService.class);
        userPreferenceGenericServiceType.set(new UserPreferenceServiceImpl());

        GenericServiceType<SaveOnboardingStatusServlet> saveOnboardingStatusServletGenericServiceType = new GenericServiceType<>();
        saveOnboardingStatusServletGenericServiceType.setClazzType(SaveOnboardingStatusServlet.class);
        saveOnboardingStatusServletGenericServiceType.set(new SaveOnboardingStatusServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) userPreferenceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) saveOnboardingStatusServletGenericServiceType);
        return serviceTypes;
    }
}
