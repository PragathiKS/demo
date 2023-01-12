package com.tetrapak.supplierportal.core.services.impl;

import com.tetrapak.supplierportal.core.mock.SupplierPortalCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CookieDataDomainScriptServiceImplTest {

    /**
     * The Constant SERVLET_RESOURCE_JSON.
     */
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    /**
     * The Constant SERVLET_RESOURCE_PATH.
     */
    private static final String SERVLET_RESOURCE_PATH = "";

    private CookieDataDomainScriptServiceImpl cookieDataDomainScriptService = new CookieDataDomainScriptServiceImpl();

    @Rule public final AemContext aemContext = SupplierPortalCoreAemContext.getAemContextWithJcrMock(
            SERVLET_RESOURCE_JSON, SERVLET_RESOURCE_PATH);

    @Before public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("cookieDomainScriptConfig",
                "[&quot;supplierportal&quot;={&quot;domainScript&quot;:&quot;579e2e0d-08e5-499e-bc40-f5764a925f75-test&quot;}]");
        aemContext.registerInjectActivateService(cookieDataDomainScriptService, _config);
    }

    @Test public void testAIPCategoryService() {
        Assert.assertEquals("Domain script", "[&quot;supplierportal&quot;={&quot;domainScript&quot;:&quot;579e2e0d-08e5-499e-bc40-f5764a925f75-test&quot;}]", cookieDataDomainScriptService.getCookieDomainScriptConfig()[0]);
    }

}
