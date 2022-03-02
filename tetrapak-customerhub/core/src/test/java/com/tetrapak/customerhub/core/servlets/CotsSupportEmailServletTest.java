package com.tetrapak.customerhub.core.servlets;

import com.adobe.acs.commons.email.EmailService;
import com.day.cq.wcm.api.LanguageManager;
import com.tetrapak.customerhub.core.services.config.AIPEmailConfiguration;
import com.tetrapak.customerhub.core.services.impl.CotsSupportServiceImpl;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.apache.sling.xss.XSSAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Session;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CotsSupportEmailServletTest {

    private static final String TEST_FILE = "src/test/resources/cotsSupportFormBean.json";
    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";

    private CotsSupportEmailServlet cotsSupportEmailServlet;

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

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Spy
    @InjectMocks
    private CotsSupportServiceImpl cotsSupportServiceImpl = new CotsSupportServiceImpl() ;

    private String content;

    private MockSlingHttpServletResponse response = context.response();

    @Spy
    private MockSlingHttpServletRequest request = context.request();

    @Spy
    private ResourceResolver resourceResolver = context.resourceResolver();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        context.load().json("/cotsSupportComponent.json",RESOURCE_PATH);
        context.registerService(XSSAPI.class,xssAPI);
        content = readFileFromPath(TEST_FILE);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        context.registerService(JobManager.class,jobManager);
        context.registerService(EmailService.class,emailService);
        context.registerService(LanguageManager.class,languageManager);
        context.registerService(AIPEmailConfiguration.class, AIPEmailConfiguration);
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(cotsSupportServiceImpl,props);
        when(cotsSupportServiceImpl.getI18nValue(any(),any(),any())).thenReturn("");
        SlingHttpServletResponse response = spy(context.response());
        doReturn(mockPrintWriter).when(response).getWriter();
        cotsSupportEmailServlet = context.registerInjectActivateService(new CotsSupportEmailServlet(),props);
        request.setResource(context.resourceResolver().getResource(RESOURCE_PATH));
    }

    @Test
    public void testSessionNull() throws IOException {
        doReturn(resourceResolver).when(request).getResourceResolver();
        when(resourceResolver.adaptTo(Session.class)).thenReturn(null);
        cotsSupportEmailServlet.doPost(request, response);
        assertEquals("status should be ok", HttpStatus.SC_OK, context.response().getStatus());
    }


    @Test
    public void testDoPost() throws IOException, URISyntaxException {
        RequestParameterMap map = new RequestParameterMap() {
            @Override public RequestParameter[] getValues(String s) {
                return new RequestParameter[0];
            }

            @Override public RequestParameter getValue(String s) {
                return null;
            }

            @Override public int size() {
                return 0;
            }

            @Override public boolean isEmpty() {
                return false;
            }

            @Override public boolean containsKey(Object key) {
                return false;
            }

            @Override public boolean containsValue(Object value) {
                return false;
            }

            @Override public RequestParameter[] get(Object key) {
                return new RequestParameter[0];
            }

            @Override public RequestParameter[] put(String key, RequestParameter[] value) {
                return new RequestParameter[0];
            }

            @Override public RequestParameter[] remove(Object key) {
                return new RequestParameter[0];
            }

            @Override public void putAll(Map<? extends String, ? extends RequestParameter[]> m) {

            }

            @Override public void clear() {

            }

            @Override public Set<String> keySet() {
                return null;
            }

            @Override public Collection<RequestParameter[]> values() {
                return null;
            }

            @Override public Set<Entry<String, RequestParameter[]>> entrySet() {
                List<RequestParameter> params = new ArrayList<>();
                RequestParameter param = mock(RequestParameter.class);
                when(param.getFileName()).thenReturn("test.pdf");
                when(param.getContentType()).thenReturn("application/pdf");
                when(param.isFormField()).thenReturn(false);
                try {
                    when(param.getInputStream()).thenReturn(new ByteArrayInputStream("Test".getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.add(param);
                Entry<String,RequestParameter[]> entry = new AbstractMap.SimpleEntry<>("sample",params.toArray(new RequestParameter[0]));
                Set<Entry<String, RequestParameter[]>> set = new HashSet<>();
                set.add(entry);
                return set;
            }
        };
        when(request.getRequestParameterMap()).thenReturn(map);
        request.setMethod("POST");
        cotsSupportEmailServlet.doPost(request, response);
        assertEquals("Status should be accepted", HttpStatus.SC_ACCEPTED, context.response().getStatus());
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }

}