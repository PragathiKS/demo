package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.beans.rebuildingkits.ImplementationStatusUpdateBean;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Session;
import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RebuildingKitsUpdateServletTest {

    private static final String AUTH_TOKEN = "authToken";

    private static final String BEAN_OK_FILE = "src/test/resources/implementationStatusUpdateBean.json";

    private static final String BEAN_BAD_FILE = "src/test/resources/implementationStatusUpdateBeanFail.json";

    @InjectMocks
    private RebuildingKitsUpdateServlet servlet = new RebuildingKitsUpdateServlet();

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private RebuildingKitsApiService rebuildingKitsApiService;

    @Mock
    private Cookie mockCookie;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private Session mockSession;

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private ResourceResolver mockResResolver;

    @Mock
    private XSSAPI xssAPI;

    @Before
    public void setUp() {
        Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
        context.registerService(XSSAPI.class, xssAPI);
    }

    @Test
    public void testDoPostOkFile() throws IOException {
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
        Mockito.when(mockCookie.getValue()).thenReturn("token");
        String content = readFileFromPath(BEAN_OK_FILE);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        String userId = mockSession.getUserID();
        Mockito.when(rebuildingKitsApiService.updateImplementationStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(
                ImplementationStatusUpdateBean.class))).thenReturn(new JsonObject());
        servlet.doPost(request, response);
        Mockito.verify(response.getWriter()).write("{}");
    }

    @Test
    public void testDoPostBadFile() throws IOException {
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
        Mockito.when(mockCookie.getValue()).thenReturn("token");
        String content = readFileFromPath(BEAN_BAD_FILE);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(rebuildingKitsApiService.updateImplementationStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(
                ImplementationStatusUpdateBean.class))).thenReturn(new JsonObject());
        servlet.doPost(request, response);
        Mockito.verify(response.getWriter()).write("{\"result\":\"bad request\",\"status\":400}");
    }

    @Test
    public void testDoPostNullResponse() throws IOException {
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
        Mockito.when(mockCookie.getValue()).thenReturn("token");
        String content = readFileFromPath(BEAN_OK_FILE);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(rebuildingKitsApiService.updateImplementationStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(
                ImplementationStatusUpdateBean.class))).thenReturn(null);
        servlet.doPost(request, response);
        Mockito.verify(response.getWriter()).write("{\"result\":\"request error\",\"status\":500}");
    }

    @Test
    public void testDoPostWithoutToken() throws IOException {
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        String content = readFileFromPath(BEAN_OK_FILE);
        context.registerService(XSSAPI.class, xssAPI);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(rebuildingKitsApiService.updateImplementationStatus(Mockito.anyString(), Mockito.anyString(), Mockito.any(
                ImplementationStatusUpdateBean.class))).thenReturn(null);
        servlet.doPost(request, response);
        Mockito.verify(response.getWriter()).write("{\"result\":\"bad request\",\"status\":400}");
    }

    @Test
    public void testWithNullSession() throws IOException {
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(null);
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        servlet.doPost(request, response);
        Mockito.verify(response.getWriter()).write("{\"result\":\"session is null\",\"status\":400}");
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }

}
