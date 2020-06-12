package com.tetrapak.publicweb.core.servlets;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Function;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.mock.MockXSSAPI;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SiteSearchServletTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/public-web/lang-masters/en";

    /** The Constant CURRENT_RESOURCE. */
    private static final String CURRENT_RESOURCE = "/content/tetrapak/public-web/lang-masters/en/search/jcr:content/root/responsivegrid/searchresults";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String DAM_CONTENT_ROOT = "/content/dam/publicweb";

    @Mock
    XSSAPI xssAPI;

    @Before
    public void setUp() throws Exception {

        context.load().json("/searchresult/en.json", TEST_CONTENT_ROOT);
        context.load().json("/searchresult/dam.json", DAM_CONTENT_ROOT);
        context.currentResource(CURRENT_RESOURCE);
        context.request().setPathInfo(CURRENT_RESOURCE);

        MockitoAnnotations.initMocks(this);
        when(xssAPI.getValidHref(context.request().getParameter("contentType")))
                .thenReturn("news,media,events,products");
        XSSAPI xssAPI = new MockXSSAPI(context.request().getParameter("contentType"));
        context.registerService(XSSAPI.class, xssAPI);
        
        Map<String, Object> config = new HashMap<>();
        config.put("noOfResultsPerHit", "10");
        config.put("defaultMaxResultSuggestion", "3000");
        context.registerService(SiteSearchServlet.class, new SiteSearchServlet());
        context.getService(SiteSearchServlet.class);
        MockOsgi.activate(context.getService(SiteSearchServlet.class), context.bundleContext(), config);

    }

    @Test
    public void testSearch() throws IOException {

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("page", "1");
        parameterMap.put("contentType", "news,media,events,products");

        final List<String> pathList = new ArrayList<>();
        pathList.add("/content/tetrapak/publicweb/lang-masters/en/home");

        MockHelper.loadQuery(context, pathList);

        context.request().setParameterMap(parameterMap);
        SiteSearchServlet servlet = MockHelper.getServlet(context, SiteSearchServlet.class);
        servlet.doGet(context.request(), context.response());
    }
}
