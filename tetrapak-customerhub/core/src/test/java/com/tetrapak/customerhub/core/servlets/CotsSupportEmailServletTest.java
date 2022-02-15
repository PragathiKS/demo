package com.tetrapak.customerhub.core.servlets;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.LanguageManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tetrapak.customerhub.core.beans.aip.CotsSupportFormBean;
import com.tetrapak.customerhub.core.beans.equipment.AddEquipmentFormBean;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.models.CotsSupportModel;
import com.tetrapak.customerhub.core.services.AddEquipmentService;
import com.tetrapak.customerhub.core.services.CotsSupportService;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jcr.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CotsSupportEmailServletTest {

    private static final String TEST_FILE = "src/test/resources/cotsSupportFormBean.json";
    private static final String RESOURCE_JSON = "cotsSupportComponent.json";
    private static final String RESOURCE_PATH = "/content/tetrapak/customerhub/global/en/automation-digital/cots-support/jcr:content/root/responsivegrid/cotssupport";

    @InjectMocks
    private CotsSupportEmailServlet servlet = new CotsSupportEmailServlet();

    @Mock
    private CotsSupportService cotsSupportService;

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

    @Rule
    public final AemContext context = CuhuCoreAemContext.getAemContext(RESOURCE_JSON,RESOURCE_PATH);

    @Before public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        context.registerService(XSSAPI.class,xssAPI);
    }


    @Test public void testDoPost() throws IOException {

        Gson gson = new Gson();
        String content = readFileFromPath(TEST_FILE);
        when(cotsSupportService.sendEmail(anyList(),
                        any(CotsSupportFormBean.class),any(SlingHttpServletRequest.class)))
                .thenReturn(true);
        when(xssAPI.getValidJSON(anyString(), anyString())).thenReturn(content);
        context.request().setParameterMap(gson.fromJson(content, Map.class));
        context.request().setResource(context.resourceResolver().getResource(RESOURCE_PATH));
        servlet.doPost(context.request(), context.response());
        assertEquals("status should be ok", HttpStatus.SC_OK, context.response().getStatus());
    }

    private String readFileFromPath(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        return IOUtils.toString(fis, StandardCharsets.UTF_8);
    }
}