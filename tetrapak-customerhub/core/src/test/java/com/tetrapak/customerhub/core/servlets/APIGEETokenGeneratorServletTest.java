package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.mock.GenericServiceType;
import com.tetrapak.customerhub.core.mock.MockAPIGEEServiceImpl;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {HttpClientBuilder.class, HttpUtil.class})
public class APIGEETokenGeneratorServletTest {

    private static final String SERVLET_RESOURCE_PATH = "/bin/customerhub/token-generator";
    private static final String SERVLET_RESOURCE_JSON = "allContent.json";

    @Mock
    CloseableHttpClient httpClient;
    @Mock
    HttpPost httpPost;
    @Mock
    CloseableHttpResponse httpResponse;
    @Mock
    StatusLine statusLine;
    @Mock
    HttpClientBuilder httpClientBuilder;

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(SERVLET_RESOURCE_JSON, SERVLET_RESOURCE_PATH, getMultipleMockedService());

    @Before
    public void setup() throws IOException {
        PowerMockito.mockStatic(HttpUtil.class);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", 200);
        PowerMockito.when(HttpUtil.setJsonResponse(Mockito.anyObject(), Mockito.anyObject())).thenReturn(jsonResponse);

        PowerMockito.mockStatic(HttpClientBuilder.class);
        PowerMockito.when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
        PowerMockito.when(httpClientBuilder.build()).thenReturn(httpClient);

        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(Mockito.anyObject())).thenReturn(httpResponse);

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
        assertEquals("status from response", HttpStatus.SC_OK, response.getStatus());
    }

    public <T> List<GenericServiceType<T>> getMultipleMockedService() {

        GenericServiceType<APIGEEService> apigeeServiceGenericServiceType = new GenericServiceType<>();
        apigeeServiceGenericServiceType.setClazzType(APIGEEService.class);
        apigeeServiceGenericServiceType.set(new MockAPIGEEServiceImpl());

        GenericServiceType<APIGEETokenGeneratorServlet> apigeeTokenGeneratorServletGenericServiceType = new GenericServiceType<>();
        apigeeTokenGeneratorServletGenericServiceType.setClazzType(APIGEETokenGeneratorServlet.class);
        apigeeTokenGeneratorServletGenericServiceType.set(new APIGEETokenGeneratorServlet());

        List<GenericServiceType<T>> serviceTypes = new ArrayList<>();
        serviceTypes.add((GenericServiceType<T>) apigeeServiceGenericServiceType);
        serviceTypes.add((GenericServiceType<T>) apigeeTokenGeneratorServletGenericServiceType);
        return serviceTypes;
    }
}
