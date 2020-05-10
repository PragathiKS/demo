package com.tetrapak.publicweb.core.schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.common.base.Function;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockReplicatorImpl;
import com.tetrapak.publicweb.core.mock.MockScheduler;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.services.impl.APIGEEServiceImpl;
import com.tetrapak.publicweb.core.services.impl.ProductServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class DeltaFeedScheduledTaskTest {

    @Rule
    public AemContext context = new AemContext();
    
    /** The apiGEE service. */
    APIGEEService apiGEEService;

    /** The product service. */
    ProductService productService;

    /** The replicator. */
    Replicator replicator;
    
    ResourceResolverFactory resolverFactory;
    
    /** The query builder. */
    @Mock
    private QueryBuilder queryBuilder;
    
    /** The result. */  
    @Mock   
    SearchResult searchResult;
    
    /** The query. */   
    @Mock   
    private Query query; 
    
    /** The hit. */ 
    @Mock   
    private Hit hit;
    
    /**
     * The Constant PRODUCTS_DATA.
     */
    private static final String PRODUCTS_DATA = "/product/products.json";
   

    /** The scheduler. */
    @Reference
    Scheduler scheduler;

    private DeltaFeedImportScheduledTask deltaFeedTask = new DeltaFeedImportScheduledTask();
    
    private static final String PRODUCT_PAGE = "/content/tetrapak/public-web/lang-masters/en/solutions/processing/main-technology-areas/test-prod-1";

    @Before
    public void setUp() throws Exception { 
    
        context.load().json("/product/root.json", PWConstants.ROOT_PATH);
        context.load().json("/product/pdp.json", PRODUCT_PAGE);
        context.load().json(PRODUCTS_DATA, PWConstants.PXP_ROOT_PATH);
        MockitoAnnotations.initMocks(this);
        
        replicator = new MockReplicatorImpl();
        context.registerService(Replicator.class, replicator);
        
        scheduler = new MockScheduler(deltaFeedTask);
        context.registerService(Scheduler.class, scheduler);
        
        context.registerAdapter(ResourceResolver.class, QueryBuilder.class, new Function<ResourceResolver, QueryBuilder>() {
            @Override
            public QueryBuilder apply(ResourceResolver arg0) {
                return queryBuilder;
            }
        });
        Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).thenReturn(query);
        Mockito.when(query.getResult()).thenReturn(searchResult);
        List<Hit> hits = new ArrayList<Hit>();
        hits.add(hit);
        Mockito.when(searchResult.getHits()).thenReturn(hits);
        Mockito.when(hit.getPath()).thenReturn(PRODUCT_PAGE);
        
        final Map<String, Object> apiGeeConfig = new HashMap<String, Object>();
        apiGeeConfig.put("apigeeServiceUrl", "https://api-mig.tetrapak.com");
        apiGeeConfig.put("apigeeClientID", "Cg3webHGK4jCbK5Gvw3C1nG26OtGHvoJ");
        apiGeeConfig.put("apigeeClientSecret", "9dR6zLA1LoKt9hXC");
        apiGEEService = new APIGEEServiceImpl();
        context.registerService(APIGEEService.class, apiGEEService);
        MockOsgi.activate(apiGEEService, context.bundleContext(),apiGeeConfig);
        
        final Map<String, Object> config = new HashMap<String, Object>();
        config.put("damRootPath", "/content/dam/tetrapak/publicweb/pxp");
        config.put("videoTypes", "mp4");
        productService = new ProductServiceImpl();
        context.registerService(ProductService.class, productService);
        MockOsgi.activate(productService, context.bundleContext(),config);
        
        context.getService(APIGEEService.class);
    }

    @Test
    public void run() throws IOException {
        final Map<String, Object> config = new HashMap<String, Object>();
        config.put("deltaFeedSchedulerExpression", "0 0 0 ? * * *");
        config.put("deltaFeedSchedulerDisable", false);
        config.put("schedulerRefreshTokenTime", 2700000);
        
        MockOsgi.injectServices(deltaFeedTask, context.bundleContext());
        MockOsgi.activate(deltaFeedTask, context.bundleContext(),config);
    }
}
