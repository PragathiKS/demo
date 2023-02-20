package com.tetrapak.publicweb.core.models;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.services.BaiduMapService;
import com.tetrapak.publicweb.core.services.impl.BaiduMapServiceImpl;
import com.tetrapak.publicweb.core.utils.PageUtil;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PageHeadModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/pageContent/test-page-with-country.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/cn";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/zh/home/jcr:content";

    /** The Baidu Map Key. */
    private static final String BAIDU_MAP_KEY = "abcd1234utrx";

    /** The model. */
    private PageHeadModel model;

    /** The BaiduMapService **/
    private BaiduMapService baiduMapService;

    @Mock
    private Page countryPage;

    @Before
    public void setUp() throws Exception {
        Class<PageHeadModel> modelClass = PageHeadModel.class;
        baiduMapService = new BaiduMapServiceImpl();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);
        MockSlingHttpServletRequest request = context.request();
        context.request().setPathInfo(RESOURCE);
        context.registerService(BaiduMapService.class, baiduMapService);
        final Map<String, Object> baiduConfig = new HashMap<>();
        baiduConfig.put("baiduMapKey","i2VwWovOU2OomMlYNPhXmGhMXcESpXVr");
        MockOsgi.activate(Objects.requireNonNull(context.getService(BaiduMapService.class)), context.bundleContext(), baiduConfig);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        context.currentPage(TEST_CONTENT_ROOT+"/zh/home");
        model = request.adaptTo(modelClass);

    }

    /**
     * Test model, resource and all getters of the Page Head model.
     *
     * @throws Exception the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("i2VwWovOU2OomMlYNPhXmGhMXcESpXVr", model.getBaiduMapkey());
        assertEquals("/content/tetrapak/publicweb/cn/zh/home/jcr:content.onetrustcookietoken.json", model.getCookieTokenServletUrl());
        assertEquals("China", model.getPageTitleCountrySuffix());
    }

}
