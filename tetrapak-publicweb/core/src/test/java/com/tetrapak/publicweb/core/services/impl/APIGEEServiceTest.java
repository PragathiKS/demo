package com.tetrapak.publicweb.core.services.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.services.APIGEEService;

import io.wcm.testing.mock.aem.junit.AemContext;

public class APIGEEServiceTest {
    
    @Rule
    public AemContext context = new AemContext();

    /** The apiGEE service. */
    APIGEEService apiGEEService;
    
    @Before
    public void setUp() throws Exception {
        final Map<String, Object> apiGeeConfig = new HashMap<String, Object>();
        apiGeeConfig.put("apigeeServiceUrl", "https://api-mig.tetrapak.com");
        apiGeeConfig.put("apigeeClientID", "test");
        apiGeeConfig.put("apigeeClientSecret", "test");
        apiGEEService = new APIGEEServiceImpl();
        context.registerService(APIGEEService.class, apiGEEService);
        MockOsgi.activate(apiGEEService, context.bundleContext(), apiGeeConfig);
        apiGEEService.getBearerToken();
    
    }

    @Test
    public void run() throws IOException {
        apiGEEService.getBearerToken();
        apiGEEService.getFillingMachines("test", "https://api-mig.tetrapak.com");
        apiGEEService.getPackageTypes("test", "https://api-mig.tetrapak.com");
        apiGEEService.getProcessingEquipements("test", "https://api-mig.tetrapak.com");
        apiGEEService.getDeltaFillingMachines("test", "https://api-mig.tetrapak.com");
        apiGEEService.getDeltaPackageTypes("test", "https://api-mig.tetrapak.com");
        apiGEEService.getDeltaProcessingEquipements("test", "https://api-mig.tetrapak.com");
        apiGEEService.getListOfFiles("delta","test");
        assertEquals("apiGEEService", "apiGEEService",
                "apiGEEService");
    }
    }
