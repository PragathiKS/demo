package com.tetrapak.customerhub.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
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

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.tetrapak.customerhub.core.mock.CuhuCoreAemContext;
import com.tetrapak.customerhub.core.services.KeylinesService;

import io.wcm.testing.mock.aem.junit.AemContext;

@RunWith(MockitoJUnitRunner.class)
public class KeylinesServletTest {

    @InjectMocks
    private KeylinesServlet servlet = new KeylinesServlet();

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private ResourceResolver mockResResolver;

    @Mock
    private KeylinesService keylineService;

    @Mock
    private PageManager mockPageManager;

    @Mock
    private Page mockPage;

    private static final String[] SHAPES = new String[] { "tetrapak:keylines/tetra-rex/mid",
	    "tetrapak:keylines/tetra-rex/base", "tetrapak:keylines/tetra-rex/high" };

    /** The Constant TEST_CONTENT. */
    private static final String TEST_CONTENT = "keylines.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/customerhub/content-components/en/keyline";

    /** The Constant RESOURCE_PATH. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/keylines";

    @Rule
    public final AemContext aemContext = CuhuCoreAemContext.getAemContext(TEST_CONTENT, TEST_CONTENT_ROOT);

    @Before
    public void setUp() throws IOException {
	Resource resource = aemContext.currentResource(RESOURCE_PATH);
	aemContext.load().json("/keyline-tags.json", "/content/cq:tags/tetrapak/keylines");
	aemContext.request().setResource(resource);

	Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
	Mockito.when(request.getParameter("type")).thenReturn("tetrapak:keylines/tetra-rex");
	Mockito.when(request.getParameterValues("shapes")).thenReturn(SHAPES);
	Mockito.when(request.getLocale()).thenReturn(new Locale("en"));
	Mockito.when(request.getResource()).thenReturn(resource);
	Mockito.when(request.getResourceResolver()).thenReturn(mockResResolver);
	Mockito.when(mockResResolver.adaptTo(PageManager.class)).thenReturn(mockPageManager);
	Mockito.when(mockPageManager.getContainingPage(resource)).thenReturn(mockPage);
	Mockito.when(response.getWriter()).thenReturn(mockPrintWriter);
    }

    @Test
    public void testDoGetOk() throws IOException {

	servlet.doGet(request, response);
	Mockito.verify(response).getWriter();
    }

}
