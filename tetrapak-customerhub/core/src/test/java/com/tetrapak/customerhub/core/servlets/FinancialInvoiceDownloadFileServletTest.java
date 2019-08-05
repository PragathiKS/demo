/**
 *
 */
package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.services.FinancialResultsApiService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test class for Financials Invoice Download File Servlet
 *
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialInvoiceDownloadFileServletTest {

    private static final String AUTH_TOKEN = "authToken";

	@InjectMocks
    FinancialInvoiceDownloadFileServlet servlet = new FinancialInvoiceDownloadFileServlet();

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private RequestPathInfo reqPathInfo;

    @Mock
    private Cookie mockCookie;

    @Mock
    private FinancialResultsApiService financialsResultsApiService;

    @Mock
    private HttpResponse apiResp;

    @Mock
    private StatusLine mockStatusLine;

    @Mock
    private HeaderIterator mockHeaderItr;

    @Mock
    private Header mockHeader;

    @Mock
    private HttpEntity mockApiRespEntity;

    @Mock
    private ServletOutputStream mockServletOutputStream;

    @Mock
    private PrintWriter mockWriter;
    
    @Mock
    private XSSAPI xssAPI;
    
    @Mock
    private  ResourceResolver mockResResolver;

    /**
     * Setup method for class test class.
     * 
     * @throws Exception java.lang.Exception
     */
    @Before
    public void setUp() {
        Mockito.when(reqPathInfo.getExtension()).thenReturn("pdf");
        // document Number
        Mockito.when(reqPathInfo.getSelectorString()).thenReturn("123");
        Mockito.when(request.getRequestPathInfo()).thenReturn(reqPathInfo);
        Mockito.when(request.getCookie(AUTH_TOKEN)).thenReturn(mockCookie);
		Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
		Mockito.when(mockResResolver.adaptTo(XSSAPI.class)).thenReturn(xssAPI);
        Mockito.when(xssAPI.encodeForHTML(mockCookie.getValue())).thenReturn(AUTH_TOKEN);
    }

    /**
     * Test method for
     * {@link FinancialInvoiceDownloadFileServlet#doGet(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)}.
     *
     * @throws IOException IO Exception
     */
    @Test
    public void testDoGetSlingHttpServletRequestSlingHttpServletResponse() throws IOException {
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(HttpServletResponse.SC_OK);
        Mockito.when(apiResp.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(financialsResultsApiService.getFinancialInvoice("123", AUTH_TOKEN)).thenReturn(apiResp);
        Mockito.when(apiResp.headerIterator()).thenReturn(mockHeaderItr);
        Mockito.when(mockHeaderItr.hasNext()).thenReturn(true, false);
        Mockito.when(mockHeader.getName()).thenReturn("Content-Type");
        Mockito.when(mockHeader.getValue()).thenReturn("application/pdf");
        Mockito.when(mockHeaderItr.nextHeader()).thenReturn(mockHeader);
        Mockito.when(apiResp.getEntity()).thenReturn(mockApiRespEntity);
        Mockito.when(response.getOutputStream()).thenReturn(mockServletOutputStream);
        Mockito.doNothing().when(mockApiRespEntity).writeTo(mockServletOutputStream);
        servlet.doGet(request, response);
        Mockito.verify(response).getOutputStream();
    }

    /**
     * Test method for
     * {@link FinancialInvoiceDownloadFileServlet#doGet(org.apache.sling.api.SlingHttpServletRequest, org.apache.sling.api.SlingHttpServletResponse)}.
     *
     * @throws IOException IO Exception
     */
    @Test
    public void testDoGetBadreq() throws IOException {
        Mockito.doNothing().when(response).setContentType("application/json");
        Mockito.doNothing().when(mockWriter).write(Mockito.anyString());
        Mockito.when(response.getWriter()).thenReturn(mockWriter);
        Mockito.when(reqPathInfo.getExtension()).thenReturn(StringUtils.EMPTY);
        servlet.doGet(request, response);
        Mockito.verify(mockWriter).write(Mockito.anyString());
    }

}
