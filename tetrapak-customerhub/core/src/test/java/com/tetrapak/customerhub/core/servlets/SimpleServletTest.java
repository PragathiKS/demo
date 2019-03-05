package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import io.wcm.testing.mock.aem.junit.AemContext;

public class SimpleServletTest {
    
    @Rule
    public final AemContext context = CuhuCoreAemContext.getAemContext();
    
    @Before
    public void setup() {
        context.currentResource(CuhuCoreAemContext.GET_SIMPLE_SERVLET_ROOT);
    }
    
    @Test
    public void doGet() throws ServletException, IOException {
        
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        SimpleServlet simpleServlet = context.getService(SimpleServlet.class);
        simpleServlet.doGet(request, response);
        assertEquals("Title = resource title", response.getOutputAsString());
    }
}
