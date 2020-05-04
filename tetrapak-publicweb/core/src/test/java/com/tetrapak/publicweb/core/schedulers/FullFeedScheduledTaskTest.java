package com.tetrapak.publicweb.core.schedulers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.replication.Replicator;
import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.mock.MockReplicatorImpl;
import com.tetrapak.publicweb.core.mock.MockScheduler;
import com.tetrapak.publicweb.core.services.APIGEEService;
import com.tetrapak.publicweb.core.services.ProductService;
import com.tetrapak.publicweb.core.services.impl.APIGEEServiceImpl;
import com.tetrapak.publicweb.core.services.impl.ProductServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FullFeedScheduledTaskTest {

    @Rule
    public AemContext context = new AemContext();
    
    /** The apiGEE service. */
    APIGEEService apiGEEService;

    /** The product service. */
    ProductService productService;

    /** The replicator. */
    Replicator replicator;
    
    ResourceResolverFactory resolverFactory;
   

    /** The scheduler. */
    @Reference
    Scheduler scheduler;

    private FullFeedImportScheduledTask fullFeedTask = new FullFeedImportScheduledTask();

    @Before
    public void setUp() throws Exception { 
    
        context.load().json("/product/root.json", PWConstants.ROOT_PATH);
        replicator = new MockReplicatorImpl();
        context.registerService(Replicator.class, replicator);
        
        scheduler = new MockScheduler(fullFeedTask);
        context.registerService(Scheduler.class, scheduler);
        
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
        config.put("fullFeedSchedulerExpression", "0 0 0 ? * SUN *");
        config.put("fullFeedSchedulerDisable", false);
        config.put("schedulerRefreshTokenTime", 2700000);
        
        MockOsgi.injectServices(fullFeedTask, context.bundleContext());
        MockOsgi.activate(fullFeedTask, context.bundleContext(),config);
    }
}
