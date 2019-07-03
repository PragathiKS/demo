/**
 *
 */
package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.tetrapak.customerhub.core.services.FinancialResultsApiService;

/**
 * Test class for Financials Invoice Download File Servlet
 *
 * @author swalamba
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FinancialInvoiceDownloadFileServletTest {

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

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Mockito.when(reqPathInfo.getExtension()).thenReturn("pdf");
        Mockito.when(reqPathInfo.getSelectorString()).thenReturn("123");// document Number
        Mockito.when(request.getRequestPathInfo()).thenReturn(reqPathInfo);
        Mockito.when(request.getCookie("authToken")).thenReturn(mockCookie);
        Mockito.when(mockCookie.getValue()).thenReturn("authToken");
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
        Mockito.when(financialsResultsApiService.getFinancialInvoice("123", "authToken")).thenReturn(apiResp);
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
