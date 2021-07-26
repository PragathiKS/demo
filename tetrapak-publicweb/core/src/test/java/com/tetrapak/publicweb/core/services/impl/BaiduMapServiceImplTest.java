package com.tetrapak.publicweb.core.services.impl;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BaiduMapServiceImplTest {
    private BaiduMapServiceImpl baiduMapServiceImpl = new BaiduMapServiceImpl();

    @Rule
    public final AemContext aemContext = new AemContext();

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("baiduMapKey", "abcd1234utrx");
        aemContext.registerInjectActivateService(baiduMapServiceImpl, _config);
    }

    @Test
    public void testUrlService() {
        Assert.assertEquals("Baidu map key", "abcd1234utrx", baiduMapServiceImpl.getBaiduMapKey());
    }
}
