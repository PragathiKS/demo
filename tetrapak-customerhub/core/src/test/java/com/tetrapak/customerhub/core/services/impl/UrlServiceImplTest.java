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
public class UrlServiceImplTest {

    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String SERVLET_RESOURCE_PATH = "";

    private UrlServiceImpl urlService = new UrlServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
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
