package com.tetrapak.customerhub.core.servlets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Session;

import org.apache.http.HttpStatus;
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

import com.tetrapak.customerhub.core.beans.aip.PlantMasterTrainingsFormBean;
import com.tetrapak.customerhub.core.services.PlantMasterTrainingsService;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(MockitoJUnitRunner.class)
public class PlantMasterTrainingsEmailServletTest {

    @InjectMocks
    private PlantMasterTrainingsEmailServlet servlet = new PlantMasterTrainingsEmailServlet();

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private PlantMasterTrainingsService plantMasterTrainingsService;

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
        
        Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
        Mockito.when(mockResResolver.adaptTo(Session.class)).thenReturn(mockSession);
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
    }

    @Test
    public void testDoPostOk() throws IOException {

        PlantMasterTrainingsFormBean planMasterTrainingsFormBean = new PlantMasterTrainingsFormBean();
        Mockito.when(plantMasterTrainingsService.sendEmail(planMasterTrainingsFormBean, request))
                .thenReturn(Boolean.TRUE);
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    @Test
    public void testDoPostFail() throws IOException {
        PlantMasterTrainingsFormBean planMasterTrainingsFormBean = new PlantMasterTrainingsFormBean();
        Mockito.when(request.getParameterMap()).thenReturn(null);
        Mockito.when(plantMasterTrainingsService.sendEmail(planMasterTrainingsFormBean, request))
                .thenReturn(Boolean.FALSE);
        servlet.doPost(request, response);
        Mockito.verify(response).getWriter();
    }

    @Test
    public void testSessionNull() throws IOException {
        doReturn(mockResResolver).when(request).getResourceResolver();
        when(mockResResolver.adaptTo(Session.class)).thenReturn(null);
        servlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, context.response().getStatus());
    }

}
