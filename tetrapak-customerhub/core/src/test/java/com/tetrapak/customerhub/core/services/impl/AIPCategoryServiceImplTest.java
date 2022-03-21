package com.tetrapak.customerhub.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;

import io.wcm.testing.mock.aem.junit.AemContext;

/**
 * The Class AIPCategoryServiceImplTest.
 */
public class AIPCategoryServiceImplTest {

    /** The Constant SERVLET_RESOURCE_JSON. */
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    /** The Constant SERVLET_RESOURCE_PATH. */
    private static final String SERVLET_RESOURCE_PATH = "";

    /** The aip category service. */
    private AIPCategoryServiceImpl aipCategoryService = new AIPCategoryServiceImpl();

    /** The aem context. */
    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH);

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("aipAutomationTrainingsId", "4466");
        _config.put("aipEngineeringLicensesId", "4820");
        _config.put("aipSiteLicensesId", "4821");
        aemContext.registerInjectActivateService(aipCategoryService, _config);
    }

    /**
     * Test AIP category service.
     */
    @Test
    public void testAIPCategoryService() {
        Assert.assertEquals("AIP Automation Trainings Category ID", "4466",
                aipCategoryService.getAutomationTrainingsId());
        Assert.assertEquals("AIP Engineering Licenses Category ID", "4820",
                aipCategoryService.getEngineeringLicensesId());
        Assert.assertEquals("AIP Site Licenses Category ID", "4821", aipCategoryService.getSiteLicensesId());
    }
}
