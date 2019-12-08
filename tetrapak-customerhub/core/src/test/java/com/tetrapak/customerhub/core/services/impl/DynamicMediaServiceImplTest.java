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
@RunWith(PowerMockRunner.class)
@PrepareForTest({DynamicMediaServiceImpl.class})
public class DynamicMediaServiceImplTest {

    private static final String SERVLET_RESOURCE_JSON = "allContent.json";
    private static final String SERVLET_RESOURCE_PATH = "";

    private DynamicMediaServiceImpl dynamicMediaService = new DynamicMediaServiceImpl();

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContextWithJcrMock(SERVLET_RESOURCE_JSON,
            SERVLET_RESOURCE_PATH);

    @Before
    public void setup() {
        Map<String, Object> _config = new HashMap<>();
        _config.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        _config.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        _config.put("dynamicMediaConfMap", "[getstarted-desktop=330\\,143,getstarted-mobileL=1024]");
        _config.put("rootPath", "/tetrapak");
        aemContext.registerInjectActivateService(dynamicMediaService, _config);
    }

    @Test
    public void testDynamicMediaService() {
        Assert.assertEquals("image service URL", "https://s7g10.scene7.com/is/image", dynamicMediaService.getImageServiceUrl());
        Assert.assertEquals("root path", "/tetrapak", dynamicMediaService.getRootPath());
        Assert.assertEquals("video service URL", "https://s7g10.scene7.com/is/content", dynamicMediaService.getVideoServiceUrl());
        Assert.assertEquals("conf map", "[getstarted-desktop=330\\,143,getstarted-mobileL=1024]", dynamicMediaService.getDynamicMediaConfMap()[0]);
    }
}
