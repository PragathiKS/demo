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
public class SiteImproveScriptServiceImplTest {

    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String SERVLET_RESOURCE_PATH = "";

    private SiteImproveScriptServiceImpl improveScriptService = new SiteImproveScriptServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH);

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("siteImproveScriptUrl", "//se1.siteimprove.com/js/siteanalyze_72177.js");
        aemContext.registerInjectActivateService(improveScriptService, _config);
    }

    @Test
    public void testSiteImproveService() {
        Assert.assertEquals("site improve URL", "//se1.siteimprove.com/js/siteanalyze_72177.js", improveScriptService.getSiteImproveScriptUrl());
    }
}
