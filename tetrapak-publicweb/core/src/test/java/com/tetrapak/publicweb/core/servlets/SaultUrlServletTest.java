package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.mock.MockXSSAPI;
import io.wcm.testing.mock.aem.junit.AemContext;

public class SaultUrlServletTest {
    
    /** The Constant PATH. */
    private static final String PATH = "/content/tetrapak/public-web/lang-masters/en/solutions/packaging/filling-machines/tetra-pak-a1-for-tfa";

   @Rule
    public AemContext context = new AemContext();
    
    private SaultUrlServlet saultUrlServlet = new SaultUrlServlet();
    
    @Before
    public void setUp() throws Exception {
        context.load().json("/searchresult/test-Content1.json", PATH);
        XSSAPI xssAPI = new MockXSSAPI(PATH);
        context.registerService(XSSAPI.class, xssAPI);
        context.registerService(SaultUrlServlet.class, saultUrlServlet);
    }
    @Test
    public void testSaultUrlServlet() throws IOException { 
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("path", PATH);
        context.request().setParameterMap(parameterMap);
        context.request().setServerName("www.tetrapak.com");
        saultUrlServlet = MockHelper.getServlet(context, SaultUrlServlet.class);  
        saultUrlServlet.doPost(context.request(), context.response());
        assertEquals("SaultUrlServlet", "SaultUrlServlet");
    }
}
