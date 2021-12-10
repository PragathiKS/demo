package com.tetrapak.customerhub.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
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
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AddEquipmentServletTest {

    private static final String AUTH_TOKEN = "authToken";
    private static final String TEST_FILE = "src/test/resources/addEquipmentFormBean.json";
    @InjectMocks
    private AddEquipmentServlet servlet = new AddEquipmentServlet();

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private AddEquipmentService addEquipmentService;

    @Mock
    private Cookie mockCookie;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private Session mockSession;

    @Mock
    private ResourceResolver mockResResolver;

    @Mock
    private XSSAPI xssAPI;

    @Before
    public void setUp() throws IOException {
        Gson gson = new Gson();
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
        Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
    }

    @Test
    public void testDoPostOkPayload() throws IOException {
        Gson gson = new Gson();
        String content = readFileFromPath(TEST_FILE);
        Mockito.when(request.getParameterMap()).thenReturn(gson.fromJson(content, Map.class));
        Mockito.when(addEquipmentService.addEquipment(Mockito.eq(mockSession.getUserID()),
                Mockito.any(AddEquipmentFormBean.class), Mockito.any(String.class), Mockito.any(List.class)))
                .thenReturn(new JsonObject());
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    @Test
    public void testDoPostFailPayload() throws IOException {
        Mockito.when(request.getParameterMap()).thenReturn(null);
        Mockito.when(addEquipmentService.addEquipment(Mockito.eq(mockSession.getUserID()),
                Mockito.any(AddEquipmentFormBean.class), Mockito.any(String.class), Mockito.any(List.class)))
                .thenReturn(new JsonObject());
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }

}