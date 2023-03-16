package com.tetrapak.customerhub.core.servlets;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.services.config.RebuildingKitsEmailConfiguration;
import com.tetrapak.customerhub.core.services.impl.RebuildingKitsEmailServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import javax.jcr.Session;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RebuildingKitsEmailServletTest {

    private static final String RESOURCE_SELECTOR_EXTENSION = "rebuildingkitdetails.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/installed-equipments/rebuilding-kits/rebuilding_kits_detail/jcr:content/root/responsivegrid/rebuildingkitdetails";
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    private RebuildingKitsEmailServlet rebuildingKitsEmailServlet;
    @Mock
    private PrintWriter mockPrintWriter;
    @Mock
    private XSSAPI xssAPI;
    @Mock
    private LanguageManager languageManager;
    @Mock
    private JobManager jobManager;
    @Mock
    private EmailService emailService;
    @Mock
    private RebuildingKitsEmailConfiguration rebuildingKitsEmailConfiguration;

    @Spy
    @InjectMocks
    private RebuildingKitsEmailServiceImpl rebuildingKitsEmailServiceImpl = new RebuildingKitsEmailServiceImpl();

    private MockSlingHttpServletResponse response = context.response();

    @Spy
    private MockSlingHttpServletRequest request = context.request();

    @Spy
    private ResourceResolver resourceResolver = context.resourceResolver();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context.load().json("/" + RESOURCE_SELECTOR_EXTENSION, RESOURCE_PATH);
        context.registerService(XSSAPI.class, xssAPI);
        context.registerService(JobManager.class, jobManager);
        context.registerService(EmailService.class, emailService);
        context.registerService(LanguageManager.class, languageManager);
        context.registerService(RebuildingKitsEmailConfiguration.class, rebuildingKitsEmailConfiguration);
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(rebuildingKitsEmailServiceImpl, props);
        SlingHttpServletResponse response = spy(context.response());
        doReturn(mockPrintWriter).when(response).getWriter();
        rebuildingKitsEmailServlet = context.registerInjectActivateService(new RebuildingKitsEmailServlet(),
                props);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
    }

    @Test
    public void testSessionNull() throws IOException {
        doReturn(resourceResolver).when(request).getResourceResolver();
        when(resourceResolver.adaptTo(Session.class)).thenReturn(null);
        rebuildingKitsEmailServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_BAD_REQUEST, context.response().getStatus());
    }

    @Test
    public void testDoPost() throws IOException {
        request.setMethod("POST");
        String requestBody = "{\n" + "\t\"rkTbNumber\": \"12345\"\n" + "\t\"mcon\": \"78945\"\n"+ "\t\"functionalLocation\": \"Sweden\"\n" + "\t\"requestedCTILanguage\": \"English\"\n"+ "}";
        request.setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        rebuildingKitsEmailServlet.doPost(request, response);
        assertEquals("Status incorrect", HttpStatus.SC_ACCEPTED, context.response().getStatus());
    }

    @Test
    public void testIOException() throws IOException {
        doReturn(resourceResolver).when(request).getResourceResolver();
        when(request.getReader().lines().collect(Collectors.joining())).thenThrow(IOException.class);
        rebuildingKitsEmailServlet.doPost(request, response);
        assertEquals("Status incorrect", HttpStatus.SC_BAD_REQUEST, context.response().getStatus());
    }

}
