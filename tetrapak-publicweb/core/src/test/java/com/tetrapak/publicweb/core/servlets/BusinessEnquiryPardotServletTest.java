package com.tetrapak.publicweb.core.servlets;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.osgi.MockOsgi;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tetrapak.publicweb.core.mock.MockHelper;
import com.tetrapak.publicweb.core.services.PardotService;
import com.tetrapak.publicweb.core.services.impl.PardotServiceImpl;

import io.wcm.testing.mock.aem.junit.AemContext;

public class BusinessEnquiryPardotServletTest {

    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/gb";

    /** The Constant TEST_RESOURCE_CONTENT. */
    private static final String TEST_RESOURCE_CONTENT = "/businessinquiryform/test-content.json";

    private static final String RESOURCE = "/content/tetrapak/publicweb/gb/en/contact-us/jcr:content/businessinquiryform";

    private PardotService pardotService;
    private BusinessEnquiryPardotServlet businessEnquiryPardotServlet = new BusinessEnquiryPardotServlet();
    /**
     * Setup.
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        context.load().json(TEST_RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        pardotService = new PardotServiceImpl();
        final Map<String, Object> pardotConfig = new HashMap<>();
        context.registerService(PardotService.class, pardotService);
        MockOsgi.activate(context.getService(PardotService.class), context.bundleContext(), pardotConfig);

        businessEnquiryPardotServlet = MockHelper.getServlet(context, BusinessEnquiryPardotServlet.class);

        final Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("firstName", "firstName");
        parameterMap.put("lastName", "lastName");
        parameterMap.put("befPardotURL", "http://go.tetrapak.com/l/857883/2020-05-29/w6xt");
        context.request().setParameterMap(parameterMap);
        context.request().setResource(context.currentResource(RESOURCE));

    }

    /**
     * Do get
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void doPost() throws IOException {
        businessEnquiryPardotServlet.doPost(context.request(), context.response());
        assertEquals("Success", context.response().getOutputAsString());
    }
}
