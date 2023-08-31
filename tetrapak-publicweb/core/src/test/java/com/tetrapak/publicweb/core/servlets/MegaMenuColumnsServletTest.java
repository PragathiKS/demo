package com.tetrapak.publicweb.core.servlets;

import com.tetrapak.publicweb.core.mock.MockHelper;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

public class MegaMenuColumnsServletTest {

    private static final String RESOURCE_CONTENT = "/headerv2/megamenuconfigv2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/experience-fragments/publicweb/en/solutions-mega-menu/master";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig/col1";

    @Rule
    public AemContext context = new AemContext();

    private MegaMenuColumnsServlet megaMenuColumnsServlet = new MegaMenuColumnsServlet();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.registerService(MegaMenuColumnsServlet.class, megaMenuColumnsServlet);
    }

    @Test
    public void testMegaMenuColumnsServlet() throws IOException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("columns", 2);
        parameterMap.put("contentPath", "/content/experience-fragments/publicweb/en/solutions-mega-menu/master/jcr:content/root/responsivegrid/megamenuconfig");
        context.request().setParameterMap(parameterMap);
        megaMenuColumnsServlet = MockHelper.getServlet(context, MegaMenuColumnsServlet.class);
        megaMenuColumnsServlet.doPost(context.request(), context.response());
    }


}
