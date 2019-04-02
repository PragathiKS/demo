package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;

import io.wcm.testing.mock.aem.junit.AemContext;

public class SimpleServletTest {
    private static final String GET_SIMPLE_SERVLET_RESSOURCE_PATH = "/content/test"; 
    private static final String GET_SIMPLE_SERVLET_RESSOURCE_JSON = "simpleservlet.json"; 
    
    @Rule
    public final AemContext context = CuhuCoreAemContext.getAemContext(GET_SIMPLE_SERVLET_RESSOURCE_JSON, GET_SIMPLE_SERVLET_RESSOURCE_PATH, getSingleMockedService());
    
    
    @Before
    public void setup() {
        context.currentResource(GET_SIMPLE_SERVLET_RESSOURCE_PATH);
    }
    
    @Test
    public void doGet() throws ServletException, IOException {
        
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();
        SimpleServlet simpleServlet = context.getService(SimpleServlet.class);
        simpleServlet.doGet(request, response);
        assertEquals("Title = resource title", response.getOutputAsString());
    }

    // Register multiple services
    @SuppressWarnings("unchecked")
    public <T> List<GenericServiceType<T>> getMultipleMockedService(){

        GenericServiceType<SimpleServlet> servletGenericsType = new GenericServiceType<>();
        servletGenericsType.setClazzType(SimpleServlet.class);
        servletGenericsType.set(new SimpleServlet());
        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>)servletGenericsType);
        return serviceTypes;
    }

    // Register single service
    @SuppressWarnings("unchecked")
    public <T> GenericServiceType<T> getSingleMockedService(){
        
        GenericServiceType<SimpleServlet> servletGenericsType = new GenericServiceType<>();
        servletGenericsType.setClazzType(SimpleServlet.class);
        servletGenericsType.set(new SimpleServlet());
        return (GenericServiceType<T>)servletGenericsType;
    }
}
