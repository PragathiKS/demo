package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.beans.equipment.EquipmentResponse;
import com.tetrapak.customerhub.core.beans.equipment.EquipmentUpdateFormBean;
import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentDetailsServletTest {

    private static final String AUTH_TOKEN = "authToken";

    private static final String BEAN_OK_FILE = "src/test/resources/equipmentUpdateFormBean.json";

    private static final String BEAN_BAD_FILE = "src/test/resources/equipmentUpdateFormBeanFail.json";

    @InjectMocks
    private EquipmentDetailsServlet servlet = new EquipmentDetailsServlet();

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private EquipmentDetailsService equipmentDetailsService;

    @Mock
    private Cookie mockCookie;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private XSSAPI xssAPI;

    @Before
    public void setUp() {
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
        Mockito.when(xssAPI.encodeForHTML(mockCookie.getValue())).thenReturn(AUTH_TOKEN);
    }

    @Test
    public void testDoPostOkFile() throws IOException {
        String content = readFileFromPath(BEAN_OK_FILE);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(equipmentDetailsService.editEquipment(
                Mockito.any(EquipmentUpdateFormBean.class), Mockito.any(String.class)))
                .thenReturn(new EquipmentResponse("200", HttpStatus.SC_OK));
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    @Test
    public void testDoPostBadFile() throws IOException {
        String content = readFileFromPath(BEAN_BAD_FILE);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(equipmentDetailsService.editEquipment(
                Mockito.any(EquipmentUpdateFormBean.class), Mockito.any(String.class)))
                .thenReturn(new EquipmentResponse("200", HttpStatus.SC_OK));
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    @Test
    public void testDoPostNullResponse() throws IOException {
        String content = readFileFromPath(BEAN_BAD_FILE);
        Mockito.when(request.getReader()).thenReturn(new BufferedReader(new StringReader(content)));
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
        Mockito.when(equipmentDetailsService.editEquipment(
                Mockito.any(EquipmentUpdateFormBean.class), Mockito.any(String.class)))
                .thenReturn(null);
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}
