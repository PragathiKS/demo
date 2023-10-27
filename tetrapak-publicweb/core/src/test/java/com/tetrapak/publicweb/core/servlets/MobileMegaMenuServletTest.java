package com.tetrapak.publicweb.core.servlets;

import com.tetrapak.publicweb.core.mock.MockHelper;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;

public class MobileMegaMenuServletTest {

    private static final String RESOURCE_CONTENT = "/headerv2/megamenuconfigv2.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/experience-fragments/publicweb/en/solutions-mega-menu/master";

    /** The Constant RESOURCE. */
    private static final String RESOURCE_PATH = TEST_CONTENT_ROOT + "/jcr:content/root/responsivegrid/megamenuconfig";

    @Rule
    public AemContext context = new AemContext();

    private MobileMegaMenuServlet mobileMegaMenuServlet = new MobileMegaMenuServlet();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.registerService(MobileMegaMenuServlet.class, mobileMegaMenuServlet);
    }

    @Test
    public void testMegaMenuColumnsServlet() throws IOException {

        context.request().setResource(context.currentResource());
        context.currentResource(RESOURCE_PATH);
        mobileMegaMenuServlet = MockHelper.getServlet(context, MobileMegaMenuServlet.class);
        mobileMegaMenuServlet.doGet(context.request(), context.response());
    }


}
