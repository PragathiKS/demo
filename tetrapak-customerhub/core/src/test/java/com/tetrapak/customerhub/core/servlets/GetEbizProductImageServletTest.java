package com.tetrapak.customerhub.core.servlets;

import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.spareparts.ImageLinks;
import com.tetrapak.customerhub.core.beans.spareparts.ImageResponse;
import com.tetrapak.customerhub.core.beans.spareparts.SparePart;
import com.tetrapak.customerhub.core.services.RebuildingKitsApiService;
import com.tetrapak.customerhub.core.services.SparePartsService;
import com.tetrapak.customerhub.core.utils.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class GetEbizProductImageServletTest {

    @InjectMocks
    private GetEbizProductImageServlet servlet = new GetEbizProductImageServlet();

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private SparePartsService sparePartsService;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private ResourceResolver mockResResolver;

    @Mock
    private ImageLinks imageLinks;

    @Mock
    private ArrayList<SparePart> spareParts;

    @Mock
    private SparePart sparePart;

    @Mock
    private ImageResponse imageResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGetOkFile() throws IOException {
        when(request.getParameter("partNumber")).thenReturn("2033-0008");
        when(request.getParameter("dimension")).thenReturn("648Wx486H");
        when(sparePartsService.getImageLinks(anyString(),anyString())).thenReturn(imageLinks);
        when(imageLinks.getParts()).thenReturn(spareParts);
        when(spareParts.isEmpty()).thenReturn(false);
        when(spareParts.get(0)).thenReturn(sparePart);
        when(sparePart.getUrl()).thenReturn("https://api-mig.tetrapak.com/spareparts/media/v1/medias/sys_master/images/images/h44/haa/13150358339614/2033-0008-0-1200Wx900H-648Wx486H.jpg");
        when(sparePartsService.getImage(anyString(),anyString())).thenReturn(imageResponse);
        when(imageResponse.getImageLink()).thenReturn("https://api-mig.tetrapak.com/spareparts/media/v1/medias/sys_master/images/images/h44/haa/13150358339614/2033-0008-0-1200Wx900H-648Wx486H.jpg");
        when(imageResponse.getBinaryResponse()).thenReturn(httpResponse);
        when((httpResponse.getEntity())).thenReturn(httpEntity);
        servlet.doGet(request, response);
        Mockito.verify(response).setContentType("image/jpeg");
        Mockito.verify(response).getOutputStream();
    }

    @Test
    public void testDoGetWithNoParams() throws IOException {
        when(request.getParameter("partNumber")).thenReturn("");
        when(request.getParameter("dimension")).thenReturn("");
        when(response.getWriter()).thenReturn(mockPrintWriter);
        servlet.doGet(request, response);
        Mockito.verify(response,times(0)).getOutputStream();
    }

    @Test
    public void testDoGetWithAPIErrors() throws IOException {
        when(request.getParameter("partNumber")).thenReturn("2033-0008");
        when(request.getParameter("dimension")).thenReturn("648Wx486H");
        when(sparePartsService.getImage(anyString(),anyString())).thenReturn(imageResponse);
        JsonObject errorResponse = new JsonObject();
        HttpUtil.setJsonResponse(errorResponse,"Error from Images API",HttpStatus.SC_INTERNAL_SERVER_ERROR);
        when(imageResponse.getErrorResponse()).thenReturn(errorResponse);
        when(response.getWriter()).thenReturn(mockPrintWriter);
        servlet.doGet(request, response);
        Mockito.verify(response,times(0)).getOutputStream();
    }
}
