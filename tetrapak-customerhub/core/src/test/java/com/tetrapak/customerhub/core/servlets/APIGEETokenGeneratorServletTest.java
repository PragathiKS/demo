package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.impl.APIGEEServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class APIGEETokenGeneratorServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/bin/customerhub/token-generator";
    private static final String SERVLET_RESOURCE_JSON = "simpleservlet.json";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(SERVLET_RESOURCE_JSON, SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() {
        aemContext.currentResource(SERVLET_RESOURCE_PATH);
        aemContext.request().setServletPath(SERVLET_RESOURCE_PATH);
        aemContext.request().setMethod(HttpConstants.METHOD_GET);
    }

    @Test
    public void doGet() throws IOException {
        MockSlingHttpServletRequest request = aemContext.request();
        MockSlingHttpServletResponse response = aemContext.response();
        APIGEETokenGeneratorServlet apigeeTokenGeneratorServlet = aemContext.getService(APIGEETokenGeneratorServlet.class);
        aemContext.registerInjectActivateService(apigeeTokenGeneratorServlet);
        apigeeTokenGeneratorServlet.doGet(request, response);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    public <T> List<GenericServiceType<T>> getMultipleMockedService(){

        GenericServiceType<APIGEEService> apigeeServiceGenericServiceType = new GenericServiceType<>();
        apigeeServiceGenericServiceType.setClazzType(APIGEEService.class);
        apigeeServiceGenericServiceType.set(new MockAPIGEEServiceImpl());

        GenericServiceType<APIGEETokenGeneratorServlet> apigeeTokenGeneratorServletGenericServiceType = new GenericServiceType<>();
        apigeeTokenGeneratorServletGenericServiceType.setClazzType(APIGEETokenGeneratorServlet.class);
        apigeeTokenGeneratorServletGenericServiceType.set(new APIGEETokenGeneratorServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>)apigeeServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>)apigeeTokenGeneratorServletGenericServiceType);
        return serviceTypes;
    }
}
