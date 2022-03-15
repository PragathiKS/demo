package com.tetrapak.customerhub.core.servlets;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.services.AIPCategoryService;
import com.tetrapak.customerhub.core.services.APIGEEService;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import com.tetrapak.customerhub.core.services.impl.PlantMasterLicensesServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.lang3.StringUtils;
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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class PlantMasterLicensesEmailServletTest {

 //   private static final String TEST_FILE = "src/test/resources/cotsSupportFormBean.json";
    private static final String RESOURCE_JSON = "plantMasterLicensesComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/licenses/jcr:content/root/responsivegrid/plantmasterlicenses";
    private static final String SAMPLE_STRING = "sample";

    private PlantMasterLicensesEmailServlet plantMasterLicensesEmailServlet;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private XSSAPI xssAPI;

    @Mock
    private ResourceResolver mockResResolver;

    @Mock
    private Session mockSession;

    @Mock
    private LanguageManager languageManager;

    @Mock
    private JobManager jobManager;

    @Mock
    private EmailService emailService;

    @Mock
    private AIPEmailConfiguration AIPEmailConfiguration;

    @Mock
    private APIGEEService apigeeService;

    @Mock
    private AIPCategoryService aipCategoryService;

/*    @Rule
    public final AemContext context = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);*/
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Spy
    @InjectMocks
    private PlantMasterLicensesServiceImpl plantMasterLicensesServiceImpl = new PlantMasterLicensesServiceImpl();

    private String content;

    private MockSlingHttpServletResponse response = context.response();

    @Spy
    private MockSlingHttpServletRequest request = context.request();

    @Spy
    private ResourceResolver resourceResolver = context.resourceResolver();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        context.load().json("/"+RESOURCE_JSON, RESOURCE_PATH);
        context.registerService(XSSAPI.class, xssAPI);
       // content = readFileFromPath(TEST_FILE);
       // when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        context.registerService(JobManager.class, jobManager);
        context.registerService(EmailService.class, emailService);
        context.registerService(LanguageManager.class, languageManager);
        context.registerService(AIPEmailConfiguration.class, AIPEmailConfiguration);
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(plantMasterLicensesServiceImpl, props);
        when(plantMasterLicensesServiceImpl.getI18nValue(any(), any(), any())).thenReturn(StringUtils.EMPTY);
        context.registerService(APIGEEService.class, apigeeService);
        when(apigeeService.getApiMappings()).thenReturn(new String[]{"aip-product-details:productinformation/categories/{id}/products"});
        context.registerService(AIPCategoryService.class, aipCategoryService);
        SlingHttpServletResponse response = spy(context.response());
        doReturn(mockPrintWriter).when(response).getWriter();
        plantMasterLicensesEmailServlet = context.registerInjectActivateService(new PlantMasterLicensesEmailServlet(), props);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
    }

    @Test
    public void testSessionNull() throws IOException {
        doReturn(resourceResolver).when(request).getResourceResolver();
        when(resourceResolver.adaptTo(Session.class)).thenReturn(null);
        plantMasterLicensesEmailServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, context.response().getStatus());
    }

    @Test
    public void testDoPost() throws IOException, URISyntaxException {

        request.setMethod("POST");
        request.setHeader("licenseType","engineering");
        String requestBody = "{\n" + "\t\"users\": [{\n" + "\t\t\t\"name\": \"name1\",\n"
                + "\t\t\t\"date\": \"date1\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename1\",\n"
                + "\t\t\t\t\"licensename2\"\n" + "\t\t\t]\n" + "\t\t},\n" + "\t\t{\n" + "\t\t\t\"name\": \"name2\",\n"
                + "\t\t\t\"date\": \"date2\",\n" + "\t\t\t\"licenses\": [\n" + "\t\t\t\t\"licensename3\",\n"
                + "\t\t\t\t\"licensename4\"\n" + "\t\t\t]\n" + "\t\t}\n" + "\t],\n" + "\t\"comments\": \"text\"\n" + "}";
        request.setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        plantMasterLicensesEmailServlet.doPost(request, response);
        assertEquals("Status incorrect", HttpStatus.SC_ACCEPTED, context.response().getStatus());

    }

}