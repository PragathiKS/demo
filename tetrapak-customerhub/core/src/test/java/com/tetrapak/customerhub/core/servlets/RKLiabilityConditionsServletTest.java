package com.tetrapak.customerhub.core.servlets;

import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.beans.rebuildingkits.PDFLink;
import com.tetrapak.customerhub.core.beans.rebuildingkits.RKLiabilityConditionsPDF;
import com.tetrapak.customerhub.core.services.KeylinesService;
import com.tetrapak.customerhub.core.services.RKLiabilityConditionsService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintWriter;

@RunWith(MockitoJUnitRunner.class)
public class RKLiabilityConditionsServletTest {

    @InjectMocks
    private RKLiabilityConditionsServlet servlet = new RKLiabilityConditionsServlet();

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private RKLiabilityConditionsService rkLiabilityConditionsService;

    @Mock
    private ResourceResolver mockResResolver;

    @Before
    public void setUp() throws IOException {
        Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
        Mockito.when(request.getParameter("preferredLanguage")).thenReturn("en");
        Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
    }

    @Test
    public void testDoGetOk() throws IOException {
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = new RKLiabilityConditionsPDF();
        rkLiabilityConditionsPDF.setEnglishPDF(new PDFLink());
        rkLiabilityConditionsPDF.setPreferredLanguagePDF(new PDFLink());
        Mockito.when(rkLiabilityConditionsService.getPDFLinksJSON(Mockito.any(), Mockito.any()))
                .thenReturn(rkLiabilityConditionsPDF);
        servlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpStatus.SC_OK);
    }

    @Test
    public void testFailure() throws IOException {
        RKLiabilityConditionsPDF rkLiabilityConditionsPDF = new RKLiabilityConditionsPDF();
        Mockito.when(rkLiabilityConditionsService.getPDFLinksJSON(Mockito.any(), Mockito.any()))
                .thenReturn(rkLiabilityConditionsPDF);
        servlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

}
