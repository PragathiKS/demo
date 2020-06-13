package com.tetrapak.publicweb.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.search.QueryBuilder;
import com.google.common.base.Function;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.mock.MockXSSAPI;
import com.tetrapak.publicweb.core.models.SearchFilterModel;
import com.tetrapak.publicweb.core.services.DynamicMediaService;
import com.tetrapak.publicweb.core.services.impl.DynamicMediaServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SiteSearchServletTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String EN_CONTENT_ROOT = "/content/tetrapak/public-web/lang-masters/en";

    /** The Constant TEST_CONTENT1. */
    private static final String TEST_CONTENT1 = "/content/tetrapak/public-web/lang-masters/en/solutions/packaging/filling-machines/tetra-pak-a1-for-tfa";

    /** The Constant TEST_CONTENT2. */
    private static final String TEST_CONTENT2 = "/content/tetrapak/public-web/lang-masters/en/about-tetra-pak/news---events/news-room";
    
    /** The Constant TEST_CONTENT3. */
    private static final String TEST_CONTENT3 = "/content/tetrapak/public-web/lang-masters/en/about-tetra-pak/news---events/events/plma-2020";

    /** The Constant TEST_CONTENT4. */
    private static final String TEST_CONTENT4 = "/content/tetrapak/public-web/lang-masters/en/insights/food-categories/cheese";
    
    /** The Constant SEARCH_PAGE. */
    private static final String SEARCH_PAGE = "/content/tetrapak/public-web/lang-masters/en/search";
    
    /** The Constant CURRENT_RESOURCE. */
    private static final String CURRENT_RESOURCE = SEARCH_PAGE + "/jcr:content/root/responsivegrid/searchresults";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String DAM_CONTENT_ROOT = "/content/dam/publicweb";
    
    private static final String CONTENT_TYPES = "news,media,events,products,cases";
    
    private static final String THEME = "act";

    
    /** The dynamic media service. */
    private DynamicMediaService dynamicMediaService;
    
    SiteSearchServlet siteSerarchServlet = new SiteSearchServlet();
    
    SearchFilterModel filters = new SearchFilterModel();

    @Before
    public void setUp() throws Exception {

        context.load().json("/searchresult/en.json", EN_CONTENT_ROOT);
        context.load().json("/searchresult/dam.json", DAM_CONTENT_ROOT);
        context.load().json("/searchresult/test-Content1.json", TEST_CONTENT1);
        context.load().json("/searchresult/test-Content2.json", TEST_CONTENT2);
        context.load().json("/searchresult/test-Content3.json", TEST_CONTENT3);
        context.load().json("/searchresult/test-Content4.json", TEST_CONTENT4);
        context.load().json("/searchresult/search-page.json", SEARCH_PAGE);
        context.currentResource(CURRENT_RESOURCE);
        context.request().setPathInfo(CURRENT_RESOURCE);
        
        final List<String> pathList = new ArrayList<>();
        pathList.add(TEST_CONTENT1+"/jcr:content");
        pathList.add(TEST_CONTENT2+"/jcr:content");
        pathList.add(TEST_CONTENT3+"/jcr:content");
        pathList.add(TEST_CONTENT4+"/jcr:content");
        pathList.add("/content/dam/publicweb/qa/Teaser.png");
      
        MockHelper.loadQuery(context, pathList);       
        context.registerAdapter(ResourceResolver.class, QueryBuilder.class,
                new Function<ResourceResolver, QueryBuilder>() {

                    @Override
                    public QueryBuilder apply(final ResourceResolver arg0) {
                        return context.getService(QueryBuilder.class);
                    }
                });
        
        dynamicMediaService = new DynamicMediaServiceImpl();
        final Map<String, Object> configuraionServiceConfig = new HashMap<String, Object>();
        configuraionServiceConfig.put("imageServiceUrl", "https://s7g10.scene7.com/is/image");
        configuraionServiceConfig.put("videoServiceUrl", "https://s7g10.scene7.com/is/content");
        context.registerInjectActivateService(dynamicMediaService, configuraionServiceConfig);
        
        Map<String, Object> config = new HashMap<>();
        config.put("noOfResultsPerHit", "10");
        config.put("defaultMaxResultSuggestion", "3000");
        context.registerService(SiteSearchServlet.class, siteSerarchServlet);
        context.getService(SiteSearchServlet.class);
        MockOsgi.activate(context.getService(SiteSearchServlet.class), context.bundleContext(), config);

    }

    @Test
    public void testContentTypesSearch() throws IOException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("page", "1");
        parameterMap.put("contentType", CONTENT_TYPES);
        
        XSSAPI xssAPI = new MockXSSAPI(CONTENT_TYPES);
        context.registerService(XSSAPI.class, xssAPI);

        context.request().setParameterMap(parameterMap);
        siteSerarchServlet = MockHelper.getServlet(context, SiteSearchServlet.class);
        siteSerarchServlet.doGet(context.request(), context.response());
    }
    
    @Test
    public void testThemeSearch() throws IOException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("page", "1");
        parameterMap.put("theme", THEME);
        
        XSSAPI xssAPI = new MockXSSAPI(THEME);
        context.registerService(XSSAPI.class, xssAPI);

        context.request().setParameterMap(parameterMap);
        siteSerarchServlet = MockHelper.getServlet(context, SiteSearchServlet.class);
        siteSerarchServlet.doGet(context.request(), context.response());
    }
}
